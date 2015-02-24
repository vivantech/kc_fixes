package org.kuali.kra.question;

import java.util.ArrayList;

import org.kuali.rice.kns.question.QuestionBase;

/* ### Vivantech Fix : #50 / [#86465282] Updating the BUTTONS list order by placing 'copyallperiods' first.
 * This will correct the issue of pressing 'Copy One' resulting in copying all periods and vice versa.
 * The code is unable to update the constants of ONE and ALL because they are Strings which get statically compiled.
 * The BUTTONS ArrayList gets compiled at run time and therefore can be updated in the overridde file.
 */
public class CopyPeriodsQuestion extends QuestionBase {
    
    public static final String ONE = "1";
    public static final String ALL = "0";
    
    private static final ArrayList<String> BUTTONS;
    static {
    	// Issue #50 / [#86465282] Correcting the order in which the copy buttons display
        BUTTONS = new ArrayList<String>();
        BUTTONS.add("copyallperiods");
        BUTTONS.add("copyoneper");        
    }
    
    /**
     * @param question
     * @param buttons
     */
    public CopyPeriodsQuestion() {
        super("Confirm", BUTTONS);
    }

}
