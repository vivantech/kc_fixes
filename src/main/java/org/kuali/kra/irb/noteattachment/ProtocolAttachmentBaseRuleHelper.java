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
package org.kuali.kra.irb.noteattachment;

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.protocol.noteattachment.ProtocolAttachmentService;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.service.KNSServiceLocator;

/**
 * This class contains methods to "help" in validating ProtocolAttachmentBase.
 */
@SuppressWarnings("deprecation")
class ProtocolAttachmentBaseRuleHelper extends org.kuali.kra.protocol.noteattachment.ProtocolAttachmentBaseRuleHelper {

    protected ProtocolAttachmentBaseRuleHelper(String aPropertyPrefix) {
        super(aPropertyPrefix, KraServiceLocator.getService(ProtocolAttachmentService.class), KNSServiceLocator.getKNSDictionaryValidationService());
    }

    protected ProtocolAttachmentBaseRuleHelper(final ProtocolAttachmentService attachmentService,
            final DictionaryValidationService validationService) {
                super(attachmentService, validationService);
    }
    
}
