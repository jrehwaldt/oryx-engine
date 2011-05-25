package org.jodaengine.resource.allocation;

import org.jodaengine.allocation.JodaFormField;

/**
 * Implements a form field.
 */
public class JodaFormFieldImpl implements JodaFormField {

    private String name, readExpression, writeExpression;
    private Class<?> dataClass;
    
    public JodaFormFieldImpl(String name, String readExpression, String writeExpression, Class<?> dataclass) {
        this.name = name;
        this.readExpression = readExpression;
        this.writeExpression = writeExpression;
        this.dataClass = dataclass;
    }
    
    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getReadExpression() {

        return readExpression;
    }
    
    @Override
    public String getWriteExpression() {

        return writeExpression;
    }

    @Override
    public Class<?> getDataClazz() {

        return dataClass;
    }

}
