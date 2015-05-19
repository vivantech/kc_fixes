/*
 * Copyright 2005-2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.institutionalproposal.rules;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.bo.Sponsor;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalCreditSplitBean;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalPersonAuditRule;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalPersonSaveRuleEvent;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalPersonSaveRuleImpl;
import org.kuali.kra.institutionalproposal.document.InstitutionalProposalDocument;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposal;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposalCostShare;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposalScienceKeyword;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposalUnrecoveredFandA;
import org.kuali.kra.rule.BusinessRuleInterface;
import org.kuali.kra.rule.event.KraDocumentEventBaseExtension;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.kra.service.SponsorService;
import org.kuali.rice.kns.util.AuditCluster;
import org.kuali.rice.kns.util.AuditError;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class...
 */
public class InstitutionalProposalDocumentRule extends ResearchDocumentRuleBase implements BusinessRuleInterface {
    
    public static final String DOCUMENT_ERROR_PATH = "document";
    public static final String INSTITUTIONAL_PROPOSAL_ERROR_PATH = "institutionalProposalList[0]";
    public static final String IP_ERROR_PATH = "institutionalProposal";
    
    public static final boolean VALIDATION_REQUIRED = true;
    public static final boolean CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME = false;
    
    /**
     * 
     * @see org.kuali.core.rules.DocumentRuleBase#processCustomSaveDocumentBusinessRules(
     * org.kuali.rice.krad.document.Document)
     */
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        boolean retval = true;
        MessageMap errorMap = GlobalVariables.getMessageMap();
        if (!(document instanceof InstitutionalProposalDocument)) {
            return false;
        }
        
