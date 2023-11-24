-- We haven't included it here as we don't want a password being committed, but you need to create
-- the user before running the script below.

GRANT CREATE SESSION to DART_QUERY;
commit;

BEGIN
    -- Add TOGDATA permissions
    FOR t IN (SELECT table_name FROM dba_tables WHERE owner = 'TOGDATA') LOOP
        EXECUTE IMMEDIATE 'GRANT SELECT ON TOGDATA.' || t.table_name || ' TO DART_QUERY';
    END LOOP;

    -- Add MLA permissions
    FOR t IN (SELECT table_name FROM dba_tables WHERE owner = 'MLA') LOOP
        EXECUTE IMMEDIATE 'GRANT SELECT ON MLA.' || t.table_name || ' TO DART_QUERY';
    END LOOP;
END;





