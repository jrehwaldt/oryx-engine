package org.jodaengine.eventmanagement.adapter.mail;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventTypes;

/**
 * The Class generic mail configuration which serves as a super class for incoming and outgoign ones.
 */
public abstract class AbstractMailConfiguration extends AbstractAdapterConfiguration {

    protected final String userName;
    protected final String password;
    protected final String domainName;
    
    /**
     * Instantiates a new mail configuration.
     *
     * @param userName the user name
     * @param password the password
     * @param address the address
     */
    public AbstractMailConfiguration(@Nonnull String userName,
                                           @Nonnull String password,
                                           @Nonnull String address) {

        super(EventTypes.Mail);
        this.userName = userName;
        this.password = password;
        this.domainName = address;
    }

    /**
     * Returns the user name.
     * 
     * @return the user name
     */
    public @Nonnull
    String getUserName() {

        return userName;
    }

    /**
     * Returns the password.
     * 
     * @return the password.
     */
    public @Nonnull
    String getPassword() {

        return password;
    }

    /**
     * Returns the address. If javax.mail is used, it is recommended to use toCon
     * 
     * @return the address
     */
    public @Nonnull
    String getDomainName() {

        return domainName;
    }
    
    /**
     * Gets the email address (a combination of username and domain name).
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return this.userName + "@" + this.getDomainName();
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((domainName == null) ? 0 : domainName.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractMailConfiguration other = (AbstractMailConfiguration) obj;
        if (domainName == null) {
            if (other.domainName != null) {
                return false;
            }
        } else if (!domainName.equals(other.domainName)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }
        return true;
    }
    
}
