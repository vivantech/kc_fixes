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
package org.kuali.kra.subaward.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.bo.versioning.VersionHistory;
import org.kuali.kra.bo.versioning.VersionStatus;
import org.kuali.kra.lookup.KraLookupableHelperServiceImpl;
import org.kuali.kra.service.ServiceHelper;
import org.kuali.kra.service.VersionHistoryService;
import org.kuali.kra.subaward.bo.SubAward;
import org.kuali.kra.subaward.bo.SubAwardFundingSource;
import org.kuali.kra.subaward.document.SubAwardDocument;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.*;

/**
 * This class is for SubAwardLookupableHelperServiceImpl
 * for lookup searches...
 */
public class SubAwardLookupableHelperServiceImpl extends KraLookupableHelperServiceImpl {

    private static final String AWARD_NUMBER = "awardNumber";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String REQUISITIONER_USER_NAME="requisitionerUserName";
    private VersionHistoryService versionHistoryService; 
    
    @Override
    public List<? extends BusinessObject>
    getSearchResults(Map<String, String> fieldValues) {
        super.setBackLocationDocFormKey(fieldValues);
        String awardNumber = fieldValues.get(AWARD_NUMBER);
        String requisitionerUserName = fieldValues.get(REQUISITIONER_USER_NAME);
        String subrecipientName = fieldValues.get(ORGANIZATION_NAME);
        fieldValues.remove(AWARD_NUMBER);
        fieldValues.remove(ORGANIZATION_NAME);
        fieldValues.remove(REQUISITIONER_USER_NAME);
        List<SubAward> unboundedResults =
        (List<SubAward>) super.getSearchResultsUnbounded(fieldValues);
        List<SubAward> returnResults = new ArrayList<SubAward>();
        try {
            returnResults = filterForActiveSubAwards(unboundedResults, awardNumber, subrecipientName, requisitionerUserName, fieldValues.get("statusCode"));
        } catch (WorkflowException e) {
            throw new RuntimeException(e);
        }

        return returnResults;
    }



