package org.jodaengine.node.activity.bpmn;

import org.jodaengine.process.token.Token;
import org.jodaengine.resource.allocation.CreationPattern;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;

/**
 * Only a mock for testing the BpmnHumanTaskActivity.
 */
public class BpmnHumanTaskActivityMock extends BpmnHumanTaskActivity {

    /**
     * Instantiates a new bpmn human task activity mock.
     *
     * @param creationPattern the creation pattern
     * @see BpmnHumanTaskActivity#BpmnHumanTaskActivity(CreationPattern, PushPattern)
     */
    public BpmnHumanTaskActivityMock(CreationPattern creationPattern) {

        super(creationPattern);
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
