--liquibase formatted sql

--changeset ekualiti:01-KC-fixes-add-active-indicator

ALTER TABLE ACTIVITY_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE PROTOCOL_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';
