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
package org.kuali.kra.common.committee.meeting;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.common.committee.bo.CommitteeScheduleBase;
import org.kuali.kra.common.committee.document.CommitteeDocumentBase;
import org.kuali.kra.common.committee.meeting.MeetingEventBase.ErrorType;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.protocol.actions.reviewcomments.ReviewCommentsService;
import org.kuali.kra.protocol.actions.submit.ProtocolSubmissionBase;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.WebUtils;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * 
 * This class is for all meeting actions. A couple of methods, which are for text area update, are copied from
 * KraTransactionalDocumentActionBase.
 */

/**
 * ### Vivantech Fix : #96 / [#88346510] Extending execute() for MeetingFormBase to make use of KualiDocumentFormBase to use formKey 
 *
 */
public abstract class MeetingActionBase extends KualiAction {
    private static final String CLOSE_QUESTION = "Would you like to save meeting data before close it ?";

    private static final String CLOSE_QUESTION_ID = "meeting.close.question";
    private static final String LINE_NUMBER = "lineNum";
    private static final String REFRESH_CALLER = "refreshCaller";
    private static final String COMMITTEE_SCHEDULE_ERROR_PATH = "meetingHelper.committeeSchedule";

    /**
     * 
     * This method is for the initial load of meeting page. It is called when 'maintain' button of committee schedule is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("id", request.getParameter("scheduleId"));
        List<CommitteeScheduleMinuteBase<?,?>> permittedMinutes = new ArrayList<CommitteeScheduleMinuteBase<?,?>>();
        CommitteeScheduleBase commSchedule = (CommitteeScheduleBase) getBusinessObjectService().findByPrimaryKey(getCommitteeScheduleBOClass(), fieldValues);
        List<CommitteeScheduleMinuteBase> minutes = commSchedule.getCommitteeScheduleMinutes();
        
        // use the entry type comparator to sort the minutes 
        Collections.sort(minutes, CommitteeScheduleMinuteBase.entryTypeComparator);
        
        for (CommitteeScheduleMinuteBase minute : minutes) {
            if (getReviewerCommentsService().getReviewerCommentsView(minute)) {
                permittedMinutes.add(minute);
            }  
        }
        commSchedule.setCommitteeScheduleMinutes(permittedMinutes);
        ((MeetingFormBase) form).setReadOnly("true".equals(request.getParameter("readOnly")));
        ((MeetingFormBase) form).getMeetingHelper().setCommitteeSchedule(commSchedule);
        if ( !((MeetingFormBase) form).getMeetingHelper().hasViewModifySchedulePermission() ) {
            // same exception as of checkauthorization of kualiaction
            throw new AuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), "start", this
                    .getClass().getSimpleName());
        }

        getMeetingService().populateFormHelper(((MeetingFormBase) form).getMeetingHelper(), commSchedule,
                getScheduleLineNumber(request, commSchedule));
        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    protected abstract Class<? extends CommitteeScheduleBase> getCommitteeScheduleBOClass();


    /*
     * This is a utility method to figure out the order of the selected schedule in schedule collections. This is primarily for
     * creating meeting tab title.
     */
    private int getScheduleLineNumber(HttpServletRequest request, CommitteeScheduleBase<?, ?, ?, ?> commSchedule) {
        int lineNumber = 0;
        if (StringUtils.isNotBlank(request.getParameter(LINE_NUMBER))) {
            lineNumber = Integer.parseInt(request.getParameter(LINE_NUMBER));
        }
        else {
            for (CommitteeScheduleBase<?, ?, ?, ?> schedule : commSchedule.getParentCommittee().getCommitteeSchedules()) {
                lineNumber++;
                if (schedule.getId().equals(commSchedule.getId())) {
                    break;
                }
            }
        }
        return lineNumber;
    }

    

    /**
     * 
     * This method is called when 'meeting detail' tab is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward management(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("management");
    }

    /**
     * 
     * This method is to save committee schedule when the 'save' button on meeting detail page is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        CommitteeScheduleBase committeeSchedule = ((MeetingFormBase) form).getMeetingHelper().getCommitteeSchedule();
        if (isValidToSave(((MeetingFormBase) form).getMeetingHelper())) {
            ((MeetingFormBase) form).getMeetingHelper().populateAttendancePreSave();
            getMeetingService().saveMeetingDetails(committeeSchedule, ((MeetingFormBase) form).getMeetingHelper().getDeletedBos());
            ((MeetingFormBase) form).getMeetingHelper().initDeletedList();
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    /*
     * validate required/format of the properties of bo. also validate business rules.
     */
    private boolean isValidToSave(MeetingHelperBase meetingHelper) {

        GlobalVariables.getMessageMap().addToErrorPath(COMMITTEE_SCHEDULE_ERROR_PATH);
        getDictionaryValidationService().validateBusinessObject(meetingHelper.getCommitteeSchedule());
        GlobalVariables.getMessageMap().removeFromErrorPath(COMMITTEE_SCHEDULE_ERROR_PATH);
        boolean valid = GlobalVariables.getMessageMap().hasNoErrors();
        valid &= applyRules(new MeetingSaveEvent(Constants.EMPTY_STRING, getCommitteeDocument(meetingHelper.getCommitteeSchedule()
                .getParentCommittee().getCommitteeDocument().getDocumentHeader().getDocumentNumber()), meetingHelper, ErrorType.HARDERROR));
        return valid;

    }

