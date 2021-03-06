/*
 * Copyright 2005-2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.budget.core;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class BudgetCategoryMapping extends KraPersistableBusinessObjectBase {

    private String budgetCategoryCode;

    private String mappingName;

    private String targetCategoryCode;

    private BudgetCategory budgetCategory;
    
    // Vivantech Fix : #70 / [#90560868] adding active indicator field and disabling the delete.
    private boolean active;

    public String getBudgetCategoryCode() {
        return budgetCategoryCode;
    }

    public void setBudgetCategoryCode(String budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getTargetCategoryCode() {
        return targetCategoryCode;
    }

    public void setTargetCategoryCode(String targetCategoryCode) {
        this.targetCategoryCode = targetCategoryCode;
    }

    /**
     * Gets the budgetCategory attribute. 
     * @return Returns the budgetCategory.
     */
    public BudgetCategory getBudgetCategory() {
        return budgetCategory;
    }

    /**
     * Sets the budgetCategory attribute value.
     * @param budgetCategory The budgetCategory to set.
     */
    public void setBudgetCategory(BudgetCategory budgetCategory) {
        this.budgetCategory = budgetCategory;
    }

    // Vivantech Fix : #70 / [#90560868] adding active indicator field and disabling the delete.
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
    
}
