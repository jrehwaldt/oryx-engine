package org.jodaengine.eventmanagement.adapter.mail;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventTypes;

/**
 * The Class generic mail configuration which serves as a super class for incoming and outgoign ones.
 */
public abstract class AbstractMailConfiguration extends AbstractAdapterConfiguration {

    /** The user name. */
    protected final String userName;
    
    /** The password. */
    protected final String password;
    
    // TODO: EVENTTEAM: Better name? Address is misleading (this appears to be the hsoter like web.de...)
    /** The address. */
    protected final String address;
    
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
        this.address = address;
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
    String getAddress() {

        return address;
    }
}
