package de.hpi.oryxengine.worklist;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.token.Token;

/**
 * Internal interface for the distribution of human tasks.
 */
public interface TaskDistribution {

    // TODO Hinschreiben was genau die Default AllocationStrategy ist
    /**
     * Distributes a {@link Task} with the default {@link AllocationStrategies}.
     *
     * @param task - {@link Task} that should be resolved to certain worklists
     * @param token the token
     */
    void distribute(@Nonnull Task task,
                    @Nonnull Token token);
    
    
    
}
