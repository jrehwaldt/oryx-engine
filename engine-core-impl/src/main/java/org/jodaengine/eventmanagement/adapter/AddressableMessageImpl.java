package org.jodaengine.eventmanagement.adapter;

/**
 * A message that can also be adressed to somebody (like the TO-Field from an email).
 */
public class AddressableMessageImpl extends MessageImpl implements AddressableMessage {


    private String address;

    @Override
    public String getAdress() {

        return address;
    }

    /**
     * Construct a new Message with a content and an adress.
     *
     * @param content the content
     * @param address the address
     */
    public AddressableMessageImpl(String content, String address) {

        super(content);
        this.address = address;
    }

}
