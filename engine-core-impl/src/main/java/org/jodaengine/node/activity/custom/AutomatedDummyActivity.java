package org.jodaengine.node.activity.custom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Class AutomatedDummyNode.
 * It really is dumb. It just prints out whatever message is send to it.
 */
public class AutomatedDummyActivity extends AbstractActivity {

    /** This is the message the node prints out during its execution. */
    private String message;

    /**
     * Instantiates a new automated dummy node.
     *  
     * @param s the String which message gets set to and which gets printed out.
     */
    public AutomatedDummyActivity(@Nullable String s) {
        super();
        this.message = s;
    }

    @Override
    protected void executeIntern(@Nonnull BPMNToken instance) {
        System.out.println(this.message);
    }

}
