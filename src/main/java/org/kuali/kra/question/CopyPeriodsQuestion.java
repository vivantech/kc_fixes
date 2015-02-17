package org.kuali.kra.question;

import java.util.ArrayList;

import org.kuali.rice.kns.question.QuestionBase;


public class CopyPeriodsQuestion extends QuestionBase {
    
    public static final String ONE = "1";
    public static final String ALL = "0";
    
    private static final ArrayList<String> BUTTONS;
    static {
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
