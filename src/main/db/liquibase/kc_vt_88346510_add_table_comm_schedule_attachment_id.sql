--liquibase formatted sql

--changeset ekualiti:88346510-add-table-comm-schedule-attachment-id

CREATE TABLE `comm_schedule_attachment_id` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
