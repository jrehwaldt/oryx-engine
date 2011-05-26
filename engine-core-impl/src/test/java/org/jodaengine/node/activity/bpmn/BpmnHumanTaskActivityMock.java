package org.jodaengine.node.activity.bpmn;

import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.PushPattern;
import org.jodaengine.process.token.Token;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;

/**
 * Only a mock for testing the BpmnHumanTaskActivity.
 */
public class BpmnHumanTaskActivityMock extends BpmnHumanTaskActivity {

    /**
     * @see BpmnHumanTaskActivity#BpmnHumanTaskActivity(CreationPattern, PushPattern)
     */
    public BpmnHumanTaskActivityMock(CreationPattern creationPattern, PushPattern pushPattern) {

        super(creationPattern, pushPattern);
    }

    /**
     * Returns the identifier for the internal variable.
     * 
     * @param token
     *            - Token that should contain the variable
     * @return a {@link String} representing the identifier
     */
    public String getInternaIdentifier(Token token) {

        return internalVariableId(getPrefixForInternalVariable(), token);
    }

    /**
     * Returns the prefix of the internal variable.
     * 
     * @return a {@link String} representing the prefix of the internal variable
     */
    public String getPrefixForInternalVariable() {

        // If the following line throws an error, it might be that the private variable in the class
        // BpmnHumanTaskActivity is not called "ITEM_PREFIX" anymore.
        String prefix = (String) Whitebox.getInternalState(this, "ITEM_PREFIX");
        Assert.assertNotNull(prefix);
        Assert.assertFalse(prefix.isEmpty());

        return prefix;
    }
}
