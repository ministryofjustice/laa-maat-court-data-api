package gov.uk.courtdata.dces.util;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.request.LogFdcProcessedRequest;
import gov.uk.courtdata.dces.request.LogProcessedRequest;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@UtilityClass
public class ContributionFileUtil {

    private static final String CONTRIBUTIONS_PREFIX = "CONTRIBUTIONS_";
    private static final String FDC_PREFIX = "FDC_";


    // FIELD NAMES
    private static final String ID = "ID";
    private static final String FILE_NAME = "FILE_NAME";
    private static final String RECORDS_SENT = "RECORDS_SENT";
    private static final String RECORDS_RECEIVED = "RECORDS_RECEIVED";
    private static final String DATE_CREATED = "DATE_CREATED";
    private static final String USER_CREATED = "USER_CREATED";
    private static final String DATE_MODIFIED = "DATE_MODIFIED";
    private static final String USER_MODIFIED = "USER_MODIFIED";
    private static final String XML_CONTENT = "XML_CONTENT";
    private static final String DATE_SENT = "DATE_SENT";
    private static final String DATE_RECEIVED = "DATE_RECEIVED";
    private static final String ACK_XML_CONTENT = "ACK_XML_CONTENT";
    // SQL RELATED
    private static final String SQL_PARAMETER = "?";
    private static final String SQL_XMLTYPE = "XMLType(?)";



    public void assessFilename(CreateFdcFileRequest fdcFileRequest){
        fdcFileRequest.setXmlFileName(getOrDefaultFileName(fdcFileRequest.getXmlFileName(), FDC_PREFIX));
    }
    public void assessFilename(CreateContributionFileRequest contributionFileRequest){
        contributionFileRequest.setXmlFileName(getOrDefaultFileName(contributionFileRequest.getXmlFileName(), CONTRIBUTIONS_PREFIX));
    }

    private String getOrDefaultFileName(String filename, String typePrefix){
        if(!StringUtils.isEmpty(filename)){
            return filename;
        }
        else{
            return generateFilename(typePrefix);
        }
    }

    @NotNull
    private static String generateFilename(String typePrefix) {
        final LocalDateTime date = LocalDateTime.now();
        final String filename = date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        final StringBuilder stringBuilder = new StringBuilder(typePrefix);
        stringBuilder.append(filename);
        stringBuilder.append(".xml");
        log.info("Contribution file name {}", stringBuilder);
        return stringBuilder.toString();
    }

    public ContributionFileErrorsEntity buildContributionFileError(LogContributionProcessedRequest request, ConcorContributionsEntity concorEntity){
        ContributionFileErrorsEntity errorEntity = buildBaseErrorEntity(request, concorEntity.getRepId(), concorEntity.getContribFileId());
        Integer concorId = request.getConcorId();
        errorEntity.setContributionId(concorId);
        errorEntity.setConcorContributionId(concorId);
        return errorEntity;
    }

    public ContributionFileErrorsEntity buildContributionFileError(LogFdcProcessedRequest request, FdcContributionsEntity fdcEntity){
        ContributionFileErrorsEntity errorEntity = buildBaseErrorEntity(request, fdcEntity.getRepOrderEntity().getId(), fdcEntity.getContFileId());
        Integer fdcId = request.getFdcId();
        errorEntity.setContributionId(fdcId);
        errorEntity.setFdcContributionId(fdcId);
        return errorEntity;
    }



    private ContributionFileErrorsEntity buildBaseErrorEntity(LogProcessedRequest request, Integer repId, Integer fileId){
        return ContributionFileErrorsEntity.builder()
                .errorText(request.getErrorText())
                .repId(repId)
                .contributionFileId(fileId)
                .dateCreated(LocalDateTime.now())
                .build();
    }

