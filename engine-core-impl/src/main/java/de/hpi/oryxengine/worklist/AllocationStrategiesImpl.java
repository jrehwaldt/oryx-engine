package de.hpi.oryxengine.worklist;

import javax.annotation.Nonnull;

/**
 * The implementation of the {@link AllocationStrategies}.
 */
public class AllocationStrategiesImpl implements AllocationStrategies {

    /** The push pattern. */
    private Pattern pushPattern;
    
    /** The pull pattern. */
    private Pattern pullPattern;
    
    /** The detour pattern. */
    private Pattern detourPattern;
    
    /** The creation pattern. */
    private Pattern creationPattern;
    
    /**
     * Default Constructor for the {@link AllocationStrategies}.
     * 
     * @param pushPattern - pattern for the distribution of tasks
     * @param pullPattern - pattern for claiming tasks
     * @param detourPattern - pattern for aborting tasks
     * @param creationPattern - pattern for the restricted access on tasks
     */
    public AllocationStrategiesImpl(@Nonnull Pattern pushPattern,
                                    @Nonnull Pattern pullPattern,
                                    Pattern detourPattern,
                                    Pattern creationPattern) {

        this.pushPattern = pushPattern;
        this.pullPattern = pullPattern;
        this.detourPattern = detourPattern;
        this.creationPattern = creationPattern;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Pattern getPullPattern() {

        return pullPattern;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pattern getPushPattern() {

        return pushPattern;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pattern getDetourPattern() {

        return detourPattern;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pattern getCreationPattern() {

        return creationPattern;
    }
}
