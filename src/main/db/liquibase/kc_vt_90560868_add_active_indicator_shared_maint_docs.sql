--liquibase formatted sql

--changeset ekualiti:kc-vt-90560868-add-active-indicator-shared-documents

ALTER TABLE APPOINTMENT_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE BUDGET_CATEGORY ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE BUDGET_CATEGORY_MAPPING ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE BUDGET_CATEGORY_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE COMMENT_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE CONTACT_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE CONTACT_USAGE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE CUSTOM_ATTRIBUTE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE NOTICE_OF_OPPORTUNITY ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE ORGANIZATION ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE ORGANIZATION_TYPE_LIST ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE PROPOSAL_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE RATE_CLASS_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE FORMULATED_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE UNIT_FORMULATED_COST ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE QUESTION ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE GROUP_TYPES ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE QUESTION_TYPES ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE SPECIAL_REVIEW ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE VALID_SP_REV_APPROVAL ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE UNIT_ADMINISTRATOR ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE UNIT_ADMINISTRATOR_TYPE ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';

ALTER TABLE WATERMARK ADD COLUMN ACTIVE_FLAG varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y';