package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;

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
     * @param variableName the variable name
     * @param summands summands
     */
    public AddNumbersAndStoreActivity(String variableName,
                                      int... summands) {
        super();
        this.summands = summands;
        resultVariableName = variableName;
    }

    /**
     * Execute intern.
     *
     * @param token the token
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(@Nonnull Token token) {
        
        int result = 0;
        for (int value: this.summands) {
            result += value;
        }
        ProcessInstanceContext context = token.getInstance().getContext();
        context.setVariable(resultVariableName, String.valueOf(result));
    }
}
