/*
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the License);
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
package org.kuali.kra.budget.rates;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class RateClassType extends KraPersistableBusinessObjectBase {

    private String rateClassType;

    private String description;

    private String sortId;

    private Boolean prefixActivityType;
    
    // Vivantech Fix : #70 / [#90560868] adding active indicator field and disabling the delete.
    private boolean active;

    public RateClassType() {
        super();
    }

    public String getRateClassType() {
        return rateClassType;
    }

    public void setRateClassType(String rateClassType) {
        this.rateClassType = rateClassType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public final Boolean getPrefixActivityType() {
        return prefixActivityType;
    }

    public final void setPrefixActivityType(Boolean prefixActivityType) {
        this.prefixActivityType = prefixActivityType;
    }

    // Vivantech Fix : #70 / [#90560868] adding active indicator field and disabling the delete.
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
