package org.jodaengine.resource.allocation;

import org.jodaengine.resource.allocation.JodaFormField;

/**
 * Implements a form field.
 */
public class JodaFormFieldImpl implements JodaFormField {

    private String name, readExpression, writeVariable, variableToInitialize;
    private Class<?> dataClass;
    
    public JodaFormFieldImpl(String name, String readExpression, String writeVariable, Class<?> dataclass) {
        this.name = name;
        this.readExpression = readExpression;
        this.writeVariable = writeVariable;
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
    public String getWriteVariable() {

        return writeVariable;
    }

    @Override
    public Class<?> getDataClazz() {

        return dataClass;
    }

}
