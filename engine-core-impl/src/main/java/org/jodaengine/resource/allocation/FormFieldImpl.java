package org.jodaengine.resource.allocation;

import org.jodaengine.allocation.FormField;

/**
 * Implements a form field.
 */
public class FormFieldImpl implements FormField {

    private String name, expression;
    private Class<?> dataClass;
    
    public FormFieldImpl(String name, String expression, Class<?> dataclass) {
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
