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

<%@ attribute name="attributes" required="true" type="java.util.Map" %>
<%@ attribute name="action" required="true" %>

<kra:permission value="${KualiForm.actionHelper.canDeleteProtocolAmendRenew}">

<kul:innerTab tabTitle="Delete Protocol, Amendment, or Renewal" parentTab="" defaultOpen="false" tabErrorKey="actionHelper.protocolWithdrawBean*">
    <div class="innerTab-container" align="left">
        <table class="tab" cellpadding="0" cellspacing="0" summary=""> 
            <tbody>
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                            <kul:htmlAttributeLabel attributeEntry="${attributes.reason}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <nobr>
                        <kul:htmlControlAttribute property="actionHelper.protocolDeleteBean.reason" attributeEntry="${attributes.reason}" />
                        </nobr>
                    </td>
                </tr>
                <tr>
					<td align="center" colspan="2">
						<div align="center">
							<html:image property="methodToCall.deleteProtocol.anchor${tabKey}"
							            src='${ConfigProperties.kra.externalizable.images.url}tinybutton-submit.gif' styleClass="tinybutton"/>
						</div>
	                </td>
                </tr>
            </tbody>
        </table>
    </div>
    
</kul:innerTab>

</kra:permission>
