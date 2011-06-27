package org.jodaengine.ext.debugging.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.shared.DebuggerInstanceAttribute;
import org.jodaengine.ext.debugging.util.DebuggerInstanceAttributeKeyProvider;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the proper function of {@link SplitJoinListener}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
public class SplitJoinListenerTest extends AbstractJodaEngineTest {
    
    private Collection<Token> splitIncoming = null;
    private Collection<Token> splitOutgoing = null;
//    private Collection<Token> joinIncoming = null;
    private Collection<Token> joinOutgoing = null;
    
    private Token mockTriggeringToken = null;
    private Node mockNode = null;
    private DebuggerInstanceAttribute mockAttribute = null;
    
    /**
     * Setup.
     */
    @BeforeMethod
    public void setUp() {
        this.mockAttribute = mock(DebuggerInstanceAttribute.class);
        this.mockTriggeringToken = mock(Token.class);
        this.mockNode = mock(Node.class);
        
        when(this.mockTriggeringToken.getAttribute(
            DebuggerInstanceAttributeKeyProvider.getAttributeKey())).thenReturn(this.mockAttribute);
        when(this.mockAttribute.getCommand(this.mockTriggeringToken)).thenReturn(DebuggerCommand.STEP_OVER);
        
        this.splitIncoming = Arrays.asList(this.mockTriggeringToken);
        this.splitOutgoing = Arrays.asList(mock(Token.class), mock(Token.class), mock(Token.class));
        
//        this.joinIncoming = Arrays.asList(mock(Token.class), mock(Token.class), mock(Token.class));
        this.joinOutgoing = Arrays.asList(mock(Token.class));
    }
    
    /**
     * Tests the split method.
     * 
     * It should preserve the {@link DebuggerInstanceAttribute}.
     */
    @Test
    public void testSplitWillPreserveAttribute() {
        
        SplitJoinListener listener = new SplitJoinListener();
        listener.splitPerformed(
            this.mockTriggeringToken,
            this.mockNode,
            this.splitIncoming,
            this.splitOutgoing);
        
        for (Token outgoingToken: this.splitOutgoing) {
            verify(outgoingToken, times(1)).setAttribute(
                DebuggerInstanceAttributeKeyProvider.getAttributeKey(),
                this.mockAttribute);
        }
    }
    
    /**
     * Tests the join method.
     * 
     * It should preserve the {@link DebuggerCommand}.
     */
    @Test
    public void testJoinWillPreserveCommand() {
        
        SplitJoinListener listener = new SplitJoinListener();
        listener.joinPerformed(
            this.mockTriggeringToken,
            this.mockNode,
            this.joinOutgoing);
        
        for (Token outgoingToken: this.joinOutgoing) {
            
            //
            // if no attribute was available, the parent attribute is set
            //
            if (!outgoingToken.equals(this.mockTriggeringToken)) {
                verify(outgoingToken, times(1)).setAttribute(
                    DebuggerInstanceAttributeKeyProvider.getAttributeKey(),
                    this.mockAttribute);
            }
            
            //
            // and the parent command was set for this token
            //
            verify(this.mockAttribute).setCommand(
                outgoingToken,
                this.mockAttribute.getCommand(this.mockTriggeringToken));
        }
    }
}
