package de.hpi.oryxengine.correlation.adapter.mail;

import javax.annotation.Nonnegative;

// TODO: Auto-generated Javadoc
/**
 * Mail type enum.
 * 
 * @see http://de.wikipedia.org/wiki/Imap
 * @see http://de.wikipedia.org/wiki/POP3
 */
public enum MailType {
    
    /** The PO p3. */
    POP3(110, 995), 
 /** The IMAP. */
 IMAP(143, 993);
    
    /** The default port. */
    private int defaultPort;
    
    /** The ssl port. */
    private int sslPort;
    
    /**
     * Enum constructor.
     * 
     * @param defaultPort default port
     * @param sslPort ssl port
     */
    private MailType(@Nonnegative int defaultPort,
                     @Nonnegative int sslPort) {
        this.defaultPort = defaultPort;
        this.sslPort = sslPort;
    }
    
    /**
     * Method providing default ports.
     * 
     * @param ssl ssl port vs. default port
     * @return the port
     */
    public @Nonnegative int getPort(boolean ssl) {
        if (ssl) {
            return this.sslPort;
        } else {
            return this.defaultPort;
        }
    }
}
