package de.hpi.oryxengine.allocation;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Pattern;

/**
 * The implementation of the {@link AllocationStrategies}.
 */
public class AllocationStrategiesImpl implements AllocationStrategies {

    private Pattern pushPattern;

    private Pattern pullPattern;

    private Pattern detourPattern;

    private Pattern creationPattern;

    /**
     * Default Constructor for the {@link AllocationStrategies}.
     * 
     * @param pushPattern
     *            - pattern for the distribution of tasks
     * @param pullPattern
     *            - pattern for claiming tasks
     * @param detourPattern
     *            - pattern for aborting tasks
     * @param creationPattern
     *            - pattern for the restricted access on tasks
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

    @Override
    public Pattern getPullPattern() {

        return pullPattern;
    }

    @Override
    public Pattern getPushPattern() {

        return pushPattern;
    }

    @Override
    public Pattern getDetourPattern() {

        return detourPattern;
    }

    @Override
    public Pattern getCreationPattern() {

        return creationPattern;
    }
}
