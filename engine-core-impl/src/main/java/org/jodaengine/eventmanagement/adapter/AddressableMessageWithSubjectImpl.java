package org.jodaengine.eventmanagement.adapter;

/**
 * A message that can be addressed to someone and has a subject as well.
 */
public class AddressableMessageWithSubjectImpl extends AddressableMessageImpl implements AddressableMessageWithSubject {

    private String subject;

    /**
     * Instantiates a new addressable message with a subject.
     * @param address the address
     * @param subject the subject
     * @param content the content
     */
    public AddressableMessageWithSubjectImpl(String address, String subject, String content) {

        super(address, content);
        this.subject = subject;
    }
    
    @Override
    public String getSubject() {
        return subject;
    }

}
