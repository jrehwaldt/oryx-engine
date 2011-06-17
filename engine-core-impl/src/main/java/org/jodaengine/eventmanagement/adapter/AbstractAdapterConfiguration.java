package org.jodaengine.eventmanagement.adapter;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;

/**
 * Abstract adapter configuration.
 */
public abstract class AbstractAdapterConfiguration implements AdapterConfiguration {

    private final EventType type;

    /**
     * Default constructor.
     * 
     * @param type
     *            the adapter's type.
     */
    public AbstractAdapterConfiguration(@Nonnull EventType type) {

        this.type = type;
    }

    @Override
    public final @Nonnull
    EventType getEventType() {

        return type;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractAdapterConfiguration other = (AbstractAdapterConfiguration) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
