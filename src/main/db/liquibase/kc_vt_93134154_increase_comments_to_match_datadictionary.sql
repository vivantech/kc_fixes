--liquibase formatted sql

--changeset ekualiti:kc_vt_93134154_increase_comments_to_match_datadictionary.sql

ALTER TABLE subaward_amount_info MODIFY COMMENTS VARCHAR(4000);

ALTER TABLE subaward_closeout MODIFY COMMENTS VARCHAR(4000);

ALTER TABLE transaction_details MODIFY COMMENTS VARCHAR(2000);