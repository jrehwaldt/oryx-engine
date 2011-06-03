package org.jodaengine.util.juel;

import javax.el.ELContext;
import javax.el.PropertyNotFoundException;

import de.odysseus.el.util.RootPropertyResolver;

/**
 * A RootPropertyResolver that does not throw an error, if a root property is undefined, but rather returns null.
 */
public class JodaRootPropertyResolver extends RootPropertyResolver {

    /**
     * Catches the {@link PropertyNotFoundException} that is thrown in the {@link RootPropertyResolver} implementation
     * and returns <code>null</code> instead.
     * 
     * {@inheritDoc}
     */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {

        try {
            return super.getValue(context, base, property);
        } catch (PropertyNotFoundException e) {
            return null;
        }
    }
}
