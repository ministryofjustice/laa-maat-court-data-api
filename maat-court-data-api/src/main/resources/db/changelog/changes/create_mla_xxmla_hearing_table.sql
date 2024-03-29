DROP TABLE MLA.XXMLA_HEARING CASCADE CONSTRAINTS;
CREATE TABLE MLA.XXMLA_HEARING
(
   TX_ID NUMBER NOT NULL ENABLE,
   CASE_ID NUMBER NOT NULL ENABLE,
   HEARING_UUID VARCHAR2(37 CHAR),
   MAAT_ID NUMBER,
   WQ_JURISDICTION_TYPE VARCHAR2(50 CHAR),
   OU_COURT_LOCATION VARCHAR2(50 CHAR),
   CREATED_DATE_TIME TIMESTAMP (6) NOT NULL ENABLE,
   UPDATED_DATE_TIME TIMESTAMP (6),

   CASE_URN VARCHAR2(50),
   RESULT_CODES VARCHAR2(100),

   CONSTRAINT XXMLA_HEARING_PK PRIMARY KEY (TX_ID)
)
LOGGING
NOCOMPRESS
NOCACHE
NOPARALLEL
MONITORING;