    /**.
     * this method for getCustomActionUrls
     * @param businessObject
     * @param pkNames
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<HtmlData> getCustomActionUrls(
    BusinessObject businessObject, List pkNames) {
        List<HtmlData> htmlDataList =
        super.getCustomActionUrls(businessObject, pkNames);
        htmlDataList.add(getOpenLink((SubAward) businessObject, false));
        htmlDataList.add(getMedusaLink((SubAward) businessObject, false));
        return htmlDataList;
    }
   /**.
    * This method is for getOpenLink
     * @param subaward
     * @param viewOnly
     * @return htmlData
     */
    protected AnchorHtmlData getOpenLink(SubAward subAward, Boolean viewOnly) {
        SubAwardDocument subAwardDocument = subAward.getSubAwardDocument();
        AnchorHtmlData htmlData = new AnchorHtmlData();
        htmlData.setDisplayText("open");
        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER,
        KRADConstants.DOC_HANDLER_METHOD);
        parameters.put(KRADConstants.PARAMETER_COMMAND,
        KewApiConstants.DOCSEARCH_COMMAND);
        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, getDocumentTypeName());
        parameters.put("viewDocument", viewOnly.toString());
        parameters.put("docOpenedFromAwardSearch", "true");
        parameters.put("docId", subAwardDocument.getDocumentNumber());
        parameters.put("placeHolderAwardId",
        subAward.getSubAwardId().toString());
        String href = UrlFactory.parameterizeUrl(
        "../" + getHtmlAction(), parameters);
        htmlData.setHref(href);
        return htmlData;
    }
    /**.
     * This Method is for getMedusaLink
     * @param subaward
     * @param readOnly
     * @return htmlData
     */
    protected AnchorHtmlData getMedusaLink(
    SubAward subAward, Boolean readOnly) {
        AnchorHtmlData htmlData = new AnchorHtmlData();
        htmlData.setDisplayText(MEDUSA);
        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, "medusa");
        parameters.put(KRADConstants.PARAMETER_COMMAND,
        KewApiConstants.DOCSEARCH_COMMAND);
        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, getDocumentTypeName());
        parameters.put("viewDocument", readOnly.toString());
        parameters.put("docId", subAward.
        getSubAwardDocument().getDocumentNumber());
        parameters.put("docOpenedFromAwardSearch", "true");
        parameters.put("placeHolderAwardId",
        subAward.getSubAwardId().toString());
        String href  = UrlFactory.parameterizeUrl(
         "../" + getHtmlAction(), parameters);
        htmlData.setHref(href);
        return htmlData;
    }

      /**.
       * This override is reset field definitions
       * @see org.kuali.core.lookup.AbstractLookupableHelperServiceImpl#getRows()
       */
      @Override
    public List<Row> getRows() {
        List<Row> rows = super.getRows();
        for (Row row : rows) {
            for (Field field : row.getFields()) {

                if (field.getPropertyName().equals("startDate") || field.getPropertyName().equals("endDate")
                        || field.getPropertyName().equals("closeoutDate")) {
                    field.setDatePicker(true);
                }
                if (field.getPropertyName().equals("requisitionerUserName")) {
                	// ### Vivantech Fix : #23 / [#83059512] Fixing incident reports from Person lookup
                    // field.setFieldConversions("principalName:requisitionerUserName,principalId:requisitionerId");
                	field.setFieldConversions("userName:person.userName,personId:personId");
                }
            }
        }
        return rows;
    }
      @Override
      protected void addEditHtmlData(List<HtmlData> htmlDataList, BusinessObject businessObject) {
          //no-op
      }

      protected List<SubAward> filterForActiveSubAwards(
              Collection<SubAward> collectionByQuery, String awardNumber,
              String subrecipientName, String requisitionerUserName, String statusCode) throws WorkflowException {
    	  
    	  // ### Vivantech Fix : #76 / [#91136756] Fixing sub award lookup to only return latest version entries. - BEGIN
    	  Map<Integer, Integer> subAwardCodes = new HashMap<Integer, Integer>();
          List<Integer> subAwardCodeList = new ArrayList<Integer>();
          
          for (SubAward subAward: collectionByQuery) {
        	  int key = Integer.parseInt(subAward.getSubAwardCode());
        	  if (subAwardCodes.containsKey(key)) {
        		  if(subAward.getSequenceNumber() > subAwardCodes.get(key)) {
        			  subAwardCodes.put(key, subAward.getSequenceNumber());
        		  }
        	  }
        	  else 
        		  subAwardCodes.put(key, subAward.getSequenceNumber());
          }
          
          for (Integer subAwardCode: subAwardCodes.keySet()) {
              subAwardCodeList.add(subAwardCode);
          }
          
          Collections.sort(subAwardCodeList);
          
          List<SubAward> activeSubAwards = new ArrayList<SubAward>();
          for (Integer versionName: subAwardCodeList) {
              VersionHistory versionHistory = versionHistoryService.getVersionHistory(SubAward.class, versionName.toString(), subAwardCodes.get(versionName));
              if (versionHistory != null && VersionStatus.ACTIVE.equals(versionHistory.getStatus())) {
                  SubAward activeSubAward =
                      (SubAward) versionHistory.getSequenceOwner();
                  if (activeSubAward != null) {
                      activeSubAwards.add(activeSubAward);
                  }
              }
          }
    	  // ### Vivantech Fix : #76 / [#91136756] Fixing sub award lookup to only return latest version entries. - END
          
          List<SubAward> filteredSubAwards = new ArrayList<SubAward>();
          for (SubAward subAward : activeSubAwards) {
              if (subrecipientName != null
                      && !subrecipientName.equals("")
                      && subAward.getOrganization().getOrganizationName() != null) {
                  String subRecName = subrecipientName.replace("*", ".*").replace("?",".");
                  subRecName = subRecName.toLowerCase();                                  
                  if (subAward.getOrganization().getOrganizationName().toLowerCase().matches(subRecName)) {
                      filteredSubAwards.add(subAward);
                  }
              } else {
                  filteredSubAwards.add(subAward);
              }
          }

          List<SubAward> filteredSubAwardList = new ArrayList<SubAward>();
          if (awardNumber != null && !awardNumber.equals("")) {
              Collection <Award>awards =
                  getBusinessObjectService().findMatching(
                          Award.class, ServiceHelper.getInstance().
                          buildCriteriaMap(new String[] {
                                  AWARD_NUMBER}, new Object[] {awardNumber }));
              List<Award> linkedAwards = new ArrayList<Award>();
                  for (Award award : awards) {
                      linkedAwards.add(award);
                  }
              List<SubAwardFundingSource> fundingSourceList =
                  new ArrayList<SubAwardFundingSource>();
              for (Award linkedAward : linkedAwards) {
                  Collection <SubAwardFundingSource> subAwardFundingSource =
                      getBusinessObjectService().findMatching(
                              SubAwardFundingSource.class, ServiceHelper.getInstance().
                              buildCriteriaMap(new String[] {"awardId"}, new Object[]
                                                                                    {linkedAward.getAwardId() }));
                      for (SubAwardFundingSource subAwardFunding
                              : subAwardFundingSource) {
                          fundingSourceList.add(subAwardFunding);
                      }
              }

              for (SubAward subAward : filteredSubAwards) {
                  for (SubAwardFundingSource subAwardFunding
                          : fundingSourceList) {
                      if (subAward.getSubAwardId().
                              equals(subAwardFunding.getSubAwardId())) {
                          filteredSubAwardList.add(subAward);
                      }
                  }
              }
        } else {
            for (SubAward subAward : filteredSubAwards) {
                filteredSubAwardList.add(subAward);
            }
        }
        List<SubAward> filteredSubAwardListSubAwards = new ArrayList<SubAward>();
        if (requisitionerUserName != null && !requisitionerUserName.equalsIgnoreCase("")) {
            for (SubAward subAward : filteredSubAwardList) {
                if (subAward.getRequisitionerUserName().equalsIgnoreCase(requisitionerUserName)
                        && (StringUtils.isEmpty(statusCode) || StringUtils.equalsIgnoreCase(statusCode, subAward.getStatusCode().toString()))) {
                    filteredSubAwardListSubAwards.add(subAward);
                }
            }
        }
        else {
            for (SubAward subAward : filteredSubAwardList) {
                if (StringUtils.isEmpty(statusCode) || StringUtils.equalsIgnoreCase(statusCode, subAward.getStatusCode().toString())) {
                    filteredSubAwardListSubAwards.add(subAward);
                }
            }


        }
        return filteredSubAwardListSubAwards;
    }

      @Override
      protected String getHtmlAction() {
          return "subAwardHome.do";
      }

    @Override
    protected String getDocumentTypeName() {
        return "SubAwardDocument";
    }

    @Override
    protected String getKeyFieldName() {
        return "SubAwardId";
    }
    /**
     * This Method is for setVersionHistoryService
     * @param versionHistoryService
     */
    public void setVersionHistoryService(
    VersionHistoryService versionHistoryService) {
       this.versionHistoryService = versionHistoryService;
    }
}
