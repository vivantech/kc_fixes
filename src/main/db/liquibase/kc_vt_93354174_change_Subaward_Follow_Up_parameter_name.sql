--liquibase formatted sql

--changeset ekualiti:93354174_change_Subaward_Follow_Up_parameter_name

UPDATE KRCR_PARM_T set PARM_NM = 'Subaward_Follow_Up' where PARM_NM = 'Subaward Follow Up';

