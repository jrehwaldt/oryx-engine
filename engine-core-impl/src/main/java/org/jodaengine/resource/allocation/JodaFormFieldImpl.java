package org.jodaengine.resource.allocation;

import org.jodaengine.allocation.JodaFormField;

/**
 * Implements a form field.
 */
public class JodaFormFieldImpl implements JodaFormField {

    private String name, expression;
    private Class<?> dataClass;
    
    public JodaFormFieldImpl(String name, String expression, Class<?> dataclass) {
        this.name = name;
        this.expression = expression;
        this.dataClass = dataclass;
    }
    
    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getExpression() {

        return expression;
    }

    @Override
    public Class<?> getDataClazz() {

        return dataClass;
    }

}
