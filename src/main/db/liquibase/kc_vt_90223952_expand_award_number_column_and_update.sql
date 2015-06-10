--liquibase formatted sql

--changeset ekualiti:kc_vt_90223952_expand_award_number_column_and_update

alter table AWARD modify AWARD_NUMBER VARCHAR(13);
alter table AWARD_COMMENT modify AWARD_NUMBER VARCHAR(13);
alter table AWARD_AMOUNT_INFO modify AWARD_NUMBER VARCHAR(13);
alter table AWARD_SYNC_STATUS modify AWARD_NUMBER VARCHAR(13);              
alter table AWARD_SPONSOR_CONTACTS modify AWARD_NUMBER VARCHAR(13);         
alter table AWARD_SPONSOR_TERM modify AWARD_NUMBER VARCHAR(13);             
alter table AWARD_SUBCONTRACT_GOALS_EXP modify AWARD_NUMBER VARCHAR(13);    
alter table AWARD_REPORT_NOTIFICATION_SENT modify AWARD_NUMBER VARCHAR(13); 
alter table AWARD_REPORT_TERMS modify AWARD_NUMBER VARCHAR(13);             
alter table AWARD_REPORT_TRACKING modify AWARD_NUMBER VARCHAR(13);          
alter table SUBCONTRACTING_BUD modify AWARD_NUMBER VARCHAR(13);             
alter table SUBCONTRACT_EXP_CAT modify AWARD_NUMBER VARCHAR(13);            
alter table SUBCONTRACT_EXP_CAT_DET modify AWARD_NUMBER VARCHAR(13);        
alter table SUB_EXP_CAT_BY_FY modify AWARD_NUMBER VARCHAR(13);              
alter table TIME_AND_MONEY_DOCUMENT modify AWARD_NUMBER VARCHAR(13);        
alter table TRANSACTION_DETAILS modify AWARD_NUMBER VARCHAR(13);            
alter table AWARD_TRANSFERRING_SPONSOR modify AWARD_NUMBER VARCHAR(13);     
alter table AWARD_UNIT_CONTACTS modify AWARD_NUMBER VARCHAR(13);            
alter table AWARD_IDC_RATE modify AWARD_NUMBER VARCHAR(13);                 
alter table AWARD_NOTEPAD modify AWARD_NUMBER VARCHAR(13);                  
alter table AWARD_PAYMENT_SCHEDULE modify AWARD_NUMBER VARCHAR(13);         
alter table AWARD_PERSONS modify AWARD_NUMBER VARCHAR(13);                  
alter table AWARD_COST_SHARE modify AWARD_NUMBER VARCHAR(13);               
alter table AWARD_CUSTOM_DATA modify AWARD_NUMBER VARCHAR(13);              
alter table AWARD_HIERARCHY modify AWARD_NUMBER VARCHAR(13);                
alter table AWARD_CLOSEOUT modify AWARD_NUMBER VARCHAR(13);                 
alter table AWARD_AMOUNT_TRANSACTION modify AWARD_NUMBER VARCHAR(13);       
alter table AWARD_AMT_FNA_DISTRIBUTION modify AWARD_NUMBER VARCHAR(13);     
alter table AWARD_APPROVED_EQUIPMENT modify AWARD_NUMBER VARCHAR(13);       
alter table AWARD_APPROVED_FOREIGN_TRAVEL modify AWARD_NUMBER VARCHAR(13);  
alter table AWARD_APPROVED_SUBAWARDS modify AWARD_NUMBER VARCHAR(13);       

