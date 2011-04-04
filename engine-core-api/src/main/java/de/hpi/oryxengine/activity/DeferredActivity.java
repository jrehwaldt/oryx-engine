package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface DeferredActivity.
 */
public interface DeferredActivity extends Activity {
    
    // TODO da sollte noch ein weiterer Parameter übergeben werden
    // Ein Event oder irgendein Objekt, das sagt das Signal gekommen ist 
    /**
     * Signal the token to do some work after the token was continued.
     *
     * @param token the token
     */
    void signal(@Nonnull Token token);

}
