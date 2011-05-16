package org.jodaengine.resource.worklist;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.jodaengine.resource.AbstractResource;

/**
 * Null Object pattern. This is an empty worklist for resources, which do not have a
 * worklist.
 */
public final class EmptyWorklist extends AbstractWorklist {

    static final String EXCEPTION_MESSAGE = "The resource object has no special type.";

    @Override
    @JsonIgnore
    public List<AbstractWorklistItem> getWorklistItems() {

        List<AbstractWorklistItem> emptyWorklistItems = Collections.emptyList();
        return Collections.unmodifiableList(emptyWorklistItems);
    }

    @Override
    public void itemIsCompleted(AbstractWorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void itemIsStarted(AbstractWorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void addWorklistItem(AbstractWorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void removeWorklistItem(AbstractWorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);

    }

    @Override
    public void itemIsAllocatedBy(AbstractWorklistItem worklistItem, AbstractResource<?> claimingResource) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public Iterator<AbstractWorklistItem> iterator() {

        return getWorklistItems().iterator();
    }

}
