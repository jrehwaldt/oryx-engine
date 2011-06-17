package org.jodaengine.eventmanagement.adapter;

/**
 * A message that can be adressed, like the To of an Email.
 */
public interface AddressableMessage extends Message {

    /**
     * Gets the adress this message is send to.
     *
     * @return the adress
     */
    String getAddress();
}
