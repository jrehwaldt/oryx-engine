package org.jodaengine.resource.allocation;

/**
 * A FormField is made up of a name, an expression for variable getting and setting and a data type that form input
 * should be converted to.
 */
public interface JodaFormField {

    /**
     * Gets the name of the form field.
     *
     * @return the name
     */
    String getName();
    
    /**
     * Gets the expression that is used to read from the context, when the initial value of this field is determined.
     *
     * @return the read expression
     */
    String getReadExpression();
    
    /**
     * Gets the variable that is used to write to the context, when an input value for this field is processed.
     * Currently, no JUEL can be used here, but only context variable names.
     *
     * @return the write expression
     */
    String getWriteVariable();
    
    /**
     * Gets the class the input should be transformed to (from String).
     *
     * @return the data clazz
     */
    Class<?> getDataClazz();
}