alter table EPS_PROPOSAL modify CURRENT_AWARD_NUMBER VARCHAR(13);                             
alter table TRANSACTION_DETAILS modify SOURCE_AWARD_NUMBER VARCHAR(13);                       
alter table TRANSACTION_DETAILS modify DESTINATION_AWARD_NUMBER VARCHAR(13);                  
alter table PROPOSAL modify CURRENT_AWARD_NUMBER VARCHAR(13);                                 
alter table PENDING_TRANSACTIONS modify SOURCE_AWARD_NUMBER VARCHAR(13);                      
alter table PENDING_TRANSACTIONS modify DESTINATION_AWARD_NUMBER VARCHAR(13);                 
alter table AWARD_HIERARCHY modify ROOT_AWARD_NUMBER VARCHAR(13);                             
alter table AWARD_HIERARCHY modify PARENT_AWARD_NUMBER VARCHAR(13);                           
alter table AWARD_HIERARCHY modify ORIGINATING_AWARD_NUMBER VARCHAR(13);

UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'A') where PARENT_AWARD_NUMBER = '000000-00000' and AWARD_NUMBER not like '%A';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'A' ) where award_number not like '%A' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'A') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'A')  where parent_award_number not like '%A' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'A') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'A')  where ROOT_AWARD_NUMBER not like '%A' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'A') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'A')  where ORIGINATING_AWARD_NUMBER not like '%A' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'A') = ah.AWARD_NUMBER ); 

UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'B') where PARENT_AWARD_NUMBER like '%A' and AWARD_NUMBER not like '%B';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'B' ) where award_number not like '%B' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'B') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'B')  where parent_award_number not like '%B' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'B') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'B')  where ROOT_AWARD_NUMBER not like '%B' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'B') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'B')  where ORIGINATING_AWARD_NUMBER not like '%B' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'B') = ah.AWARD_NUMBER ); 

UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'C') where PARENT_AWARD_NUMBER like '%B' and AWARD_NUMBER not like '%C';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'C' ) where award_number not like '%C' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'C') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'C')  where parent_award_number not like '%C' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'C') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'C')  where ROOT_AWARD_NUMBER not like '%C' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'C') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'C')  where ORIGINATING_AWARD_NUMBER not like '%C' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'C') = ah.AWARD_NUMBER ); 


UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'D') where PARENT_AWARD_NUMBER like '%C' and AWARD_NUMBER not like '%D';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'D' ) where award_number not like '%D' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'D') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'D')  where parent_award_number not like '%D' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'D') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'D')  where ROOT_AWARD_NUMBER not like '%D' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'D') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'D')  where ORIGINATING_AWARD_NUMBER not like '%D' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'D') = ah.AWARD_NUMBER ); 

UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'E') where PARENT_AWARD_NUMBER like '%D' and AWARD_NUMBER not like '%E';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'E' ) where award_number not like '%E' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'E') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'E')  where parent_award_number not like '%E' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'E') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'E')  where ROOT_AWARD_NUMBER not like '%E' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'E') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'E')  where ORIGINATING_AWARD_NUMBER not like '%E' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'E') = ah.AWARD_NUMBER ); 

UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'F') where PARENT_AWARD_NUMBER like '%E' and AWARD_NUMBER not like '%F';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'F' ) where award_number not like '%F' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'F') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'F')  where parent_award_number not like '%F' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'F') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'F')  where ROOT_AWARD_NUMBER not like '%F' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'F') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'F')  where ORIGINATING_AWARD_NUMBER not like '%F' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'F') = ah.AWARD_NUMBER ); 

UPDATE AWARD_HIERARCHY set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'G') where PARENT_AWARD_NUMBER like '%F' and AWARD_NUMBER not like '%G';
update award aw set AWARD_NUMBER = CONCAT(AWARD_NUMBER, 'G' ) where award_number not like '%G' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(aw.award_number, 'G') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set PARENT_AWARD_NUMBER = CONCAT(PARENT_AWARD_NUMBER, 'G')  where parent_award_number not like '%G' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.PARENT_AWARD_NUMBER, 'G') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ROOT_AWARD_NUMBER = CONCAT(ROOT_AWARD_NUMBER, 'G')  where ROOT_AWARD_NUMBER not like '%G' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ROOT_AWARD_NUMBER, 'G') = ah.AWARD_NUMBER ); 
update AWARD_HIERARCHY ah1 set ORIGINATING_AWARD_NUMBER = CONCAT(ORIGINATING_AWARD_NUMBER, 'G')  where ORIGINATING_AWARD_NUMBER not like '%G' and exists (select PARENT_AWARD_NUMBER from (select * from AWARD_HIERARCHY) ah where CONCAT(ah1.ORIGINATING_AWARD_NUMBER, 'G') = ah.AWARD_NUMBER ); 

