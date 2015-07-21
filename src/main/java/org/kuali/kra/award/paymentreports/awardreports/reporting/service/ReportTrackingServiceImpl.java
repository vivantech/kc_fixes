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
package org.kuali.kra.award.paymentreports.awardreports.reporting.service;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardService;
import org.kuali.kra.award.paymentreports.ReportRegenerationType;
import org.kuali.kra.award.paymentreports.ReportStatus;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTerm;
import org.kuali.kra.award.paymentreports.awardreports.reporting.ReportTracking;
import org.kuali.kra.award.paymentreports.awardreports.reporting.ReportTrackingBean;
import org.kuali.kra.service.AwardScheduleGenerationService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * 
 * This class manages the services needed for Report Tracking.
 */
public class ReportTrackingServiceImpl implements ReportTrackingService {
    
    private static final String PENDING_STATUS_DESCRIPTION = "Pending";
    
    private AwardScheduleGenerationService awardScheduleGenerationService;
    private BusinessObjectService businessObjectService;
    private AwardService awardService;
    
    @Override
    public void generateReportTrackingAndSave(Award award, boolean forceReportRegeneration) throws ParseException {
        if ((forceReportRegeneration || autoRegenerateReports(award)) && award.getPrincipalInvestigator() != null) {
            
            List<AwardReportTerm> awardReportTermItems = award.getAwardReportTermItems();
            List<ReportTracking> reportTrackingsToBeDeleted = new ArrayList<ReportTracking>();
            
            for (AwardReportTerm eachAwardTerm : awardReportTermItems) {
                
                // refresh here! NOT in buildReportTracking() method!
                eachAwardTerm.refresh();
                
                // if the report item has N for GenerateReportRequiremtns, then skip it
                eachAwardTerm.refreshReferenceObject("reportClass");
                if (!eachAwardTerm.getReportClass().getGenerateReportRequirements()) {
                    continue;
                }
                
                /**
                * creating this secondary AwardReportTerm List as we need to pass a List of AwardReportTerms to the dates generation
                * service below, and we only want to be concerned with the current item, the whole list that we are looping through.
                */
                List<java.util.Date> dates = new ArrayList<java.util.Date>();
                List<AwardReportTerm> awardReportTerms = new ArrayList<AwardReportTerm>();
                awardReportTerms.add(eachAwardTerm); 
                dates = getAwardScheduleGenerationService().generateSchedules(award, awardReportTerms, true); 
                
                //List<ReportTracking> newReportTrackings = new ArrayList<ReportTracking>();
                
                // rebuild report tracking based on Frequency base, and replace the list to eachAwardTerm
                eachAwardTerm.setReportTrackings(rebuildReportTrackingsBasedOnFrequencyBase(eachAwardTerm, reportTrackingsToBeDeleted));
                
                // generate all reports based on Award, and add them to eachAwardTerm
                eachAwardTerm.getReportTrackings().addAll(generateReportTrackings(dates, award, eachAwardTerm));

                // set the tracking reports to AwardTerm
                Collections.sort(eachAwardTerm.getReportTrackings());
                
            }
            this.getBusinessObjectService().delete(reportTrackingsToBeDeleted);
            this.getBusinessObjectService().save(awardReportTermItems);
        }
    }
    
