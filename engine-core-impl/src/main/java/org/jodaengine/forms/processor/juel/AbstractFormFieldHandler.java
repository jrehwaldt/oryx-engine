package org.jodaengine.forms.processor.juel;

import org.jodaengine.deployment.importer.archive.AbstractDarHandler;

/**
 * Realizes a chain of responsibility for form field resolution.
 */
public abstract class AbstractFormFieldHandler {
    protected AbstractFormFieldHandler next;

    public void setNext(AbstractFormFieldHandler next) {
        this.next = next;
    }
}
