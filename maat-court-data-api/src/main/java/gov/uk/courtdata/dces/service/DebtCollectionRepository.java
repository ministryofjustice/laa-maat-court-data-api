package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebtCollectionRepository {

    private final JdbcTemplate jdbcTemplate;
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

    public boolean saveContributionFilesEntity(ContributionFilesEntity contributionFilesEntity) {
        contributionFilesEntity.setUserCreated(DB_USER_NAME);
        contributionFilesEntity.setDateCreated(LocalDate.now());
        contributionFilesEntity.setDateSent(LocalDate.now());

        log.info("Inserting into TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");
        runContributionFilesSqlStatement(contributionFilesEntity, false);
        return true;
    }

    public boolean updateContributionFilesEntity(ContributionFilesEntity contributionFilesEntity) {
        contributionFilesEntity.setUserModified(DB_USER_NAME);
        contributionFilesEntity.setDateModified(LocalDate.now());
        log.info("Updating TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");


        runContributionFilesSqlStatement(contributionFilesEntity, true);
        return true;
    }

    private void runContributionFilesSqlStatement(ContributionFilesEntity contributionFilesEntity, boolean isUpdate) {
        String sqlStatement;
        Map<String, String> fieldValueMap = ContributionFileUtil.generateSqlFieldValueMap(contributionFilesEntity, isUpdate);
        if(isUpdate){
            sqlStatement = generateUpdateSQLStatement(fieldValueMap);
        }
        else {
            sqlStatement = generateInsertSQLStatement(fieldValueMap);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                    Clob xmlClob = con.createClob();
                    Clob ackClob = con.createClob();

                    ContributionFileUtil.setPreparedStatementParameters(ps, fieldValueMap, contributionFilesEntity, xmlClob, ackClob, isUpdate);

                    return ps;
                }, keyHolder);
        // if we're creating a new instance. We want to set the id here.
        if(!isUpdate) {
            Number newId = keyHolder.getKey();
            if(Objects.nonNull(newId)) {
                contributionFilesEntity.setFileId(newId.intValue());
            }
        }
    }

    private String generateInsertSQLStatement(Map<String, String> fieldMap){
        String sql = "INSERT INTO TOGDATA.CONTRIBUTION_FILES (%s) VALUES (%s)";

        String fields = String.join(", ", fieldMap.keySet());
        String values = String.join(", ", fieldMap.values());

        return sql.formatted(fields,values);
    }

    private String generateUpdateSQLStatement(Map<String, String> fieldMap){
        String sql = "UPDATE TOGDATA.CONTRIBUTION_FILES SET %s WHERE ID=?";
        List<String> updateSqlLine = new ArrayList<>();
        for(Map.Entry<String,String> entry: fieldMap.entrySet()){
            updateSqlLine.add(createUpdateSqlLine(entry.getKey(),entry.getValue()));
        }
        return sql.formatted(String.join(", ", updateSqlLine));
    }

    private String createUpdateSqlLine(String field, String value){
        return field+"="+value;
    }


    @SuppressWarnings("squid:S1192") // ignore "Can be a constant" as is not relevant here.
    public int globalUpdatePart1(String delay) {
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
                                                               TRUNC( ADD_MONTHS( NVL(R.SENTENCE_ORDER_DATE, SYSDATE ), ?) ) <= TRUNC(SYSDATE) 
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
        return jdbcTemplate.update(query, delay);
    }

    @SuppressWarnings("squid:S1192") // ignore "Can be a constant" as is not relevant here.
    public int globalUpdatePart2(String delay){
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
                                                              TRUNC( ADD_MONTHS( R.SENTENCE_ORDER_DATE, ?) ) > TRUNC(SYSDATE)  
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
        return jdbcTemplate.update(query,delay);
    }

    public List<Integer> getMerge1TestCandidates(int numTestCandidates){
        // note the double SELECT is to avoid < desired rows being obtained due to ROWNUM and DISTINCT ordering.
        String sqlStatement = """
                SELECT * FROM (
                SELECT DISTINCT RO.ID
                                FROM TOGDATA.CONCOR_CONTRIBUTIONS CC,
                                    TOGDATA.REP_ORDERS RO,
                                    TOGDATA.REP_ORDER_CROWN_COURT_OUTCOMES ROCCO
                                WHERE CC.REP_ID = RO.ID
                                AND RO.ID = ROCCO.REP_ID
                                AND CC.STATUS = 'SENT'
                                AND ROCCO.CCOO_OUTCOME IS NOT NULL
                                AND RO.SENTENCE_ORDER_DATE IS NOT NULL
                                AND TRUNC( ADD_MONTHS( NVL(RO.SENTENCE_ORDER_DATE, SYSDATE ), 5) ) <= TRUNC(SYSDATE)
                                AND RO.DATE_RECEIVED<'01-JAN-2015'
                               )
                               WHERE ROWNUM <= ?
                """;
         return jdbcTemplate.queryForList(sqlStatement, Integer.class, numTestCandidates);
    }

}