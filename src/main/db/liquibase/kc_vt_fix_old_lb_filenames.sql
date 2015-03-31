--liquibase formatted sql

--changeset vivantech:fix-old-lb-filenames

update databasechangelog set filename = replace(filename, 'kc_fixes', 'kc_vt');