UPDATE AWARD_SYNC_STATUS set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_SYNC_STATUS.AWARD_ID = AWARD.AWARD_ID);                           
UPDATE AWARD_SPONSOR_CONTACTS set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_SPONSOR_CONTACTS.AWARD_ID = AWARD.AWARD_ID);                 
UPDATE AWARD_SPONSOR_TERM set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_SPONSOR_TERM.AWARD_ID = AWARD.AWARD_ID);                         
UPDATE AWARD_SUBCONTRACT_GOALS_EXP set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where AWARD_SUBCONTRACT_GOALS_EXP.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));       
UPDATE AWARD_REPORT_NOTIFICATION_SENT set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where AWARD_REPORT_NOTIFICATION_SENT.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)); 
UPDATE AWARD_REPORT_TERMS set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_REPORT_TERMS.AWARD_ID = AWARD.AWARD_ID);                         
UPDATE AWARD_REPORT_TRACKING set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where AWARD_REPORT_TRACKING.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));                   

UPDATE SUBCONTRACTING_BUD set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where SUBCONTRACTING_BUD.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));                         
UPDATE SUBCONTRACT_EXP_CAT set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where SUBCONTRACT_EXP_CAT.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));                       
UPDATE SUBCONTRACT_EXP_CAT_DET set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where SUBCONTRACT_EXP_CAT_DET.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));               
UPDATE SUB_EXP_CAT_BY_FY set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where SUB_EXP_CAT_BY_FY.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));                           
UPDATE TIME_AND_MONEY_DOCUMENT set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where TIME_AND_MONEY_DOCUMENT.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));               
UPDATE TRANSACTION_DETAILS set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where TRANSACTION_DETAILS.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));                       
UPDATE AWARD_TRANSFERRING_SPONSOR set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_TRANSFERRING_SPONSOR.AWARD_ID = AWARD.AWARD_ID);         

UPDATE AWARD_UNIT_CONTACTS set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_UNIT_CONTACTS.AWARD_ID = AWARD.AWARD_ID);                       
UPDATE AWARD_IDC_RATE set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_IDC_RATE.AWARD_ID = AWARD.AWARD_ID);                                 
UPDATE AWARD_NOTEPAD set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_NOTEPAD.AWARD_ID = AWARD.AWARD_ID);                                   
UPDATE AWARD_PAYMENT_SCHEDULE set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_PAYMENT_SCHEDULE.AWARD_ID = AWARD.AWARD_ID);                 
UPDATE AWARD_PERSONS set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_PERSONS.AWARD_ID = AWARD.AWARD_ID);                                   
UPDATE AWARD_COMMENT set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_COMMENT.AWARD_ID = AWARD.AWARD_ID);                                   
UPDATE AWARD_COST_SHARE set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_COST_SHARE.AWARD_ID = AWARD.AWARD_ID);                             
UPDATE AWARD_CUSTOM_DATA set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_CUSTOM_DATA.AWARD_ID = AWARD.AWARD_ID);                           
UPDATE AWARD_CLOSEOUT set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_CLOSEOUT.AWARD_ID = AWARD.AWARD_ID);                                 
UPDATE AWARD_AMOUNT_INFO set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_AMOUNT_INFO.AWARD_ID = AWARD.AWARD_ID);                           
UPDATE AWARD_AMOUNT_TRANSACTION set AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where AWARD_AMOUNT_TRANSACTION.AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12));             
UPDATE AWARD_AMT_FNA_DISTRIBUTION set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_AMT_FNA_DISTRIBUTION.AWARD_ID = AWARD.AWARD_ID);         
UPDATE AWARD_APPROVED_EQUIPMENT set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_APPROVED_EQUIPMENT.AWARD_ID = AWARD.AWARD_ID);             
UPDATE AWARD_APPROVED_FOREIGN_TRAVEL set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_APPROVED_FOREIGN_TRAVEL.AWARD_ID = AWARD.AWARD_ID);   
UPDATE AWARD_APPROVED_SUBAWARDS set AWARD_NUMBER = (select AWARD_NUMBER from AWARD where AWARD_APPROVED_SUBAWARDS.AWARD_ID = AWARD.AWARD_ID);             

