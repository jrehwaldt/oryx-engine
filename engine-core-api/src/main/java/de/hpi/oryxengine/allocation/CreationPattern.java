package de.hpi.oryxengine.allocation;

/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist items according to a specific Creation strategy.
     */
    void createWorklistItems();
}
