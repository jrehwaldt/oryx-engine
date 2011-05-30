package org.jodaengine.forms;

import java.util.Map;

/**
 * Implements a form field.
 */
public class JodaFormFieldImpl implements JodaFormField {
    
    private String name, readExpression, readVariable, writeVariable, writeExpression;
    private Class<?> dataClass;


    public JodaFormFieldImpl(String name, Map<String, String> jodaAttributes, Class<?> dataclass) {
        
        this.name = name;
        // TODO REVIEW It is never explained what happens, if the fields are filled with null
        this.readVariable = jodaAttributes.get(JodaFormAttributes.READ_VARIABLE);
        this.readExpression = jodaAttributes.get(JodaFormAttributes.READ_EXPRESSION);
        this.writeVariable = jodaAttributes.get(JodaFormAttributes.WRITE_VARIABLE);
        this.writeExpression = jodaAttributes.get(JodaFormAttributes.WRITE_EXPRESSION);
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

    @Override
    public String getReadVariable() {

        return readVariable;
    }

    @Override
    public String getWriteExpression() {

        return writeExpression;
    }

}
