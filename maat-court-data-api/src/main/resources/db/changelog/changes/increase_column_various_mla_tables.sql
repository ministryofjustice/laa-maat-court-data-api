ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify FORENAME varchar2(100 char);
ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify SURNAME varchar2(100 char);
ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify ADDRESS_LINE3 varchar2(100 char);
ALTER TABLE "MLA"."XXMLA_WQ_RESULT" modify RESULT_TEXT varchar2(4000 char);
ALTER TABLE "MLA"."XXMLA_WQ_OFFENCE" modify OFFENCE_WORDING varchar2(4000 char);

ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify ADDRESS_LINE1 varchar2(100 char);
ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify ADDRESS_LINE2 varchar2(100 char);
ALTER TABLE "MLA"."XXMLA_OFFENCE" modify OFFENCE_WORDING varchar2(4000 char);
ALTER TABLE "MLA"."XXMLA_DEFENDANT" modify SURNAME varchar2(100 char);
COMMIT;

-- Following script is to reverse/back-out changes in case of any problem

ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify FORENAME varchar2(35 char);
ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify SURNAME varchar2(35 char);
ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify ADDRESS_LINE3 varchar2(35 char);
ALTER TABLE "MLA"."XXMLA_WQ_RESULT" modify RESULT_TEXT varchar2(2500 char);
ALTER TABLE "MLA"."XXMLA_WQ_OFFENCE" modify OFFENCE_WORDING varchar2(2500 char);

ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify ADDRESS_LINE1 varchar2(35 char);
ALTER TABLE "MLA"."XXMLA_WQ_DEFENDANT" modify ADDRESS_LINE2 varchar2(35 char);
ALTER TABLE "MLA"."XXMLA_OFFENCE" modify OFFENCE_WORDING varchar2(2500 char);
ALTER TABLE "MLA"."XXMLA_DEFENDANT" modify SURNAME varchar2(50 char)

-- LASB-2294 - SQL Queries executed on 8th Aug 2023

ALTER TABLE "MLA"."XXMLA_RESULT" modify RESULT_TEXT varchar2(4000 char);

--The following script is to roll back the changes in case of any problem :

ALTER TABLE "MLA"."XXMLA_RESULT" modify RESULT_TEXT varchar2(2500 char);
