package org.jodaengine.forms;
// TODO REVIEW Wo kommt dieses Paket here? Entweder resource.allocation oder form?

/**
 * The Class JodaFormAttributes.
 * 
 * TODO REVIEW Wow... Wat'ne Ansage! Sind das die berüchtigten "joda-tags"?
 */
public final class JodaFormAttributes {

    public static final String WRITE_VARIABLE = "joda:writeVariable";
    public static final String WRITE_EXPRESSION = "joda:writeExpression";
    public static final String READ_VARIABLE = "joda:readVariable";
    public static final String READ_EXPRESSION = "joda:readExpression";
    public static final String CLASS_NAME = "joda:class";
    
    // TODO REVIEW stattdessen als 'enum' implementieren - vielleicht aber auch nicht.
    /**
     * Hidden constructor as this class only holds String constants.
     */
    private JodaFormAttributes() {
        
    }
}
