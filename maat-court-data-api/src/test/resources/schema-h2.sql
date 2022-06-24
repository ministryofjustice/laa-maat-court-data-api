CREATE SCHEMA IF NOT EXISTS MLA;
CREATE SCHEMA IF NOT EXISTS TOGDATA;
CREATE SEQUENCE  IF NOT EXISTS "MLA"."TXID";
CREATE SEQUENCE  IF NOT EXISTS "MLA"."CASEID";
CREATE SEQUENCE  IF NOT EXISTS "MLA"."PROCEDING";
CREATE SEQUENCE  IF NOT EXISTS "MLA"."LIBRAID";

DROP TABLE IF EXISTS TOGDATA.CONFIG_PARAMETERS;

CREATE TABLE TOGDATA.CONFIG_PARAMETERS
("CODE" VARCHAR2(30 BYTE),
"DESCRIPTION" VARCHAR2(200 BYTE),
"VALUE" VARCHAR2(70 BYTE) ,
"DATE_CREATED" DATE DEFAULT sysdate,
"USER_CREATED" VARCHAR2(50 BYTE),
"TIME_STAMP" TIMESTAMP (6),
"USER_MODIFIED" VARCHAR2(50 BYTE),
"EFFECTIVE_DATE" DATE);

INSERT INTO TOGDATA.CONFIG_PARAMETERS
VALUES ('RESERVATION_TIME', 'description', '24', null, 'test', null, 'test', '2022-01-01');

DROP TABLE IF EXISTS TOGDATA.FINANCIAL_ASSESSMENTS;

CREATE TABLE TOGDATA.FINANCIAL_ASSESSMENTS;

ALTER TABLE TOGDATA.FINANCIAL_ASSESSMENTS ADD COLUMN IF NOT EXISTS VALID VARCHAR(1) default null;