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
package org.kuali.kra.irb.actions.print;

import org.kuali.kra.protocol.actions.print.ProtocolPrintWatermarkBase;
import org.kuali.kra.protocol.actions.print.ProtocolReviewCommentsPrintBase;


/**
 * This class provides the implementation for printing Protocol Review Comments Report. It
 * generates XML that conforms with Protocol Review Comments Report XSD, fetches XSL style-sheets
 * applicable to this XML, returns XML and XSL for any consumer that would use
 * this XML and XSls for any purpose like report generation, PDF streaming etc.
 * 
 */
public class ProtocolReviewCommentsPrint extends ProtocolReviewCommentsPrintBase {

    private static final long serialVersionUID = 7330987522940892013L;

    
    @Override
    protected ProtocolPrintWatermarkBase getNewProtocolPrintWatermarkInstanceHook() {
        return new ProtocolPrintWatermark();
    }
}