UPDATE EPS_PROPOSAL set CURRENT_AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where EPS_PROPOSAL.CURRENT_AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)) where CURRENT_AWARD_NUMBER is not null;   
UPDATE TRANSACTION_DETAILS  set SOURCE_AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where TRANSACTION_DETAILS.SOURCE_AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)) where SOURCE_AWARD_NUMBER is not null;             
UPDATE TRANSACTION_DETAILS  set DESTINATION_AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where TRANSACTION_DETAILS.DESTINATION_AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)) where DESTINATION_AWARD_NUMBER is not null;                       
UPDATE PROPOSAL set CURRENT_AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where PROPOSAL.CURRENT_AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)) where CURRENT_AWARD_NUMBER is not null;   
UPDATE PENDING_TRANSACTIONS set SOURCE_AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where PENDING_TRANSACTIONS.SOURCE_AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)) where SOURCE_AWARD_NUMBER is not null;             
UPDATE PENDING_TRANSACTIONS set DESTINATION_AWARD_NUMBER = (select distinct aw.AWARD_NUMBER from (select * from AWARD) aw where PENDING_TRANSACTIONS.DESTINATION_AWARD_NUMBER = SUBSTR(aw.AWARD_NUMBER,1,12)) where DESTINATION_AWARD_NUMBER is not null;             
update version_history vh set SEQ_OWNER_VERSION_NAME_VALUE = (select distinct aw.award_number from (select * from award) aw where vh.SEQ_OWNER_VERSION_NAME_VALUE = substr(aw.award_number, 1,12)) where SEQ_OWNER_CLASS_NAME = 'org.kuali.kra.award.home.Award' and exists  (select distinct award_number from award where vh.SEQ_OWNER_VERSION_NAME_VALUE = substr(award.award_number, 1,12));
update negotiation ng set ASSOCIATED_DOCUMENT_ID = (select distinct aw.award_number from (select * from award) aw where ng.ASSOCIATED_DOCUMENT_ID = substr(aw.award_number, 1,12)) where NEGOTIATION_ASSC_TYPE_ID = 4 and ASSOCIATED_DOCUMENT_ID is not null and exists  (select distinct award_number from award where ng.ASSOCIATED_DOCUMENT_ID = substr(award.award_number, 1,12));
update PROTOCOL_FUNDING_SOURCE fs set FUNDING_SOURCE = (select distinct aw.award_number from (select * from award) aw where fs.FUNDING_SOURCE = substr(aw.award_number, 1,12)) where FUNDING_SOURCE_TYPE_CODE = 6 and FUNDING_SOURCE is not null and exists  (select distinct award_number from award where fs.FUNDING_SOURCE = substr(award.award_number, 1,12));
update IACUC_PROTOCOL_FUNDING_SOURCE fs set FUNDING_SOURCE = (select distinct aw.award_number from (select * from award) aw where fs.FUNDING_SOURCE = substr(aw.award_number, 1,12)) where FUNDING_SOURCE_TYPE_CODE = 6 and FUNDING_SOURCE is not null and exists  (select distinct award_number from award where fs.FUNDING_SOURCE = substr(award.award_number, 1,12));

