DROP TABLE MLA.XXMLA_HEARING CASCADE CONSTRAINTS;
CREATE TABLE MLA.XXMLA_HEARING
(
    ID VARCHAR2(100)  NOT NULL,
    HEARING_UUID VARCHAR2(100),
    MAAT_ID NUMBER,
    WQ_JURISDICTION_TYPE VARCHAR2(50),
    OU_COURT_LOCATION VARCHAR2(50),
    CASE_URN VARCHAR2(50),
    RESULT_CODES VARCHAR2(100),
    CREATED_DATE_TIME TIMESTAMP (6) NOT NULL,
    UPDATED_DATE_TIME TIMESTAMP (6)
)
LOGGING
NOCOMPRESS
NOCACHE
NOPARALLEL
MONITORING;