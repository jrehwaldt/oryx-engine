package org.jodaengine.eventmanagement.adapter;

/**
 * A generalMessage.
 */
public class MessageImpl implements Message {

    /** The content. */
    private String content;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getContent() {

        return content;
    }

    /**
     * Construct a new Message with the given content.
     *
     * @param content the content
     */
    public MessageImpl(String content) {

        this.content = content;
    }

}
