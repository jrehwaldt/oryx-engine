package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.token.Token;

/**
 * An activity is the behaviour of a Node, e.g. execution
 * behaviour for sending a mail.
 */
public interface Activity {
    
    /**
     * Execute. Starts the execution of the Activity.
     * 
     * @param token the instance the activity operates on
     */
    void execute(@Nonnull Token token);
    
    /**
     * Returns the activity's state.
     * 
     * @return the activity's state
     */
    @Nonnull ActivityState getState();
    
    // TODO da sollte noch ein weiterer Parameter übergeben werden
    // Ein Event oder irgendein Objekt, das sagt das Signal gekommen ist 
    void signal(@Nonnull Token token);
}
