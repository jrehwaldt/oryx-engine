package org.jodaengine.eventmanagement.adapter;

/**
 * A message that can also be adressed to somebody (like the TO-Field from an email).
 */
public class AddressableMessageImpl extends MessageImpl implements AddressableMessage {


    private String address;

    @Override
    public String getAddress() {

        return address;
    }

    /**
     * Construct a new Message with a content and an adress.
     * @param address the address
     * @param content the content
     */
    public AddressableMessageImpl(String address, String content) {

        super(content);
        this.address = address;
    }

}
