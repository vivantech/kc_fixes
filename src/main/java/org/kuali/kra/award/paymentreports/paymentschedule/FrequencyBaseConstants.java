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
package org.kuali.kra.award.paymentreports.paymentschedule;

/**
 * This class...
 */
public enum FrequencyBaseConstants {
    AWARD_EXECUTION_DATE("1"),AWARD_EFFECTIVE_DATE("2"),AWARD_EXPIRATION_DATE_OF_OBLIGATION("3"),FINAL_EXPIRATION_DATE("4")
        ,AWARD_EFFECTIVE_DATE_OF_OBLIGATION("5");
    
    String frequencyBase;
    
    FrequencyBaseConstants(String frequencyBase){
        this.frequencyBase = frequencyBase;
    }
    
    public String getfrequencyBase(){
        return frequencyBase;
    }
    

}