    /*
     * This method is to get committeedocument to apply tule. The committeedocument from committeeschedule is null for
     * 'documentheader.workflowdocument' it caused problem.
     */
    protected CommitteeDocumentBase getCommitteeDocument(String documentNumber) {
        try {
            return (CommitteeDocumentBase) KraServiceLocator.getService(DocumentService.class).getByDocumentHeaderId(documentNumber);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * This method is called when 'Meeting Action' Tab is clicked. It is forwarded to 'meetingAction'
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward actions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("actions");
    }

    /**
     * 
     * This method is for the 'view' button of protocol submitted. It will be forwarded to protocol action page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private ActionForward viewProtocolSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolSubmissionBase protocolSubmission = ((MeetingFormBase) form).getMeetingHelper().getCommitteeSchedule()
                .getLatestProtocolSubmissions().get(Integer.parseInt(request.getParameter("line")));
        
// TODO *********commented the code below during IACUC refactoring*********         
//        response.sendRedirect("iacucProtocolActions.do?methodToCall=start&submissionId=" + protocolSubmission.getSubmissionId());
        
        response.sendRedirect(getActionIdHook() + ".do?methodToCall=start&submissionId=" + protocolSubmission.getSubmissionId());
        return null;
    }

    protected abstract String getActionIdHook();


    protected BusinessObjectService getBusinessObjectService() {
        return KraServiceLocator.getService(BusinessObjectService.class);
    }

    /**
     * 
     * This method is for 'close' button. Confirmation of 'save' is performed.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward close(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        MeetingFormBase meetingForm = (MeetingFormBase) form;
        Object question = request.getParameter(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME);
        if (question == null && meetingForm.getMeetingHelper().canModifySchedule()) {
            return performQuestionWithoutInput(mapping, form, request, response, CLOSE_QUESTION_ID, CLOSE_QUESTION,
                    KRADConstants.CONFIRMATION_QUESTION, ((MeetingFormBase) form).getMethodToCall(), "");
        }
        else if (meetingForm.getMeetingHelper().canModifySchedule()) {
            Object buttonClicked = request.getParameter(KRADConstants.QUESTION_CLICKED_BUTTON);
            if ((CLOSE_QUESTION_ID.equals(question)) && ConfirmationQuestion.YES.equals(buttonClicked)) {
                CommitteeScheduleBase committeeSchedule = meetingForm.getMeetingHelper().getCommitteeSchedule();
                if (isValidToSave(((MeetingFormBase) form).getMeetingHelper())) {
                    ((MeetingFormBase) form).getMeetingHelper().populateAttendancePreSave();
                    getMeetingService().saveMeetingDetails(committeeSchedule,
                            ((MeetingFormBase) form).getMeetingHelper().getDeletedBos());
                    ((MeetingFormBase) form).getMeetingHelper().initDeletedList();
                }
                else {
                    return mapping.findForward(Constants.MAPPING_BASIC);

                }
            }
        }

        return mapping.findForward(KRADConstants.MAPPING_PORTAL);
    }

    /**
     * 
     * This method is for cancel button
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward(KRADConstants.MAPPING_PORTAL);
    }

    /**
     * override method to handle person/rolodex lookup return.
     * 
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#refresh(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (StringUtils.isNotBlank(request.getParameter(REFRESH_CALLER))) {
            if ("nonOrganizationalRolodexLookupable".equals(request.getParameter(REFRESH_CALLER))) {
                ((MeetingFormBase) form).getMeetingHelper().getNewOtherPresentBean().getAttendance().setNonEmployeeFlag(true);
            }
            else {
                ((MeetingFormBase) form).getMeetingHelper().getNewOtherPresentBean().getAttendance().setNonEmployeeFlag(false);
            }
        }
        return super.refresh(mapping, form, request, response);

    }

    /**
     * primarily to sort attendance for every action.
     * 
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);
        
        // ### Vivantech Fix : #96 / [#88346510] Extending MeetingFormBase to make use of KualiDocumentFormBase to use formKey
        MeetingFormBase formBase = ((MeetingFormBase) form);
        MeetingHelperBase meetingHelper = formBase.getMeetingHelper();
        
        meetingHelper.sortAttendances();
        // if view protocol is using popup, then need following code
        String command = request.getParameter("command");
        if (StringUtils.isNotBlank(command) && "viewProtocolSubmission".equals(command)) {
            forward = viewProtocolSubmission(mapping, form, request, response);
        }
        
        meetingHelper.setHideReviewerName(
                getReviewerCommentsService().setHideReviewerName(
                		meetingHelper.getCommitteeSchedule().getCommitteeScheduleMinutes()));
        
        // use the entry type comparator to sort the minutes 
        Collections.sort(meetingHelper.getCommitteeSchedule().getCommitteeScheduleMinutes(), CommitteeScheduleMinuteBase.entryTypeComparator);
               
        //
        String formKey = formBase.getFormKey();
        if (StringUtils.isBlank(formKey)) {
            // generate doc form key here if it does not exist
            formKey = GlobalVariables.getUserSession().addObjectWithGeneratedKey(form);
            formBase.setFormKey(formKey);
        }        

        return forward;
    }

    protected DictionaryValidationService getDictionaryValidationService() {
        return KNSServiceLocator.getKNSDictionaryValidationService();
    }

    /**
     * 
     * This method is to reload the meeting page. Reload page when it is 'readOnly' and moving from page to page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward reload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    protected boolean applyRules(KualiDocumentEvent event) {
        return KraServiceLocator.getService(KualiRuleService.class).applyRules(event);
    }
    
    protected abstract CommonMeetingService getMeetingService();
    protected abstract ReviewCommentsService<?> getReviewerCommentsService();

}
