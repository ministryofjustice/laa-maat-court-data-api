--UPDATE:

update mla.xxmla_xlat_offence
set code_meaning     = 'Application to Vacate Trial',
    offence_type     ='VA',
    application_flag = 1,
    modified_user    ='benn-m1',
    modified_date=sysdate
where offence_code = '3198';

update mla.xxmla_xlat_offence
set code_meaning     = 'Application for Special Measures',
    offence_type     ='CR',
    application_flag = 1,
    modified_user    ='benn-m1',
    modified_date=sysdate
where offence_code = 'YJC99';

update mla.xxmla_xlat_offence
set code_meaning  = 'Possess identity document belonging to another',
    offence_type  ='CE',
    modified_user ='benn-m1',
    modified_date=sysdate
where offence_code = 'DB120';


--ROLLBACK :


update mla.xxmla_xlat_offence
set code_meaning     = 'UNKNOWN OFFENCE',
    offence_type     =null,
    application_flag = 0,
    modified_user    = null,
    modified_date=null
where offence_code = '3198';

update mla.xxmla_xlat_offence
set code_meaning     = 'UNKNOWN OFFENCE',
    offence_type     =null,
    application_flag = 0,
    modified_user    = null,
    modified_date=null
where offence_code = 'YJC99';

update mla.xxmla_xlat_offence
set code_meaning  = 'UNKNOWN OFFENCE',
    offence_type  =null,
    modified_user = null,
    modified_date=null
where offence_code = 'DB120';