        retval &= processUnrecoveredFandABusinessRules(document);
        retval &= processSponsorProgramBusinessRule(document);
        retval &= processInstitutionalProposalBusinessRules(document);
        retval &= processInstitutionalProposalFinancialRules(document);
        retval &= processInstitutionalProposalPersonBusinessRules(errorMap, document);
//        moved to processRunAuditBusinessRules()
//        retval &= processInstitutionalProposalPersonCreditSplitBusinessRules(document);
//        retval &= processInstitutionalProposalPersonUnitCreditSplitBusinessRules(document);
        retval &= processKeywordBusinessRule(document);
        retval &= processAccountIdBusinessRule(document);
        retval &= processCostShareRules(document);
        retval &= validateSponsors(document);
        return retval;
    }    
        
    private boolean validateSponsors(Document document) {
        boolean valid = true;
        MessageMap errorMap = GlobalVariables.getMessageMap();
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        SponsorService ss = this.getSponsorService();
        if (!ss.validateSponsor(institutionalProposalDocument.getInstitutionalProposal().getSponsor())) {
            errorMap.putError("document.institutionalProposalList[0].sponsorCode", KeyConstants.ERROR_INVALID_SPONSOR_CODE);
            valid = false;
        }
        if (!StringUtils.isEmpty(institutionalProposalDocument.getInstitutionalProposal().getPrimeSponsorCode()) &&
                !ss.validateSponsor(institutionalProposalDocument.getInstitutionalProposal().getPrimeSponsor())) {
            errorMap.putError("document.institutionalProposalList[0].primeSponsorCode", KeyConstants.ERROR_INVALID_PRIME_SPONSOR_CODE);
            valid = false;
            // ### Vivantech Fix : #87 / [#91531064] fix for the issue with Institutional Proposal with inactive sponsor not being editable
        } else if (!StringUtils.isEmpty(institutionalProposalDocument.getInstitutionalProposal().getPrimeSponsorCode()) &&
               !institutionalProposalDocument.getInstitutionalProposal().getPrimeSponsor().isActive()) {
            KNSGlobalVariables.getMessageList().add(KeyConstants.ERROR_INACTIVE_PRIME_SPONSOR_CODE);
            if (!StringUtils.isEmpty(institutionalProposalDocument.getInstitutionalProposal().getSponsorCode()) &&
               institutionalProposalDocument.getInstitutionalProposal().getSponsor().isActive()) {
                KNSGlobalVariables.getMessageList().remove(new ErrorMessage(KeyConstants.ERROR_INACTIVE_SPONSOR_CODE));
            }
        }
        return valid;
    }
    
    /**
    *
    * process Cost Share business rules.
    * @param awardDocument
    * @return
    */
    private boolean processUnrecoveredFandABusinessRules(Document document) {
        boolean valid = true;
        MessageMap errorMap = GlobalVariables.getMessageMap();
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        int i = 0;
        List<InstitutionalProposalUnrecoveredFandA> institutionalProposalUnrecoveredFandAs = 
                                    institutionalProposalDocument.getInstitutionalProposal().getInstitutionalProposalUnrecoveredFandAs();
        errorMap.addToErrorPath(DOCUMENT_ERROR_PATH);
        errorMap.addToErrorPath(IP_ERROR_PATH);
        for (InstitutionalProposalUnrecoveredFandA institutionalProposalUnrecoveredFandA : institutionalProposalUnrecoveredFandAs) {
            String errorPath = "institutionalProposalUnrecoveredFandAs[" + i + Constants.RIGHT_SQUARE_BRACKET;
            errorMap.addToErrorPath(errorPath);
            InstitutionalProposalSaveUnrecoveredFandARuleEvent event = new InstitutionalProposalSaveUnrecoveredFandARuleEvent(errorPath, 
                                                                                institutionalProposalDocument, 
                                                                                institutionalProposalUnrecoveredFandA);
            valid &= new InstitutionalProposalUnrecoveredFandARuleImpl().processSaveInstitutionalProposalUnrecoveredFandABusinessRules(event);
            errorMap.removeFromErrorPath(errorPath);
            i++;
        }
        errorMap.removeFromErrorPath(IP_ERROR_PATH);
        errorMap.removeFromErrorPath(DOCUMENT_ERROR_PATH);
        return valid;
    }
    
    /**
     * @see org.kuali.core.rule.DocumentAuditRule#processRunAuditBusinessRules(
     * org.kuali.rice.krad.document.Document)
     */
    public boolean processRunAuditBusinessRules(Document document){
        boolean retval = true;
        
        retval &= super.processRunAuditBusinessRules(document);
        retval &= new InstitutionalProposalPersonAuditRule().processRunAuditBusinessRules(document);
        retval &= processInstitutionalProposalPersonCreditSplitBusinessRules(document);
        retval &= processInstitutionalProposalPersonUnitCreditSplitBusinessRules(document);
        // ### Vivantech Fix : #87 / [#91531064] fix for the issue with Institutional Proposal with inactive sponsor not being editable
        retval &= processInactiveSponsorAuditRule(document);
        return retval;
        
        
    }
    
    // ### Vivantech Fix : #87 / [#91531064] fix for the issue with Institutional Proposal with inactive sponsor not being editable
    private boolean processInactiveSponsorAuditRule(Document document) {
        boolean valid = true;
        InstitutionalProposal institutionalProposal = ((InstitutionalProposalDocument) document).getInstitutionalProposal();
        List<AuditError> auditErrors = new ArrayList<AuditError>();
        if (!StringUtils.isEmpty(institutionalProposal.getSponsorCode())) {
            Map<String, String> primaryKeys = new HashMap<String, String>();
            primaryKeys.put("sponsorCode", institutionalProposal.getSponsorCode());
            Sponsor sp = (Sponsor) KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(Sponsor.class, primaryKeys);
            if (sp != null && !sp.isActive()) {
                auditErrors.add(new AuditError(Constants.IP_SPONSOR_KEY, KeyConstants.ERROR_INACTIVE_SPONSOR_CODE, 
                        Constants.MAPPING_INSTITUTIONAL_PROPOSAL_HOME_PAGE + "." + Constants.SPONSOR_PROGRAM_INFORMATION_PANEL_ANCHOR));
                valid &= false;
            }
        }
        if (!StringUtils.isEmpty(institutionalProposal.getPrimeSponsorCode())) {
            Map<String, String> primaryKeys = new HashMap<String, String>();
            primaryKeys.put("sponsorCode", institutionalProposal.getPrimeSponsorCode());
            Sponsor sp = (Sponsor) KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(Sponsor.class, primaryKeys);
            if (sp != null && !sp.isActive()) {
                auditErrors.add(new AuditError(Constants.IP_PRIME_SPONSOR_KEY, KeyConstants.ERROR_INACTIVE_PRIME_SPONSOR_CODE, 
                        Constants.MAPPING_INSTITUTIONAL_PROPOSAL_HOME_PAGE + "." + Constants.SPONSOR_PROGRAM_INFORMATION_PANEL_ANCHOR));
                valid &= false;
            }
        }
        
        if (auditErrors.size() > 0) {
            KNSGlobalVariables.getAuditErrorMap().put("sponsorProgramInformationAuditWarnings", new AuditCluster(Constants.SPONSOR_PROGRAM_INFORMATION_PANEL_NAME, auditErrors, Constants.AUDIT_WARNINGS));
           valid &= false;
        }
        return valid;

    }
    
    private boolean processInstitutionalProposalPersonBusinessRules(MessageMap errorMap, Document document) {
        errorMap.addToErrorPath(DOCUMENT_ERROR_PATH);
        errorMap.addToErrorPath(IP_ERROR_PATH);
        InstitutionalProposalPersonSaveRuleEvent event = new InstitutionalProposalPersonSaveRuleEvent("Project Persons", "projectPersons", document);
        boolean success = new InstitutionalProposalPersonSaveRuleImpl().processInstitutionalProposalPersonSaveBusinessRules(event);
        errorMap.removeFromErrorPath(IP_ERROR_PATH);
        errorMap.removeFromErrorPath(DOCUMENT_ERROR_PATH);
        
        return success;
    }
    
    private boolean processInstitutionalProposalPersonCreditSplitBusinessRules(Document document) {
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        return new InstitutionalProposalCreditSplitBean(institutionalProposalDocument).recalculateCreditSplit();
        
    }
    
    private boolean processInstitutionalProposalPersonUnitCreditSplitBusinessRules(Document document) {
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        return new InstitutionalProposalCreditSplitBean(institutionalProposalDocument).recalculateCreditSplit();
    }
    
    private boolean processKeywordBusinessRule(Document document) {
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        List<InstitutionalProposalScienceKeyword> keywords = institutionalProposalDocument.getInstitutionalProposal().getKeywords();
        for ( InstitutionalProposalScienceKeyword keyword : keywords ) {
            for ( InstitutionalProposalScienceKeyword keyword2 : keywords ) {
                if ( keyword == keyword2 ) {
                    continue;
                } else if ( StringUtils.equalsIgnoreCase(keyword.getScienceKeywordCode(), keyword2.getScienceKeywordCode()) ) {
                    GlobalVariables.getMessageMap().putError("document.institutionalProposalList[0].keyword", "error.proposalKeywords.duplicate");
                   
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean processAccountIdBusinessRule(Document document) {
        boolean retVal = true;
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        InstitutionalProposal institutionalProposal = institutionalProposalDocument.getInstitutionalProposal();

        String ipAccountNumber = institutionalProposal.getCurrentAccountNumber();
        String awardNumber = institutionalProposal.getCurrentAwardNumber();
        if (!StringUtils.isEmpty(awardNumber) && !StringUtils.isEmpty(ipAccountNumber)) {
            BusinessObjectService boService = KraServiceLocator.getService(BusinessObjectService.class);
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put("awardNumber", awardNumber);
            Collection awardCol = boService.findMatching(Award.class, fieldValues);
            if (!awardCol.isEmpty()) {
                Award award = (Award) (awardCol.toArray())[0];
                String awardAccountNumber = award.getAccountNumber();
                if (!StringUtils.equalsIgnoreCase(ipAccountNumber, awardAccountNumber)) {
                    GlobalVariables.getMessageMap().putError("document.institutionalProposal.currentAccountNumber",
                            "error.institutionalProposal.accountNumber.invalid", ipAccountNumber);
                    retVal = false;
                }
            }
        }
        return retVal;
    }
    
    /**
     * Validate Sponsor/program Information rule. Regex validation for CFDA number(7 digits with a period in the 3rd character and an optional alpha character in the 7th field).
     * @param proposalDevelopmentDocument
     * @return
    */
    private boolean processSponsorProgramBusinessRule(Document document) {
        boolean valid = true;
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        String errorPath = "institutionalSponsorAndProgram";
        InstitutionalProposalSponsorAndProgramRuleEvent event = new InstitutionalProposalSponsorAndProgramRuleEvent(errorPath, 
                                                               institutionalProposalDocument, institutionalProposalDocument.getInstitutionalProposal());
        valid &= new InstitutionalProposalSponsorAndProgramRuleImpl().processInstitutionalProposalSponsorAndProgramRules(event);
        return valid;
    }
    
    /**
     * Validate Sponsor/program Information rule. Regex validation for CFDA number(7 digits with a period in the 3rd character and an optional alpha character in the 7th field).
     * @param proposalDevelopmentDocument
     * @return
    */
    private boolean processInstitutionalProposalFinancialRules(Document document) {
        boolean valid = true;
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        String errorPath = "institutionalProposalFinancial";
        InstitutionalProposalFinancialRuleEvent event = new InstitutionalProposalFinancialRuleEvent(errorPath, 
                                                               institutionalProposalDocument, institutionalProposalDocument.getInstitutionalProposal());
        valid &= new InstitutionalProposalFinancialRuleImpl().processInstitutionalProposalFinancialRules(event);
        return valid;
    }    
    
    /**
     * Validate information on Institutional Proposal Tab from Institutional Proposal Home page.
     * @param proposalDevelopmentDocument
     * @return
    */
    private boolean processInstitutionalProposalBusinessRules(Document document) {
        boolean valid = true;
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        String errorPath = "institutionalProposal";
        InstitutionalProposalRuleEvent event = new InstitutionalProposalRuleEvent(errorPath, 
                                                               institutionalProposalDocument, institutionalProposalDocument.getInstitutionalProposal());
        valid &= new InstitutionalProposalRuleImpl().processInstitutionalProposalRules(event);
        return valid;
    }

    public boolean processRules(KraDocumentEventBaseExtension event) {
        boolean retVal = false;
        retVal = event.getRule().processRules(event);
        return retVal;
    }   
    
    private boolean processCostShareRules(Document document) {
        boolean valid = true;
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        String errorPath = "institutionalProposal";
        int i = 0;
        List<InstitutionalProposalCostShare> costShares = institutionalProposalDocument.getInstitutionalProposal().getInstitutionalProposalCostShares();
        for (InstitutionalProposalCostShare costShare : costShares) {
            InstitutionalProposalAddCostShareRuleEvent event = new InstitutionalProposalAddCostShareRuleEvent(errorPath, institutionalProposalDocument, costShare);
            valid &= new InstitutionalProposalAddCostShareRuleImpl().processInstitutionalProposalCostShareBusinessRules(event, i);
            i++;
        }
        return valid;
    }
    
    private SponsorService getSponsorService() {
        return KraServiceLocator.getService(SponsorService.class);
    }
}
