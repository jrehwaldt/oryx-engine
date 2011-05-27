package org.jodaengine.resource.allocation;

import org.jodaengine.RepositoryService;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist item.
     * 
     * @param token
     *            the token
     * @param repoService
     *            the repository service to receive artifacts (e.g. forms) from
     * @return a list of created worklist items
     */
    AbstractWorklistItem createWorklistItem(Token token, RepositoryService repoService);

    /**
     * Gets the push pattern of the creation pattern that describes the way of distribution of an item.
     * 
     * @return the push pattern
     */
    PushPattern getPushPattern();
}
