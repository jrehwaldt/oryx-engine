package de.hpi.oryxengine.correlation.adapter.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * Tests the imap mail adapter.
 * 
 * @author Jan Rehwaldt
 */
public class ImapMailAdapterTest {
    
    private PullingInboundAdapter imap = null;
    private MailAdapterConfiguration config = null;
    
    private CorrelationManager mock = null;
    
    /**
     * Setup test case.
     */
    @BeforeTest
    public void setUp() {
        this.mock = mock(CorrelationManager.class);
        this.config = new MailAdapterConfiguration(
            MailType.IMAP,
            "oryxengine@googlemail.com",
            "dalmatina!",
            "imap.gmail.com",
            MailType.IMAP.getPort(true),
            true
        );
        this.imap = new InboundImapMailAdapterImpl(this.mock, this.config);
    }
    
    /**
     * Tests a imap pull.
     * @throws Exception if thrown, test fails
     */
    @Test
    public void testImapPull() throws Exception {
        this.imap.pull();
        verify(this.mock).correlate(null);
    }
}
