package org.jodaengine.node.activity.custom;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.Token;

import javax.annotation.Nonnull;


/**
 * The Class AddNumbersAndStoreActivity.
 * As the name indicates, an activity that adds any number of summands and stores the result.
 */
public class AddContextNumbersAndStoreActivity
extends AbstractActivity {

    /** Summands. */
    private String[] summands;

    /** The name the resulting variable should have. */
    private String resultVariableName;

    /**
     * Instantiates a "new adds the numbers and store" activity.
     *
     * @param variableName the variable name
     * @param summands summands
     */
    public AddContextNumbersAndStoreActivity(String variableName,
                                      String... summands) {
        super();
        this.summands = summands;
        resultVariableName = variableName;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {
        
        ProcessInstanceContext context = token.getInstance().getContext();
        
        int result = 0;
        for (String value : this.summands) {
            
            result +=  Integer.valueOf(context.getVariable(value).toString());
        }

        context.setVariable(resultVariableName, result);
    }
}
