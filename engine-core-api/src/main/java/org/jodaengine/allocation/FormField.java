package org.jodaengine.allocation;

/**
 * A FormField is made up of a name, an expression for variable getting and setting and a data type that form input
 * should be converted to.
 */
public interface FormField {

    String getName();
    
    String getExpression();
    
    Class<?> getDataClazz();
}