    /**
     * 
     * This method...
     * @param dates
     * @param award
     * @param awardTerm
     * @Param reportsToSave
     */
    protected List<ReportTracking> generateReportTrackings(List<java.util.Date> dates, Award award, AwardReportTerm awardTerm) {
    	List<ReportTracking> reportTrackingReturn = new ArrayList<ReportTracking>();
    	
    	ReportTracking reportTracking = new ReportTracking();
    	// if there is no date, but there is more than one reportTracking, then report tracking should NOT be generated anymore
    	// because there should already be at least one reports without date. 
    	// 1. if there is no date
        if (dates.size() == 0) {
        	// 1.1 And, there is no report or there is no reports with empty due date
        	// then default report tracking (with empty due date) should be generated.
        	if (awardTerm.getReportTrackings().size() == 0 || !anyReportsWithEmptyDueDate(awardTerm.getReportTrackings())){
                reportTracking = buildReportTracking(award, awardTerm);
                reportTrackingReturn.add(reportTracking);
        	} 
        // 2. if there are dates, then create reports using the dates
        } else {
        	// Add a new report tracking item for each date, if that date doesn't already have a report tracking item.
            for (java.util.Date date : dates) {
                if (!isAwardTermDateAlreadySet(awardTerm.getReportTrackings(), date)) {
					reportTracking = buildReportTracking(award, awardTerm);
					java.sql.Date sqldate = new java.sql.Date(date.getTime());
					reportTracking.setDueDate(sqldate);
					reportTrackingReturn.add(reportTracking);
                }
            }
        }
        
        return reportTrackingReturn;
    }

    
    /**
     * 
     * This method builds a basic report tracking item pre-populated with Award and AwardTerm data.
     * @param award
     * @param awardTerm
     * @return
     */
    protected ReportTracking buildReportTracking(Award award, AwardReportTerm awardTerm) {
        
        ReportTracking reportTracking = new ReportTracking();
        reportTracking.setAwardNumber(award.getAwardNumber());
        reportTracking.setAwardReportTermId(awardTerm.getAwardReportTermId());
        reportTracking.setDueDate(awardTerm.getDueDate());
        reportTracking.setFrequency(awardTerm.getFrequency());
        reportTracking.setFrequencyBase(awardTerm.getFrequencyBase());
        reportTracking.setFrequencyBaseCode(awardTerm.getFrequencyBaseCode());
        reportTracking.setFrequencyCode(awardTerm.getFrequencyCode());
        reportTracking.setOspDistributionCode(awardTerm.getOspDistributionCode());
        reportTracking.setLastUpdateDate(new java.sql.Timestamp(new java.util.Date().getTime()));
        reportTracking.setLastUpdateUser(GlobalVariables.getUserSession().getPerson().getName());
        reportTracking.setLeadUnit(award.getLeadUnit());
        reportTracking.setLeadUnitNumber(award.getLeadUnitNumber());
        reportTracking.setOverdue(0);
        reportTracking.setPiName(award.getPrincipalInvestigatorName());
        if (award.getPrincipalInvestigator() != null) {
            reportTracking.setPiPersonId(award.getPrincipalInvestigator().getPersonId());
            reportTracking.setPiRolodexId(award.getPrincipalInvestigator().getRolodexId());
        }
        reportTracking.setReport(awardTerm.getReport());
        reportTracking.setReportClass(awardTerm.getReportClass());
        reportTracking.setReportClassCode(awardTerm.getReportClassCode());
        reportTracking.setReportCode(awardTerm.getReportCode());
        ReportStatus pending = getPendingReportStatus();
        reportTracking.setReportStatus(pending);
        reportTracking.setStatusCode(pending.getReportStatusCode());
        
        reportTracking.setSponsor(award.getSponsor());
        reportTracking.setSponsorAwardNumber(award.getSponsorAwardNumber());
        reportTracking.setSponsorCode(award.getSponsorCode());
        reportTracking.setTitle(award.getTitle());
        reportTracking.setBaseDate(calculateBaseDate(awardTerm));
        return reportTracking;
    }
    
    /**
     * 
     * This method calculates the the frequency base date based on the award term's selected base code.
     * It is a duplication of the logic on awardReportClasses.tag.
     * If this function requires updating, you'll need to update the javascript in the tag file.
     * @param awardTerm
     * @return
     */
    protected Date calculateBaseDate(AwardReportTerm awardTerm) { 
        Date returnDate = null;
        if (awardTerm != null && awardTerm.getFrequencyBaseCode() != null) {
            if (StringUtils.equalsIgnoreCase(awardTerm.getFrequencyBaseCode(), "1")) {
                returnDate = awardTerm.getAward().getAwardExecutionDate();
            } else if (StringUtils.equalsIgnoreCase(awardTerm.getFrequencyBaseCode(), "2")) {
                returnDate = awardTerm.getAward().getAwardEffectiveDate();
            } else if (StringUtils.equalsIgnoreCase(awardTerm.getFrequencyBaseCode(), "3")) {
                returnDate = awardTerm.getAward().getLastAwardAmountInfo().getObligationExpirationDate();
            } else if (StringUtils.equalsIgnoreCase(awardTerm.getFrequencyBaseCode(), "4")) {
                returnDate = awardTerm.getAward().getLastAwardAmountInfo().getFinalExpirationDate();
            } else if (StringUtils.equalsIgnoreCase(awardTerm.getFrequencyBaseCode(), "5")) {
                returnDate = awardTerm.getAward().getLastAwardAmountInfo().getCurrentFundEffectiveDate();
            }
        }
        return returnDate;
    }
    
