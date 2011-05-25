package org.jodaengine.allocation;

/**
 * A FormField is made up of a name, an expression for variable getting and setting and a data type that form input
 * should be converted to.
 */
public interface JodaFormField {

    String getName();
    
    /**
     * Gets the expression that is used to read from the context, when the initial value of this field is determined.
     *
     * @return the read expression
     */
    String getReadExpression();
    
    /**
     * Gets the expression that is used to write to the context, when an input value for this field is processed.
     *
     * @return the write expression
     */
    String getWriteExpression();
    
    Class<?> getDataClazz();
}
