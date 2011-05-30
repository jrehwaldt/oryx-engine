package org.jodaengine.forms;

/**
 * The Class FormFieldArguments holds the arguments to instantiate a {@link JodaFormField}. These are the input and
 * output variables and the input and output expressions.
 */
public class JodaFormFieldArguments {
    private String inputVariable = null;
    private String outputVariable = null;
    private String inputExpression = null;
    private String outputExpression = null;

    /**
     * Gets the input context variable name.
     *
     * @return the input variable or <code>null</code> if not specified
     */
    public String getInputVariable() {
    
        return inputVariable;
    }

    /**
     * Sets the input context variable name.
     *
     * @param inputVariable the new input variable
     */
    public void setInputVariable(String inputVariable) {
    
        this.inputVariable = inputVariable;
    }

    /**
     * Gets the output context variable name.
     *
     * @return the output variable or <code>null</code> if not specified
     */
    public String getOutputVariable() {
    
        return outputVariable;
    }

    /**
     * Sets the output context variable name.
     *
     * @param outputVariable the new output variable
     */
    public void setOutputVariable(String outputVariable) {
    
        this.outputVariable = outputVariable;
    }

    /**
     * Gets the expression for form value input evaluation.
     *
     * @return the input expression or <code>null</code> if not specified
     */
    public String getInputExpression() {
    
        return inputExpression;
    }

    /**
     * Sets the input expression.
     *
     * @param inputExpression the new input expression
     */
    public void setInputExpression(String inputExpression) {
    
        this.inputExpression = inputExpression;
    }

    /**
     * Gets the expression for evaluation of the initial form value.
     *
     * @return the output expression or <code>null</code> if not specified
     */
    public String getOutputExpression() {
    
        return outputExpression;
    }

    /**
     * Sets the output expression.
     *
     * @param outputExpression the new output expression
     */
    public void setOutputExpression(String outputExpression) {
    
        this.outputExpression = outputExpression;
    }

    
}
