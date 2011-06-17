package org.jodaengine.eventmanagement.adapter;

/**
 * A message that can be addressed to someone and has a subject as well.
 */
public class AddressableMessageWithSubjectImpl extends AddressableMessageImpl implements AddressableMessageWithSubject {

    private String subject;

    /**
     * Instantiates a new addressable message with a subject.
     *
     * @param content the content
     * @param address the address
     * @param subject the subject
     */
    public AddressableMessageWithSubjectImpl(String content, String address, String subject) {

        super(content, address);
        this.subject = subject;
    }
    
    @Override
    public String getSubject() {
        return subject;
    }

}
