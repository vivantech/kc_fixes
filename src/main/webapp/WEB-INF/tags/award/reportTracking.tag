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

<%@ attribute name="innerTabParent" description="Inner Tab Parent Name" required="true"%>
<%@ attribute name="index" description="Index" required="true"%>

<script type='text/javascript' src='dwr/interface/KraPersonService.js'></script>

<c:set var="reportTrackingAttributes" value="${DataDictionary.ReportTracking.attributes}" />
<c:set var="reportTrackingBeanAttributes" value="${DataDictionary.ReportTrackingBean.attributes}" />

<c:set var="reportTrackingPermission" value="${KualiForm.permissionsHelper.maintainAwardReportTracking}" scope="request"/>
<c:set var="reportTrackingReadOnly" value="${!reportTrackingPermission || readOnly}" scope="request"/>


<kul:innerTab parentTab="${innerTabParent}" defaultOpen="false"
	tabTitle="Details - Report Tracking"
	useCurrentTabIndexAsKey="true"
	tabErrorKey="document.awardList[0].awardReportTermItems[${index}]*,reportTrackingBeans[${index}]*,document.award.awardReportTermItems[${index}]*, document.awardList[0].awardReportTermItems[${index}].frequencyBaseCode">

	<kra:softError softErrorKey="awardReportTerms-${KualiForm.document.award.awardReportTermItems[index].reportClassCode}-${KualiForm.document.award.awardReportTermItems[index].frequencyCode}-${KualiForm.document.award.awardReportTermItems[index].frequencyBaseCode}-${KualiForm.document.award.awardReportTermItems[index].ospDistributionCode}" />
	<table cellpadding="0" cellspacing="0" summary="">
		<c:if test="${!reportTrackingReadOnly}">
		<tr>
			<th colspan="3"><div align="center">Edit Selected:</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingBeanAttributes.preparerId}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingBeanAttributes.statusCode}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingBeanAttributes.activityDate}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingBeanAttributes.comments}" noColon="true" />
			</div></th>
				<th colspan="2"> <div align="center">Action</div></th>	
		</tr>
		<tr>
			<th colspan="3">
				<div align="center">
					<html:image property="methodToCall.selectAllMultEdit.AwardReportTermItemsIndex${index}"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' 
										alt="Select All" onclick="" styleClass="tinybutton"/>
					:
					<html:image property="methodToCall.selectNoneMultiEdit.AwardReportTermItemsIndex${index}"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' 
										alt="Deselect All" onclick="" styleClass="tinybutton"/>
				</div>
			</th>
			<td>
			
				<kul:htmlControlAttribute property="reportTrackingBeans[${index}].preparerName" readOnly="${reportTrackingReadOnly}" 
				onblur="loadContactPersonName('reportTrackingBeans[${index}].preparerName',
										'reportTrackingBeans.fullName',
										'na',
										'na',
										'na',
										'sub.reportTrackingBeans.div');"
				attributeEntry="${reportTrackingBeanAttributes.preparerName}"/>
				
				<c:if test="${!reportTrackingReadOnly}">	
	            	<kul:lookup boClassName="org.kuali.kra.bo.KcPerson" 
	                                fieldConversions="personId:reportTrackingBeans[${index}].preparerId,userName:reportTrackingBeans[${index}].preparerName" />
                </c:if>	
				<kul:htmlControlAttribute property="reportTrackingBeans[${index}].preparerId" readOnly="${reportTrackingReadOnly }"
						attributeEntry="${reportTrackingBeanAttributes.preparerId}"   />
                <kul:directInquiry boClassName="org.kuali.kra.bo.KcPerson" inquiryParameters="reportTrackingBeans[${index}].preparerId:personId" /> 
                <br/>	
                	<span id="reportTrackingBeans.fullName"> <c:out value="${reportTrackingBeans[$index].preparerFullname}"/>&nbsp;</span>  
                <html:hidden styleId ="sub.reportTrackingBeans.div" property="reportTrackingBeans[${index}].preparerId" />
			</td>
			<td>
				<kul:htmlControlAttribute property="reportTrackingBeans[${index}].statusCode" 
							attributeEntry="${reportTrackingBeanAttributes.statusCode}" readOnly="${reportTrackingReadOnly }"  />
			</td>
			<td>
				<kul:htmlControlAttribute property="reportTrackingBeans[${index}].activityDate" 
							attributeEntry="${reportTrackingBeanAttributes.activityDate}" readOnly="${reportTrackingReadOnly }"  />
			</td>
			<td>
				<kul:htmlControlAttribute property="reportTrackingBeans[${index}].comments" 
							attributeEntry="${reportTrackingBeanAttributes.comments}" readOnly="${reportTrackingReadOnly }"  />
			</td>
				<td colspan="2">
				<div align="center">
				<html:image property="methodToCall.updateMultileReportTracking.AwardReportTermItemsIndex${index}"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-update.gif' 
										alt="Update Multiple Report Tracking" onclick="" styleClass="tinybutton"/>
				</div>
			</td>
		</tr>
		</c:if>
		<tr>
			<th><div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.multiEditSelected}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.dueDate}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.overdue}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.preparerId}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.statusCode}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.activityDate}" noColon="true" />
			</div></th>
			<th> <div align="center">
				<kul:htmlAttributeLabel attributeEntry="${reportTrackingAttributes.comments}" noColon="true" />
			</div></th>
			<th> <div align="center">
				Last Update
			</div></th>
			<th> <div align="center">Action</div></th>
		</tr>
		<c:forEach var="reportTracking" items="${KualiForm.document.award.awardReportTermItems[index].reportTrackings}" varStatus="status">
			<tr>
				<td>
					<c:if test="${!reportTrackingReadOnly}">
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].multiEditSelected" 
							attributeEntry="${reportTrackingAttributes.multiEditSelected}" readOnly="${reportTrackingReadOnly }"  />
					</c:if>
				</td>
				<td>
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].dueDate" 
						attributeEntry="${reportTrackingAttributes.dueDate}" readOnly="${reportTrackingReadOnly}"  />
				</td>
				<td>
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].overdue" 
						attributeEntry="${reportTrackingAttributes.overdue}" readOnly="${true }"  />
				</td>
				<td>
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerName" 
					onblur="loadContactPersonName('document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerName',
										'preparer[${status.index}]',
										'na',
										'na',
										'na',
										'sub.reportTrackingBeans.div');"	attributeEntry="${reportTrackingAttributes.preparerName}" readOnly="${reportTrackingReadOnly }"  />
					<c:if test="${!reportTrackingReadOnly}">
	                	<kul:lookup boClassName="org.kuali.kra.bo.KcPerson" 
	                                fieldConversions="userName:document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerName,personId:document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerId" />
                   </c:if>	
                    <kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerId" 
						attributeEntry="${reportTrackingAttributes.preparerId}" readOnly="${reportTrackingReadOnly }"  /> 	
                    <kul:directInquiry boClassName="org.kuali.kra.bo.KcPerson" inquiryParameters="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerId:personId" />
                    <html:hidden styleId ="sub.reportTrackingBeans.div" property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].preparerId" />
					<br/>
					<span id="preparer[${status.index}]">
                    &nbsp;
					</span>
				</td>
				<td>
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].statusCode" 
						attributeEntry="${reportTrackingAttributes.statusCode}" readOnly="${reportTrackingReadOnly }"  />
				</td>
				<td>
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].activityDate" 
						attributeEntry="${reportTrackingAttributes.activityDate}" readOnly="${reportTrackingReadOnly }"  />
				</td>
				<td>
					<kul:htmlControlAttribute property="document.award.awardReportTermItems[${index}].reportTrackings[${status.index}].comments" 
						attributeEntry="${reportTrackingAttributes.comments}" readOnly="${reportTrackingReadOnly }"  />
				</td>
				<td>
					${reportTracking.lastUpdateUser } : 
					<fmt:formatDate value="${reportTracking.lastUpdateDate}" pattern="MM/dd/yyyy HH:mm:ss"/>
					
				</td>
				<td>
					<c:if test="${reportTracking.displayDeleteButton && !reportTrackingReadOnly}">										       
					
						<html:image property="methodToCall.deleteReportTrackingRecord.awardReportTermItems${index}.line${status.index}.anchor${currentTabIndex}"
				        src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"
				        onclick="return confirm('Are you sure you want to delete this report tracking detail?  Note, this record may be regenerated on the next save, based on the selected frequency and frequency base.')"/>
			        </c:if>
				</td>
			</tr>			
		</c:forEach>
	</table>

</kul:innerTab>
