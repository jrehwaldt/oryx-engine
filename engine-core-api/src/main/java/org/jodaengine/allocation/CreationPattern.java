package org.jodaengine.allocation;

import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist items according to a specific Creation strategy.
     *
     * @param token the token
     * @param repoService the repository service to receive artifacts (e.g. forms) from
     * @return a list of created worklist items
     */
    List<AbstractWorklistItem> createWorklistItems(Token token, RepositoryService repoService);
    
    
}