    /**
     * 
     * This method...
     * @return
     * @unsupported
     */
    protected ReportStatus getPendingReportStatus() {
        Map params = new HashMap();
        params.put("DESCRIPTION", PENDING_STATUS_DESCRIPTION);
        ReportStatus rs = (ReportStatus) this.getBusinessObjectService().findByPrimaryKey(ReportStatus.class, params);
        return rs;
    }
    
    /**
     * 
     * This method purges (puts them in the deleteReportsList) and report tracking items that are pending, and come from award term
     * that has a frequency base that allows for regeneration.  Any ReportTracking items not purges are in the NEW returning list.
     * @param awardTerm
     * @param reportListToClean
     * @param deleteReports
     * @return
     */
    private List<ReportTracking> rebuildReportTrackingsBasedOnFrequencyBase(AwardReportTerm eachAwardTerm, List<ReportTracking> reportTrackingsToBeDeleted) {
        List<ReportTracking> reportTrackingReturn = new ArrayList<ReportTracking>();
        String reportStatusCode = getPendingReportStatus().getReportStatusCode();
        for (ReportTracking reportTracking : eachAwardTerm.getReportTrackings()) {
        	String regenerationTypeDesc = ObjectUtils.isNull(eachAwardTerm.getFrequencyBase()) 
        			? "" : eachAwardTerm.getFrequencyBase().getReportRegenerationType().getDescription();
        	
            if (StringUtils.equals(reportStatusCode, reportTracking.getStatusCode())  && StringUtils.equals(regenerationTypeDesc, ReportRegenerationType.REGEN.getDescription())) {
            	// existing reports should be deleted when it has REGEN for frequency base 
            	reportTrackingsToBeDeleted.add(reportTracking);
            } else {
            	// reports with ONLY ADD NEW should NOT be deleted 
            	reportTrackingReturn.add(reportTracking);
            }
        }
        return reportTrackingReturn;
    }
    
    private boolean isAwardTermDateAlreadySet(List<ReportTracking> reportTrackings, java.util.Date date) {
        boolean retVal = false;
        if (date == null && reportTrackings.size() > 0) {
            retVal =  true;
        }
        if (!retVal) {
            for (ReportTracking rt : reportTrackings) {
                if (rt.getDueDate() != null && rt.getDueDate().getTime() == date.getTime()) {
                    retVal =  true;
                }
            }
        }
        return retVal;
    }
    
    private boolean anyReportsWithEmptyDueDate(List<ReportTracking> reportTrackings) {
        boolean retVal = false;
		for (ReportTracking rt : reportTrackings) {
			if (ObjectUtils.isNull(rt.getDueDate())){
				retVal = true;
			}
		}
        return retVal;
    }

    
    @Override
    public List<ReportTracking> getReportTacking(AwardReportTerm awardTerm) {
        List<ReportTracking> reportTrackings = new ArrayList<ReportTracking>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("awardReportTermId", awardTerm.getAwardReportTermId());
        Collection<ReportTracking> reportTrackingCollection = this.getBusinessObjectService().findMatching(ReportTracking.class, params);
        //if there are none, check to make sure this isn't due to award versioning and the id changing
        if (reportTrackingCollection != null && !reportTrackingCollection.isEmpty()) {
            reportTrackings.addAll(reportTrackingCollection);    
        } else {
            params.clear();
            params.put("awardNumber", awardTerm.getAwardNumber());
            params.put("reportClassCode", awardTerm.getReportClassCode());
            params.put("reportCode", awardTerm.getReportCode());
            params.put("frequencyCode", awardTerm.getFrequencyCode());
            params.put("frequencyBaseCode", awardTerm.getFrequencyBaseCode());
            params.put("ospDistributionCode", awardTerm.getOspDistributionCode());
            reportTrackingCollection = this.getBusinessObjectService().findMatching(ReportTracking.class, params);
            for (ReportTracking reportTrack : reportTrackingCollection) {
                reportTrack.setAwardReportTermId(awardTerm.getAwardReportTermId());
            }
            reportTrackings.addAll(reportTrackingCollection);
        }
        Collections.sort(reportTrackings);
        return reportTrackings;
    }
    
    @Override
    public List<ReportTracking> getReportTacking(Award award) {
        List<ReportTracking> reportTrackings = new ArrayList<ReportTracking>();
        for (AwardReportTerm reportTerm : award.getAwardReportTermItems()){
        	if (!CollectionUtils.isEmpty(reportTerm.getReportTrackings())){
        		reportTrackings.addAll(reportTerm.getReportTrackings());
        	}
        }
        return reportTrackings;
    }
    
