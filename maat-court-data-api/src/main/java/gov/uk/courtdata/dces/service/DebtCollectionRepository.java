package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebtCollectionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ContributionFilesRepository contributionFilesRepository;
    private static final String DB_USER_NAME = "DCES";

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
        contributionFileEntity.setUserCreated(DB_USER_NAME);
        contributionFileEntity.setDateCreated(LocalDate.now());

        contributionFileEntity = saveNonXmls(contributionFileEntity);

        log.info("Inserting into TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");
        return saveXmlTypes(contributionFileEntity);
    }

    private ContributionFilesEntity saveNonXmls(ContributionFilesEntity contributionFilesEntity){
        String xmlContent = contributionFilesEntity.getXmlContent();
        String ackXMLContent = contributionFilesEntity.getAckXmlContent();

        contributionFilesEntity.setAckXmlContent(null);
        contributionFilesEntity.setXmlContent(null);
        contributionFilesEntity = contributionFilesRepository.save(contributionFilesEntity);
        contributionFilesEntity.setXmlContent(xmlContent);
        contributionFilesEntity.setAckXmlContent(ackXMLContent);
        return contributionFilesEntity;
    }

    public boolean saveXmlTypes(ContributionFilesEntity contributionFilesEntity){
        jdbcTemplate.execute(
                (ConnectionCallback<Object>) con -> {

                    String updateSQL = "UPDATE TOGDATA.CONTRIBUTION_FILES SET XML_CONTENT = XMLType(?), ACK_XML_CONTENT = XMLType(?) WHERE ID = ?";

                    try (PreparedStatement ps = (con.prepareStatement(updateSQL))) {

                        Clob clob = con.createClob();
                        clob.setString(1, contributionFilesEntity.getXmlContent());
                        ps.setClob(1, clob);

                        Clob ackXmlContentClob = con.createClob();
                        ackXmlContentClob.setString(1, contributionFilesEntity.getAckXmlContent());
                        ps.setClob(2, ackXmlContentClob);

                        ps.setInt(3, contributionFilesEntity.getId());

                        return ps.executeQuery();
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
        return jdbcTemplate.batchUpdate(query);
    }

}