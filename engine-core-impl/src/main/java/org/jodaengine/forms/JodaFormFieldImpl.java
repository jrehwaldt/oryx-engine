package org.jodaengine.forms;


/**
 * Implements a form field.
 */
public class JodaFormFieldImpl implements JodaFormField {
    
    private String name, outputExpression, outputVariable, inputVariable, inputExpression;
    private Class<?> dataClass;


    /**
     * Creates a new JodaFormFieldImpl from the data as specified.
     *
     * @param name the name of the form field in the corresponding HTML.
     * @param jodaAttributes a container that holds all the joda:*-attributes from the HTML.
     * @param dataclass the class that the input should be coerced to from {@link String}.
     */
    public JodaFormFieldImpl(String name, JodaFormFieldArguments jodaAttributes, Class<?> dataclass) {
        
        this.name = name;
        this.outputVariable = jodaAttributes.getOutputVariable();
        this.outputExpression = jodaAttributes.getOutputExpression();
        this.inputVariable = jodaAttributes.getInputVariable();
        this.inputExpression = jodaAttributes.getInputExpression();
        this.dataClass = dataclass;
    }

    @Override
    public String getName() {

        return name;
    }
    
    @Override
    public String getOutputExpression() {

        return outputExpression;
    }

    @Override
    public String getInputVariable() {

        return inputVariable;
    }

    @Override
    public Class<?> getDataClazz() {

        return dataClass;
    }

    @Override
    public String getOutputVariable() {

        return outputVariable;
    }

    @Override
    public String getInputExpression() {

        return inputExpression;
    }

}
