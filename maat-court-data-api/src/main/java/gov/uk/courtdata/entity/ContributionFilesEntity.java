package gov.uk.courtdata.entity;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTRIBUTION_FILES", schema = "TOGDATA")
@XmlType
    public class ContributionFilesEntity {

    private static final String SQL_DATE_VALUE = "TO_DATE('%s','yyyy-mm-dd')";
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
    public static final String SQL_STRING_VALUE = "'?'";
    public static final String SQL_INTEGER_VALUE = "?";
    public static final String SQL_XMLTYPE = "XMLType(?)";


    @Id
        @SequenceGenerator(name = "contributions_files_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contributions_files_gen_seq")
        @Column(name = "ID", nullable = false)
        private Integer fileId;

        @Column(name = FILE_NAME, length = 100, nullable = false)
        private String fileName;

        @Column(name = RECORDS_SENT)
        private Integer recordsSent;

        @Column(name = RECORDS_RECEIVED)
        private Integer recordsReceived;

        @CreationTimestamp
        @Column(name = DATE_CREATED)
        private LocalDate dateCreated;

        @Builder.Default
        @Column(name = USER_CREATED)
        private String userCreated = "DCES";

        @Column(name = DATE_MODIFIED)
        private LocalDate dateModified;

        @Column(name = USER_MODIFIED, length = 225)
        private String userModified;

        @Column(name = XML_CONTENT)
        private String xmlContent;

        @UpdateTimestamp
        @Column(name = DATE_SENT)
        private LocalDate dateSent;

        @Column(name = DATE_RECEIVED)
        private LocalDate dateReceived;

        @Column(name = ACK_XML_CONTENT)
        private String ackXmlContent;

        public void incrementReceivedCount(){
            if (Objects.isNull(this.recordsReceived)){
                this.recordsReceived=1;
            }
            else{
                this.recordsReceived++;
            }
        }

        public Map<String, String> generateInsertStatementMap(){
            Map<String,String> fieldMap = new HashMap<>();
            fieldMap.put("ID", "TOGDATA.S_GENERAL_SEQUENCE.NEXTVAL");
            addtoFieldMap(fieldMap, FILE_NAME, fileName, SQL_STRING_VALUE);
            addtoFieldMap(fieldMap, RECORDS_SENT, recordsSent, SQL_INTEGER_VALUE);
            addtoFieldMap(fieldMap, RECORDS_RECEIVED, recordsReceived, SQL_INTEGER_VALUE);
            addtoFieldMap(fieldMap, USER_CREATED, userCreated, SQL_STRING_VALUE);
            addtoFieldMap(fieldMap, DATE_MODIFIED, dateModified, SQL_DATE_VALUE);
            addtoFieldMap(fieldMap, USER_MODIFIED, recordsSent, SQL_STRING_VALUE);
            addtoFieldMap(fieldMap, DATE_SENT, recordsSent, SQL_DATE_VALUE);
            addtoFieldMap(fieldMap, DATE_RECEIVED, recordsSent, SQL_DATE_VALUE);

            if(Objects.nonNull(xmlContent) ) { fieldMap.put(XML_CONTENT, SQL_XMLTYPE); }
            if(Objects.nonNull(ackXmlContent) ) { fieldMap.put(ACK_XML_CONTENT, SQL_XMLTYPE); }

            return fieldMap;
        }

        private String generateUpdateSQLSetStatement(){
        List<String> fieldList = new ArrayList<>();
        addToSqlToFieldList(fieldList, fileName, createUpdateSqlLine(FILE_NAME,SQL_STRING_VALUE));
        addToSqlToFieldList(fieldList, recordsSent, createUpdateSqlLine(RECORDS_SENT,SQL_INTEGER_VALUE));
        addToSqlToFieldList(fieldList, recordsReceived, createUpdateSqlLine(RECORDS_RECEIVED,SQL_INTEGER_VALUE));
        addToSqlToFieldList(fieldList, dateCreated, createUpdateSqlLine(DATE_CREATED,SQL_DATE_VALUE));
        addToSqlToFieldList(fieldList, userCreated, createUpdateSqlLine(USER_CREATED,SQL_STRING_VALUE));
        addToSqlToFieldList(fieldList, dateModified, createUpdateSqlLine(DATE_MODIFIED,SQL_DATE_VALUE));
        addToSqlToFieldList(fieldList, userModified, createUpdateSqlLine(USER_MODIFIED,SQL_STRING_VALUE));
        addToSqlToFieldList(fieldList, dateSent, createUpdateSqlLine(DATE_SENT,SQL_DATE_VALUE));
        addToSqlToFieldList(fieldList, dateReceived, createUpdateSqlLine(DATE_RECEIVED,SQL_DATE_VALUE));
        if(Objects.nonNull(xmlContent)) { fieldList.add(createUpdateSqlLine(XML_CONTENT,SQL_XMLTYPE)); }
        if(Objects.nonNull(ackXmlContent)) { fieldList.add(createUpdateSqlLine(ACK_XML_CONTENT,SQL_XMLTYPE)); }
        return String.join(", ", fieldList);
        }

        private String createUpdateSqlLine(String field, String value){
            return field+"="+value;
        }

        private void addToSqlToFieldList(List<String> fieldList, Object field, String sql){
            if(Objects.nonNull(field)){
                fieldList.add(sql.formatted(field));
            }
        }

        private void addtoFieldMap(Map<String, String> fieldMap, String columnName, Object field, String sql){
            if(Objects.nonNull(field)){
                fieldMap.put(columnName,sql.formatted(field));
            }
        }

    }