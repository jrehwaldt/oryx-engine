package org.jodaengine.forms;

/**
 * The Class JodaFormAttributes.
 * Defines the names to use to specify input/output variables and expressions ins HTML forms.
 */
public final class JodaFormAttributes {

    public static final String INPUT_VARIABLE = "joda:writeVariable";
    public static final String INPUT_EXPRESSION = "joda:writeExpression";
    public static final String OUTPUT_VARIABLE = "joda:readVariable";
    public static final String OUTPUT_EXPRESSION = "joda:readExpression";
    public static final String CLASS_NAME = "joda:class";
    
    /**
     * Hidden constructor as this class only holds String constants.
     */
    private JodaFormAttributes() {
        
    }
}