    @Override
    public boolean autoRegenerateReports(Award award) {
        // ### Vivantech Fix : #151 / [#90223952] added char to the end of award number
        String rootAwardNumberEnder = "-00001A";
        boolean retVal = StringUtils.endsWith(award.getAwardNumber(), rootAwardNumberEnder);
        if (!retVal) {
            for (AwardReportTerm term : award.getAwardReportTermItems()) {
                List<ReportTracking> tracking = getReportTacking(term);
                if (!tracking.isEmpty()) {
                    return true;
                }
            }
        }
        return retVal;
    }

    public AwardScheduleGenerationService getAwardScheduleGenerationService() {
        return awardScheduleGenerationService;
    }

    public void setAwardScheduleGenerationService(AwardScheduleGenerationService awardScheduleGenerationService) {
        this.awardScheduleGenerationService = awardScheduleGenerationService;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public AwardService getAwardService() {
        return awardService;
    }

    public void setAwardService(AwardService awardService) {
        this.awardService = awardService;
    }

    @Override
    public void setReportTrackingListSelected(List<ReportTracking> reportTrackingListing, boolean selectedValue) {
        for (ReportTracking rt : reportTrackingListing) {
            rt.setMultiEditSelected(selectedValue);
        }
    }

    @Override
    public void updateMultipleReportTrackingRecords(List<ReportTracking> reportTrackingListing,
            ReportTrackingBean reportTrackingBean) {
        for (ReportTracking rt : reportTrackingListing) {
            if (rt.getMultiEditSelected()) {
                if (StringUtils.isNotBlank(reportTrackingBean.getComments())) {
                    rt.setComments(reportTrackingBean.getComments());
                }
                if (StringUtils.isNotBlank(reportTrackingBean.getPreparerId())) {
                    rt.setPreparerId(reportTrackingBean.getPreparerId());
                    rt.setPreparerName(reportTrackingBean.getPreparerName());
                }
                if (reportTrackingBean.getActivityDate() != null) {
                    rt.setActivityDate(reportTrackingBean.getActivityDate());
                }
                if (StringUtils.isNotBlank(reportTrackingBean.getStatusCode())) {
                    rt.setStatusCode(reportTrackingBean.getStatusCode());
                    rt.setReportStatus(getReportStatus(reportTrackingBean.getStatusCode()));
                }
            }
        }
    }
    
    protected ReportStatus getReportStatus(String statusCode) {
        Map params = new HashMap();
        params.put("REPORT_STATUS_CODE", statusCode);
        ReportStatus rs = (ReportStatus) this.getBusinessObjectService().findByPrimaryKey(ReportStatus.class, params);
        return rs;
    }

    @Override
    public boolean shouldAlertReportTrackingDetailChange(Award award) {
        boolean retVal = false;
        
        if (award.getAwardId() != null) {
            Award dbAward = this.getAwardService().getAward(award.getAwardId());
            if (dbAward != null) {
                List<ReportTracking> dbReportTrackings = this.getReportTacking(dbAward);
                if (dbReportTrackings != null && !dbReportTrackings.isEmpty()) {
                    retVal = !dateCompare(award.getAwardExecutionDate(), dbAward.getAwardExecutionDate()) 
                        || !dateCompare(award.getAwardEffectiveDate(), dbAward.getAwardEffectiveDate())
                        || !dateCompare(award.getLastAwardAmountInfo().getObligationExpirationDate(), 
                                dbAward.getLastAwardAmountInfo().getObligationExpirationDate())
                        || !dateCompare(award.getLastAwardAmountInfo().getFinalExpirationDate(), dbAward.getLastAwardAmountInfo().getFinalExpirationDate())
                        || !dateCompare(award.getLastAwardAmountInfo().getCurrentFundEffectiveDate(), 
                                dbAward.getLastAwardAmountInfo().getCurrentFundEffectiveDate());
                }
            }
}
        return retVal;
    }
    
    /**
     * 
     * This method returns true if the two are the same, returns false otherwise.
     * @param formDate
     * @param dbDate
     * @return
     */
    private boolean dateCompare(Date formDate, Date dbDate) {
        boolean retVal = false;
        if (formDate == null && dbDate == null) {
            retVal = true;
        } else {
            if (formDate != null && dbDate != null && formDate.equals(dbDate)) {
                retVal = true;
            }
        }
        return retVal;
    }
}