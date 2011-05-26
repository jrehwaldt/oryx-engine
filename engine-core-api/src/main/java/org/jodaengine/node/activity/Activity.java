package org.jodaengine.node.activity;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.token.Token;

/**
 * An activity is the behaviour of a Node, e.g. execution
 * behaviour for sending a mail.
 * 
 * !!!Activities should be programmed stateless!!!
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Activity {

    /**
     * Execute. Starts the execution of the Activity.
     * 
     * @param token
     *            the instance the activity operates on
     */
    void execute(@Nonnull Token token);

    /**
     * Do some cleanup, if necessary. Cancellation of the activity's execution is not handled here.
     * 
     * @param executingToken
     *            the token that currently executes the activity
     */
    void cancel(Token executingToken);

    /**
     * This method is called when the execution of the activity is resumed.
     * 
     * @param token
     *            the token that resumes this activity
     * @param resumeObject
     *            - an object that is passed from class that resumes the Token
     */
    void resume(Token token, Object resumeObject);
}
