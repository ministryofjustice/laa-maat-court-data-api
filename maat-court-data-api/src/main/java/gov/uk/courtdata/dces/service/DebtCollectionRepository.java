package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.FdcMapper;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebtCollectionRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String USER_AUDIT = "DCES";

    /*
    Native query to avoid issues with JPA populating REP_ORDER object solely for the SENTENCE_ORDER_DATE.
     */
    List<FdcContributionsEntity> findFdcEntriesByStatus(FdcContributionsStatus status){
        String query = """
                SELECT fdc.id, fdc.FINAL_COST, fdc.DATE_CALCULATED, fdc.LGFS_COST, fdc.AGFS_COST, rep.SENTENCE_ORDER_DATE, fdc.REP_ID, fdc.STATUS, fdc.USER_MODIFIED, fdc.DATE_MODIFIED, fdc.CONT_FILE_ID
                FROM TOGDATA.FDC_CONTRIBUTIONS fdc INNER JOIN TOGDATA.REP_ORDERS rep
                    ON fdc.rep_id = rep.id
                    WHERE fdc.status = ?
                """;
        return jdbcTemplate.query(query, new FdcMapper(), status.toString());
    }

    /*
    Native query to avoid issues with JPA populating REP_ORDER object solely for the SENTENCE_ORDER_DATE.
     */
    List<FdcContributionsEntity> findFdcEntriesByIdIn(Set<Integer> idList){
        String query = String.format(
                """
                SELECT fdc.id, fdc.FINAL_COST, fdc.DATE_CALCULATED, fdc.LGFS_COST, fdc.AGFS_COST, rep.SENTENCE_ORDER_DATE, fdc.REP_ID, fdc.STATUS, fdc.USER_MODIFIED, fdc.DATE_MODIFIED, fdc.CONT_FILE_ID
                FROM TOGDATA.FDC_CONTRIBUTIONS fdc INNER JOIN TOGDATA.REP_ORDERS rep
                    ON fdc.rep_id = rep.id
                    WHERE fdc.id in ( %s )
                """, String.join(",", Collections.nCopies(idList.size(), "?")) );
        return jdbcTemplate.query(query, new FdcMapper(), idList.toArray());
    }

    public boolean saveContributionFilesEntity(ContributionFilesEntity contributionFilesEntity) {
        contributionFilesEntity.setUserCreated(USER_AUDIT);
        contributionFilesEntity.setDateCreated(LocalDate.now());
        contributionFilesEntity.setUserModified(USER_AUDIT);
        contributionFilesEntity.setDateModified(LocalDate.now());
        contributionFilesEntity.setDateSent(LocalDate.now());

        log.info("Inserting into TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");
        runContributionFilesSqlStatement(contributionFilesEntity, false);
        return true;
    }

    public boolean updateContributionFilesEntity(ContributionFilesEntity contributionFilesEntity) {
        contributionFilesEntity.setUserModified(USER_AUDIT);
        contributionFilesEntity.setDateModified(LocalDate.now());
        log.info("Updating TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");

        runContributionFilesSqlStatement(contributionFilesEntity, true);
        return true;
    }

    /***
     * This method inserts/updates CONTRIBUTION_FILES. It's required due to the manipulation/saving of the XML type fields
     * These fields do not have a proper mapping on JPA, and as such they are treated as characters, with the inbuilt character
     * limits. Which XML, as a clob extension, do not have. This method avoids this issue by saving as a clob using jdbcTemplates.
     * @param contributionFilesEntity The entity to be saved. If isUpdate=false, the id will be populated on INSERT
     * @param isUpdate If this is an INSERT, or UPDATE action
     */
    private void runContributionFilesSqlStatement(ContributionFilesEntity contributionFilesEntity, boolean isUpdate) {
        String sqlStatement;
        Map<String, String> fieldValueMap = ContributionFileUtil.generateSqlFieldValueMap(contributionFilesEntity, isUpdate);
        if (isUpdate){
            sqlStatement = generateUpdateSQLStatement(fieldValueMap);
        } else {
            sqlStatement = generateInsertSQLStatement(fieldValueMap);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    // Oracle 19c returns string `ROWID` with `Statement.RETURN_GENERATED_KEYS` flag (not numeric `ID`).
                    // https://docs.oracle.com/en/database/oracle/oracle-database/19/jjdbc/JDBC-standards-support.html#GUID-11E3AAF8-009C-418E-8263-E41EE49F2EA8
                    PreparedStatement ps = con.prepareStatement(sqlStatement, new String[] {"ID"});
                    Clob xmlClob = con.createClob();
                    Clob ackClob = con.createClob();

                    ContributionFileUtil.setPreparedStatementParameters(ps, fieldValueMap, contributionFilesEntity, xmlClob, ackClob, isUpdate);

                    return ps;
                }, keyHolder);
        // if we're creating a new instance. We want to set the id here.
        if (!isUpdate) {
            Number newId = keyHolder.getKey();
            if (Objects.nonNull(newId)) {
                contributionFilesEntity.setFileId(newId.intValue());
            }
        }
    }

    /***
     * A helper method to generate the SQL needed for the CONTRIBUTION_FILES INSERT statements.
     *
     * @param fieldMap Map containing all the field names, and values to be inserted.
     * @return The SQL statement to INSERT those field/values into the DB.
     */
    private String generateInsertSQLStatement(Map<String, String> fieldMap) {
        String sql = "INSERT INTO TOGDATA.CONTRIBUTION_FILES (%s) VALUES (%s)";

        String fields = String.join(", ", fieldMap.keySet());
        String values = String.join(", ", fieldMap.values());

        return sql.formatted(fields,values);
    }

    /***
     * A helper method to generate the SQL needed for the CONTRIBUTION_FILES UPDATE statements.
     *
     * @param fieldMap Map containing all the field names, and values to be updated.
     * @return The SQL statement to UPDATE the field/values.
     */
    private String generateUpdateSQLStatement(Map<String, String> fieldMap) {
        String sql = "UPDATE TOGDATA.CONTRIBUTION_FILES SET %s WHERE ID=?";
        List<String> updateSqlLine = new ArrayList<>();
        for (Map.Entry<String,String> entry: fieldMap.entrySet()){
            updateSqlLine.add(createUpdateSqlLine(entry.getKey(),entry.getValue()));
        }
        return sql.formatted(String.join(", ", updateSqlLine));
    }

    private String createUpdateSqlLine(String field, String value) {
        return field+"="+value;
    }


    /***
     * Sets the STATUS of Fdc Contributions to either INVALID, or REQUESTED, when they meet a range of criteria.
     * More detail on the FDC Merge statements can be found here:
     * <a href="https://dsdmoj.atlassian.net/wiki/spaces/DCES/pages/4541743889/Global+FDC+Contribution+Update+PL+SQL+issues">Global FDC Contribution Update PL SQL issues</a>
     * More detail on this part, including with inline comments can be found here:
     * <a href="https://dsdmoj.atlassian.net/wiki/spaces/DCES/pages/4541744335/1st+LOOP+FdcDelayedPickup+Change+to+an+UPDATE+statement">1st LOOP FdcDelayedPickup Change to an UPDATE statement</a>
     * @param delay number of months. Sets the delay before the FDC will be flagged for pickup.
     *              Used in "( TRUNC( ADD_MONTHS( NVL(R.SENTENCE_ORDER_DATE, SYSDATE ), ?) ) <= TRUNC(SYSDATE) )"
     * @return Number of entries modified. Includes both INVALID, and REQUESTED changes.
     */
    @SuppressWarnings("squid:S1192") // ignore "Can be a constant" as is not relevant here.
    public int setEligibleForFdcDelayedPickup(String delay) {
        log.info("eligibleForFdcDelayedPickup entered");
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
                   UPDATE SET FC.STATUS = MERGERESULT.NEWSTATUS,
                              FC.DATE_MODIFIED = SYSTIMESTAMP,
                              FC.USER_MODIFIED = 'DCES'""";
        return jdbcTemplate.update(query, delay);
    }

    /***
     * Sets the STATUS of Fdc Contributions to either INVALID, or REQUESTED, when they meet a range of criteria.
     * More detail on the FDC Merge statements can be found here:
     *      * <a href="https://dsdmoj.atlassian.net/wiki/spaces/DCES/pages/4541743889/Global+FDC+Contribution+Update+PL+SQL+issues">Global FDC Contribution Update PL SQL issues</a>
     *      * More detail on this part, including with inline comments can be found here:
     *      * <a href="https://dsdmoj.atlassian.net/wiki/spaces/DCES/pages/4543512577/2nd+LOOP+FdcFastTracking+Change+to+an+UPDATE+statementstatement">2nd LOOP FdcFastTracking Change to an UPDATE statement</a>
     * @param delay number of months. The lower bound for the date for those being accelerated.
     *              Used in "TRUNC( ADD_MONTHS( R.SENTENCE_ORDER_DATE, ?) ) > TRUNC(SYSDATE)"
     * @return Number of entries modified. Includes both INVALID, and REQUESTED changes.
     */
    @SuppressWarnings("squid:S1192") // ignore "Can be a constant" as is not relevant here.
    public int setEligibleForFdcFastTracking(String delay) {
        log.info("eligibleForFdcFastTracking entered");
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
                                                                                                                         AND FI.LATEST_COST_IND = 'Current'  
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
                                                                                             AND FI.LATEST_COST_IND = 'Current'  
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
                  UPDATE SET FC.STATUS = QUERY1.NEWSTATUS,
                             FC.DATE_MODIFIED = SYSTIMESTAMP,
                             FC.USER_MODIFIED = 'DCES'""";
        return jdbcTemplate.update(query,delay);
    }
}
