package org.jodaengine.node.activity.custom;

import javax.annotation.Nonnull;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Class AddNumbersAndStoreActivity.
 * As the name indicates, an activity that adds any number of summands and stores the result.
 */
public class AddNumbersAndStoreActivity
extends AbstractActivity {

    /** Summands. */
    private int[] summands;

    /** The name the resulting variable should have. */
    private String resultVariableName;

    /**
     * Instantiates a "new adds the numbers and store" activity.
     *
     * @param variableName - the name of the variable for accessing the result
     * @param summands summands
     */
    public AddNumbersAndStoreActivity(String variableName,
                                      int... summands) {
        super();
        this.summands = summands;
        resultVariableName = variableName;
    }

    @Override
    protected void executeIntern(@Nonnull BPMNToken bPMNToken) {
        
        int result = 0;
        for (int value: this.summands) {
            result += value;
        }
        ProcessInstanceContext context = bPMNToken.getInstance().getContext();
        context.setVariable(resultVariableName, String.valueOf(result));
    }
}