    public void setPreparedStatementParameters(PreparedStatement ps, Map<String,String> fieldValueMap, ContributionFilesEntity contributionFilesEntity, Clob xmlContent, Clob ackXmlContent, boolean isUpdate) throws SQLException {
        int parameterIndex = 1;

        if(fieldValueMap.containsKey(FILE_NAME)) {ps.setString(parameterIndex++, contributionFilesEntity.getFileName());}
        if(fieldValueMap.containsKey(RECORDS_SENT)) {ps.setInt(parameterIndex++, contributionFilesEntity.getRecordsSent());}
        if(fieldValueMap.containsKey(RECORDS_RECEIVED)) {ps.setInt(parameterIndex++, contributionFilesEntity.getRecordsReceived());}
        if(fieldValueMap.containsKey(USER_CREATED)) {ps.setString(parameterIndex++, contributionFilesEntity.getUserCreated());}
        if(fieldValueMap.containsKey(DATE_CREATED)) {ps.setDate(parameterIndex++, Date.valueOf(contributionFilesEntity.getDateCreated()));}
        if(fieldValueMap.containsKey(USER_MODIFIED)) {ps.setString(parameterIndex++, contributionFilesEntity.getUserModified());}
        if(fieldValueMap.containsKey(DATE_MODIFIED)) {ps.setDate(parameterIndex++, Date.valueOf(contributionFilesEntity.getDateModified()));}
        if(fieldValueMap.containsKey(DATE_SENT)) {ps.setDate(parameterIndex++, Date.valueOf(contributionFilesEntity.getDateSent()));}
        if(fieldValueMap.containsKey(DATE_RECEIVED)) {ps.setDate(parameterIndex++, Date.valueOf(contributionFilesEntity.getDateReceived()));}

        if(fieldValueMap.containsKey(XML_CONTENT)) {
            xmlContent.setString(1, contributionFilesEntity.getXmlContent());
            ps.setClob(parameterIndex++, xmlContent);
        }
        if(fieldValueMap.containsKey(ACK_XML_CONTENT)) {
            ackXmlContent.setString(1, contributionFilesEntity.getAckXmlContent());
            ps.setClob(parameterIndex++, ackXmlContent);
        }

        if(isUpdate) {
            ps.setInt(parameterIndex, contributionFilesEntity.getFileId());
        }
    }

    public Map<String, String> generateSqlFieldValueMap(ContributionFilesEntity contributionFilesEntity, boolean isUpdate){
        Map<String,String> fieldMap = new LinkedHashMap<>();

        if(!isUpdate){
            fieldMap.put(ID, "TOGDATA.S_GENERAL_SEQUENCE.NEXTVAL");
        }
        addtoFieldMap(fieldMap, FILE_NAME, contributionFilesEntity.getFileName(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, RECORDS_SENT, contributionFilesEntity.getRecordsSent(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, RECORDS_RECEIVED, contributionFilesEntity.getRecordsReceived(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, USER_CREATED, contributionFilesEntity.getUserCreated(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, DATE_CREATED, contributionFilesEntity.getDateCreated(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, USER_MODIFIED, contributionFilesEntity.getUserModified(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, DATE_MODIFIED, contributionFilesEntity.getDateModified(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, DATE_SENT, contributionFilesEntity.getDateSent(), SQL_PARAMETER);
        addtoFieldMap(fieldMap, DATE_RECEIVED, contributionFilesEntity.getDateReceived(), SQL_PARAMETER);

        if(Objects.nonNull(contributionFilesEntity.getXmlContent()) ) { fieldMap.put(XML_CONTENT, SQL_XMLTYPE); }
        if(Objects.nonNull(contributionFilesEntity.getAckXmlContent()) ) { fieldMap.put(ACK_XML_CONTENT, SQL_XMLTYPE); }

        return fieldMap;
    }

    private void addtoFieldMap(Map<String, String> fieldMap, String columnName, Object field, String sql){
        if(Objects.nonNull(field)){
            fieldMap.put(columnName,sql.formatted(field));
        }
    }


}
