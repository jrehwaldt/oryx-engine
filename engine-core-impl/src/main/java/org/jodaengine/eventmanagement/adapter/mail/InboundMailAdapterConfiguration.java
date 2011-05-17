package org.jodaengine.eventmanagement.adapter.mail;

import java.util.Properties;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterRegistrar;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.PullAdapterConfiguration;
import org.jodaengine.eventmanagement.timing.PullAdapterJob;
import org.jodaengine.exception.AdapterSchedulingException;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The mail adapter configuration.
 */
public final class InboundMailAdapterConfiguration extends AbstractAdapterConfiguration implements
PullAdapterConfiguration {

    private final String userName;
    private final String password;
    private final String address;
    private final int port;
    private final boolean useSSL;
    private final MailProtocol protocol;
    private static final long DEFAULT_INTERVAL = 6000L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Default constructor.
     * 
     * @param protocol
     *            the mail protocol
     * @param userName
     *            the account's user name
     * @param password
     *            the account's password
     * @param address
     *            the mail server's address
     * @param port
     *            the mail server's port
     * @param useSSL
     *            should ssl be used for connection
     */
    public InboundMailAdapterConfiguration(@Nonnull MailProtocol protocol,
                                           @Nonnull String userName,
                                           @Nonnull String password,
                                           @Nonnull String address,
                                           @Nonnegative int port,
                                           @Nonnull boolean useSSL) {

        super(EventTypes.Mail);
        this.protocol = protocol;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.port = port;
        this.useSSL = useSSL;
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

    /**
     * Returns the port number.
     * 
     * @return the port number
     */
    public @Nonnegative
    int getPort() {

        return port;
    }

    /**
     * Creates a property set configured with the corresponding
     * settings.
     * 
     * @return a properties instance
     */
    public @Nonnull
    Properties toMailProperties() {

        // create the properties for the Session
        Properties props = new Properties();

        String identifier = "";
        switch (this.protocol) {
            case IMAP:
                identifier = "imap";
                break;
            case POP3:
                identifier = "pop3";
                break;
            default:
                logger.error("No supported mail type configured: {}", this.protocol);
        }

        if (this.useSSL) {
            // set this session up to use SSL for IMAP connections
            props.setProperty("mail." + identifier + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            // don't fallback to normal IMAP connections on failure.
            props.setProperty("mail." + identifier + ".socketFactory.fallback", "false");
        }

        // use the simap port for imap/ssl connections.
        props.setProperty("mail." + identifier + ".socketFactory.port", String.valueOf(this.port));
        props.setProperty("mail." + identifier + ".port", String.valueOf(this.port));

        // note that you can also use the defult imap port (including the
        // port specified by mail.imap.port) for your SSL port configuration.
        // however, specifying mail.imap.socketFactory.port means that,
        // if you decide to use fallback, you can try your SSL connection
        // on the SSL port, and if it fails, you can fallback to the normal
        // IMAP port.

        return props;
    }

    @Override
    public long getTimeInterval() {

        return DEFAULT_INTERVAL;
    }

    /**
     * Returns, whether to use ssl.
     * 
     * @return should use ssl
     */
    public boolean isUseSSL() {

        return useSSL;
    }

    /**
     * Returns the account's protocol.
     * 
     * @return the account protocol
     */
    public MailProtocol getProtocol() {

        return protocol;
    }

    @Override
    public String getUniqueName() {

        return String.format("%s:%s:%s:%s", protocol, address, port, userName);
    }

    /**
     * JodaEngine google configuration. This is to prevent code duplication. Remove this later.
     * 
     * @return the mail adapter configuration
     */
    public static InboundMailAdapterConfiguration jodaGoogleConfiguration() {

        // TODO @All: WTF delete this (in July). Other options would be a local file.. but well. no.
// CHECKSTYLE:OFF
        return new InboundMailAdapterConfiguration(MailProtocol.IMAP, "oryxengine", "dalmatina!",
            "imap.googlemail.com", MailProtocol.IMAP.getPort(true), true);
// CHECKSTYLE:ON
    }

    @Override
    public Class<? extends Job> getScheduledClass() {

        return PullAdapterJob.class;
    }

    /**
     * create the Adapter which is defined by this Adapter configuration.
     * 
     * @param correlationManager
     *            - the {@link CorrelationManager}
     * @return the InboundMailAdapter
     */
    private InboundImapMailAdapterImpl createAdapter(CorrelationManager correlationManager) {

        InboundImapMailAdapterImpl adapter = new InboundImapMailAdapterImpl(correlationManager, this);
        logger.debug("Registered mail adapter {}", adapter);
        return adapter;
    }

    @Override
    public CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar, CorrelationManager correlationService)
    throws AdapterSchedulingException {

        InboundPullAdapter adapter = createAdapter(correlationService);
        adapterRegistrar.registerPullAdapter(adapter);

        return adapter;
    }
}
