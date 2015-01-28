<%--
 Copyright 2005-2014 The Kuali Foundation

 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.osedu.org/licenses/ECL-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="institutionalProposalIntellectualPropertyReview"
	documentTypeName="InstitutionalProposalDocument"
	renderMultipart="false"
	showTabButtons="true"
	auditCount="0"
  	headerDispatch="${KualiForm.headerDispatch}"
  	headerTabActive="intellectualPropertyReview">
  	
  	<div align="right"><kul:help documentTypeName="InstitutionalProposalDocument" pageName="Intellectual Property Review" /></div>

<kra-ip:institutionalProposalReviewData />
<kra-ip:institutionalProposalActivities />

<kul:panelFooter />	

<div id="globalbuttons" class="globalbuttons">
	<!-- ### Vivantech Fix : #42 / [#86805502] Cannot verify that the methodToCall should be methodToCall.save -->
	<html:submit value="Save" property="methodToCall.save" style="display:none;"/>
	<!-- ### Vivantech Fix : #42 / [#86805502] Cannot verify that the methodToCall should be methodToCall.save -->
	<html:submit value="Edit IP Review" property="methodToCall.editIntellectualPropertyReview" styleClass="btn btn-primary btn-xs" alt="Edit IP Review"
		onclick="javascript: openNewWindow('institutionalProposalIntellectualPropertyReview','editIntellectualPropertyReview','','${KualiForm.formKey}','${KualiForm.document.sessionDocument}');return false"/>
	<html:submit value="Reload" property="methodToCall.reload" styleClass="btn btn-primary btn-xs" alt="Reload"
		onclick="excludeSubmitRestriction=true"/>
    <c:if test="${!empty KualiForm.documentActions[Constants.KUALI_ACTION_CAN_CLOSE]}">
		<html:submit value="Close" property="methodToCall.close" styleClass="btn btn-primary btn-xs" alt="Close"/>
    </c:if>
</div>
                
<script language="javascript" src="scripts/kuali_application.js"></script>

</kul:documentPage>