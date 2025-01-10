CREATE SCHEMA IF NOT EXISTS MLA;
CREATE SCHEMA IF NOT EXISTS TOGDATA;
CREATE SCHEMA IF NOT EXISTS HUB;
CREATE SEQUENCE  IF NOT EXISTS "MLA"."TXID";
CREATE SEQUENCE  IF NOT EXISTS "MLA"."CASEID";
CREATE SEQUENCE  IF NOT EXISTS "MLA"."PROCEDING";
CREATE SEQUENCE  IF NOT EXISTS "MLA"."LIBRAID";

DROP TABLE IF EXISTS TOGDATA.CONFIG_PARAMETERS;

CREATE TABLE TOGDATA.CONFIG_PARAMETERS
("CODE" VARCHAR(30),
"DESCRIPTION" VARCHAR(200),
"VALUE" VARCHAR(70) ,
"DATE_CREATED" DATE,
"USER_CREATED" VARCHAR(50),
"TIME_STAMP" TIMESTAMP (6),
"USER_MODIFIED" VARCHAR(50),
"EFFECTIVE_DATE" DATE);

INSERT INTO TOGDATA.CONFIG_PARAMETERS
VALUES ('RESERVATION_TIME', 'description', '24', null, 'test', null, 'test', '2022-01-01');

CREATE SCHEMA IF NOT EXISTS APPLICATION;
DROP ALIAS IF EXISTS APPLICATION.UPDATE_CC_OUTCOME;