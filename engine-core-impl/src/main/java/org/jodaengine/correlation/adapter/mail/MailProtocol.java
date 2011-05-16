package org.jodaengine.correlation.adapter.mail;

import javax.annotation.Nonnegative;

/**
 * Mail type enum.
 * 
 * @see http://de.wikipedia.org/wiki/Imap
 * @see http://de.wikipedia.org/wiki/POP3
 */
public enum MailProtocol {
    
    POP3(110, 995), 
    IMAP(143, 993);
    
    private int defaultPort;
    private int sslPort;
    
    /**
     * Enum constructor.
     * 
     * @param defaultPort default port
     * @param sslPort ssl port
     */
    private MailProtocol(@Nonnegative int defaultPort,
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
