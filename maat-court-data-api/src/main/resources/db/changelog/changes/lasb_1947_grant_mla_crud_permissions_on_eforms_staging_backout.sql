-- Remove CRUD privileges for MLA on HUB.EFORMS_STAGING table.
-- This must be run as the ADMIN user.
REVOKE INSERT, SELECT, UPDATE, DELETE ON HUB.EFORMS_STAGING TO MLA;

