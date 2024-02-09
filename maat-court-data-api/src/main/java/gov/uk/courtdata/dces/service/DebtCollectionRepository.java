package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebtCollectionRepository {

    private final JdbcTemplate jdbcTemplate;

    List<String> getContributionFiles(final String fromDate, final String toDate) {
        String query = "SELECT cf.xml_content FROM TOGDATA.CONTRIBUTION_FILES CF " +
                "WHERE CF.FILE_NAME LIKE '%%CONTRIBUTIONS%%' " +
                "AND TO_DATE(CF.DATE_CREATED) BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') Order by CF.ID ASC";

        return jdbcTemplate.queryForList(query, String.class, fromDate, toDate);
    }

    List<String> getFdcFiles(final String fromDate, final String toDate) {
        String query = "SELECT cf.xml_content FROM TOGDATA.CONTRIBUTION_FILES CF " +
                "WHERE CF.FILE_NAME LIKE '%%FDC%%' " +
                "AND TO_DATE(CF.DATE_CREATED) BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') Order by CF.ID ASC";

        return jdbcTemplate.queryForList(query, String.class, fromDate, toDate);
    }

    public boolean save(ContributionFilesEntity contributionFileEntity) {

        log.info("Inserting into TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");
        return insertingTableRow(contributionFileEntity);
    }

    public boolean insertingTableRow(ContributionFilesEntity contributionFileEntity) {
        jdbcTemplate.execute(
                (ConnectionCallback<Object>) con -> {

                    String insertingSQL = "INSERT INTO TOGDATA.CONTRIBUTION_FILES (ID, FILE_NAME, RECORDS_SENT, DATE_CREATED, USER_CREATED, XML_CONTENT, ACK_XML_CONTENT) " +
                            "VALUES (TOGDATA.S_GENERAL_SEQUENCE.NEXTVAL, ?, ?, ?, ?, XMLType(?), XMLType(?))";

                    try (PreparedStatement ps = con.prepareStatement(insertingSQL)) {

                        ps.setString(1, contributionFileEntity.getFileName());
                        ps.setInt(2, contributionFileEntity.getRecordsSent());
                        ps.setDate(3, Date.valueOf(LocalDate.now()));
                        ps.setString(4, contributionFileEntity.getUserCreated());

                        Clob clob = con.createClob();
                        clob.setString(1, contributionFileEntity.getXmlContent());
                        ps.setClob(5, clob);

                        Clob ackXmlContentClob = con.createClob();
                        ackXmlContentClob.setString(1, contributionFileEntity.getAckXmlContent());
                        ps.setClob(6, ackXmlContentClob);

                        return ps.execute();
                    }
                }
        );
        return false;
    }

    @SuppressWarnings("squid:S1192") // ignore "Can be a constant" as is not relevant here.
    public int[] globalUpdatePart1() {
        log.info("globalUpdatePart1 entered");
        String query = """
                MERGE INTO TOGDATA.FDC_CONTRIBUTIONS FC 
                 USING  
                                 ( 
                                 SELECT F.ID, 
                                  (SELECT DECODE(SUM(CON_COUNT),0,'INVALID','REQUESTED') STATUS 
                                       FROM 
                                       ( 
                                          SELECT COUNT(C.ID) AS CON_COUNT  
                                          FROM TOGDATA.CONTRIBUTIONS C 
                                          WHERE 
                                             C.REP_ID = F.REP_ID 
                                             AND 
                                             C.TRANSFER_STATUS = 'SENT' 
                                          UNION 
                                          SELECT COUNT(CC.ID) AS CON_COUNT 
                                          FROM TOGDATA.CONCOR_CONTRIBUTIONS CC 
                                          WHERE 
                                          CC.REP_ID = F.REP_ID 
                                          AND 
                                          CC.STATUS = 'SENT' 
                                       )) NEWSTATUS 
                                                               FROM TOGDATA.FDC_CONTRIBUTIONS F 
                                                               JOIN TOGDATA.REP_ORDERS R ON ( R.ID = F.REP_ID ) 
                                                            WHERE 
                                                               TRUNC( ADD_MONTHS( NVL(R.SENTENCE_ORDER_DATE, SYSDATE ), TOGDATA.REF_DATA_MANAGEMENT.GET_CONFIG_PARAMETER ('FDC_CALCULATION_DELAY')) ) <= TRUNC(SYSDATE) 
                                                               AND 
                                                               F.LGFS_COMPLETE     = 'Y' 
                                                               AND 
                                                               F.AGFS_COMPLETE     = 'Y' 
                                                               AND 
                                                               ( F.STATUS = 'WAITING_ITEMS' 
                                                                OR   
                                                                 (F.STATUS = 'INVALID' AND F.ID = (SELECT MAX(ID) FROM TOGDATA.FDC_CONTRIBUTIONS FC WHERE FC.REP_ID = F.REP_ID)) 
                                                               ) 
                                                               AND EXISTS (SELECT 1 FROM TOGDATA.REP_ORDER_CROWN_COURT_OUTCOMES CCO WHERE CCO.REP_ID = R.ID)   
                                                               AND EXISTS (SELECT 1 FROM TOGDATA.FDC_ITEMS FI WHERE FI.FDC_ID = F.ID) 
                                                               AND F.STATUS != (SELECT DECODE(SUM(CON_COUNT),0,'INVALID','REQUESTED') 
                                                                                   FROM 
                                                                                   ( 
                                                                                      SELECT COUNT(C.ID) AS CON_COUNT  
                                                                                      FROM TOGDATA.CONTRIBUTIONS C 
                                                                                      WHERE 
                                                                                         C.REP_ID = F.REP_ID 
                                                                                         AND 
                                                                                         C.TRANSFER_STATUS = 'SENT' 
                                                                                      UNION 
                                                                                      SELECT COUNT(CC.ID) AS CON_COUNT 
                                                                                      FROM TOGDATA.CONCOR_CONTRIBUTIONS CC 
                                                                                      WHERE 
                                                                                      CC.REP_ID = F.REP_ID 
                                                                                      AND 
                                                                                      CC.STATUS = 'SENT' 
                                                                                   )) 
                                 ) MERGERESULT 
                 ON (FC.ID = MERGERESULT.ID) 
                 WHEN MATCHED THEN 
                   UPDATE SET FC.STATUS = MERGERESULT.NEWSTATUS""";
        log.info("globalUpdatePart1 exiting");
        return jdbcTemplate.batchUpdate(query);
    }

    @SuppressWarnings("squid:S1192") // ignore "Can be a constant" as is not relevant here.
    public int[] globalUpdatePart2(){
        log.info("globalUpdatePart2 entered");
        String query = """
                MERGE INTO TOGDATA.FDC_CONTRIBUTIONS FC 
                USING  
                                ( 
                                SELECT F.ID, 
                
                                      DECODE(F.ACCELERATE,'Y','Y',(SELECT NVL(MAX('Y'),'N') 
                                                                                                     FROM TOGDATA.FDC_CONTRIBUTIONS FC 
                                                                                                     WHERE FC.ID = F.ID 
                                                                                                     AND NOT EXISTS (SELECT 1 
                                                                                                                       FROM TOGDATA.FDC_ITEMS FI 
                                                                                                                       WHERE FI.FDC_ID = FC.ID 
                                                                                                                         AND NVL(FI.PAID_AS_CLAIMED,'N') = 'N' 
                                                                                                                         AND FI.ADJUSTMENT_REASON IS NULL   
                                                                                                                         AND FI.LATEST_COST_IND = 'CURRENT'  
                                                                                                                       ) 
                                                                                                     AND EXISTS (SELECT 1 FROM TOGDATA.FDC_ITEMS FITM    
                                                                                                                    WHERE FITM.FDC_ID = F.ID     
                                                                                                                      AND FITM.ADJUSTMENT_REASON IS NULL))) ACCELERATE_FLAG, 
                
                                 CASE WHEN EXISTS (SELECT 1 FROM TOGDATA.FDC_CONTRIBUTIONS FPREV 
                                                          WHERE FPREV.REP_ID = F.REP_ID 
                                                            AND FPREV.STATUS = 'SENT' 
                                                            AND FPREV.ID < F.ID) 
                                             THEN 'Y' 
                                             ELSE 'N' 
                                        END PREV_FDC_SENT, 
                
                                 (SELECT DECODE(SUM(CON_COUNT),TOGDATA.REF_DATA_MANAGEMENT.GET_CONFIG_PARAMETER ('FDC_CALCULATION_DELAY'),'INVALID','REQUESTED') STATUS 
                                      FROM 
                                      ( 
                                         SELECT COUNT(C.ID) AS CON_COUNT   
                                         FROM TOGDATA.CONTRIBUTIONS C 
                                         WHERE 
                                            C.REP_ID = F.REP_ID 
                                            AND 
                                            C.TRANSFER_STATUS = 'SENT' 
                                         UNION 
                                         SELECT COUNT(CC.ID) AS CON_COUNT 
                                         FROM TOGDATA.CONCOR_CONTRIBUTIONS CC 
                                         WHERE 
                                         CC.REP_ID = F.REP_ID 
                                         AND 
                                         CC.STATUS = 'SENT' 
                                      )) NEWSTATUS 
                
                                                              FROM TOGDATA.FDC_CONTRIBUTIONS F 
                                                              JOIN TOGDATA.REP_ORDERS R ON ( R.ID = F.REP_ID ) 
                                                           WHERE 
                                                              TRUNC( ADD_MONTHS( R.SENTENCE_ORDER_DATE, 5) ) > TRUNC(SYSDATE)  
                                                              AND 
                                                              F.LGFS_COMPLETE     = 'Y' 
                                                              AND 
                                                              F.AGFS_COMPLETE     = 'Y' 
                                                              AND 
                                                              F.STATUS = 'WAITING_ITEMS' 
                                                              AND EXISTS (SELECT 1 FROM TOGDATA.REP_ORDER_CROWN_COURT_OUTCOMES CCO WHERE CCO.REP_ID = R.ID)   
                
                                                              AND EXISTS (SELECT 1 FROM TOGDATA.FDC_ITEMS FI WHERE FI.FDC_ID = F.ID) 
                
                                                              AND F.STATUS != (SELECT DECODE(SUM(CON_COUNT),0,'INVALID','REQUESTED') 
                                                                                  FROM 
                                                                                  ( 
                                                                                     SELECT COUNT(C.ID) AS CON_COUNT   
                                                                                     FROM TOGDATA.CONTRIBUTIONS C 
                                                                                     WHERE 
                                                                                        C.REP_ID = F.REP_ID 
                                                                                        AND 
                                                                                        C.TRANSFER_STATUS = 'SENT' 
                                                                                     UNION 
                                                                                     SELECT COUNT(CC.ID) AS CON_COUNT 
                                                                                     FROM TOGDATA.CONCOR_CONTRIBUTIONS CC 
                                                                                     WHERE 
                                                                                     CC.REP_ID = F.REP_ID 
                                                                                     AND 
                                                                                     CC.STATUS = 'SENT' 
                                                                                  )) 
                
                
                    AND ((DECODE(F.ACCELERATE,'Y','Y',(SELECT NVL(MAX('Y'),'N') 
                                                                          FROM TOGDATA.FDC_CONTRIBUTIONS FC 
                                                                         WHERE FC.ID = F.ID 
                                                                           AND NOT EXISTS (SELECT 1 
                                                                                           FROM TOGDATA.FDC_ITEMS FI 
                                                                                           WHERE FI.FDC_ID = FC.ID 
                                                                                             AND NVL(FI.PAID_AS_CLAIMED,'N') = 'N' 
                                                                                             AND FI.ADJUSTMENT_REASON IS NULL  
                                                                                             AND FI.LATEST_COST_IND = 'CURRENT'  
                                                                                           ) 
                                                                           AND EXISTS (SELECT 1 FROM TOGDATA.FDC_ITEMS FITM  
                                                                                        WHERE FITM.FDC_ID = F.ID   
                                                                                          AND FITM.ADJUSTMENT_REASON IS NULL)))='Y'  
                                            AND  
                
                    (SELECT DECODE(SUM(CON_COUNT),0,'INVALID','REQUESTED') STATUS 
                                      FROM 
                                      ( 
                                         SELECT COUNT(C.ID) AS CON_COUNT   
                                         FROM TOGDATA.CONTRIBUTIONS C 
                                         WHERE 
                                            C.REP_ID = F.REP_ID 
                                            AND 
                                            C.TRANSFER_STATUS = 'SENT' 
                                         UNION 
                                         SELECT COUNT(CC.ID) AS CON_COUNT 
                                         FROM TOGDATA.CONCOR_CONTRIBUTIONS CC 
                                         WHERE 
                                         CC.REP_ID = F.REP_ID 
                                         AND 
                                         CC.STATUS = 'SENT' 
                                      ))='REQUESTED') 
                OR 
                
                
                (( CASE WHEN EXISTS (SELECT 1 FROM TOGDATA.FDC_CONTRIBUTIONS FPREV 
                                                          WHERE FPREV.REP_ID = F.REP_ID 
                                                            AND FPREV.STATUS = 'SENT' 
                                                            AND FPREV.ID < F.ID) 
                                             THEN 'Y' 
                                             ELSE 'N' 
                   END)='Y' 
                                AND 
                
                (SELECT DECODE(SUM(CON_COUNT),0,'INVALID','REQUESTED') STATUS 
                                      FROM 
                                      ( 
                                         SELECT COUNT(C.ID) AS CON_COUNT   
                                         FROM TOGDATA.CONTRIBUTIONS C 
                                         WHERE 
                                            C.REP_ID = F.REP_ID 
                                            AND 
                                            C.TRANSFER_STATUS = 'SENT' 
                                         UNION 
                                         SELECT COUNT(CC.ID) AS CON_COUNT 
                                         FROM TOGDATA.CONCOR_CONTRIBUTIONS CC 
                                         WHERE 
                                         CC.REP_ID = F.REP_ID 
                                         AND 
                                         CC.STATUS = 'SENT' 
                                      ))='REQUESTED')) 
                                ) QUERY1 
                ON (FC.ID = QUERY1.ID) 
                WHEN MATCHED THEN 
                  UPDATE SET FC.STATUS = QUERY1.NEWSTATUS""";
        log.info("globalUpdatePart2 exiting");
        return jdbcTemplate.batchUpdate(query);
    }

}