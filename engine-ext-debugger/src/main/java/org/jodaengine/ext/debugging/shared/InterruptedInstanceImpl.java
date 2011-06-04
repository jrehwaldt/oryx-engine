package org.jodaengine.ext.debugging.shared;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.Interrupter;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.debugging.rest.DereferencedObjectException;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a container class for interrupted {@link Token}s,
 * which were matched by a {@link Breakpoint}.
 * 
 * It, furthermore, implements the {@link Interrupter}, which interrupts and signals
 * the {@link DebuggerTokenListener}. It therefore uses the Java Concurrent classes.
 * 
 * @see http://www.ibm.com/developerworks/java/library/j-jtp11234/
 * @see http://download.oracle.com/javase/tutorial/essential/concurrency/
 * @see http://stackoverflow.com/questions/184147/countdownlatch-vs-semaphore
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public final class InterruptedInstanceImpl implements InterruptedInstance, Interrupter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final UUID id;
    
    private final Token interruptedToken;
    private final Breakpoint causingBreakpoint;
    
    private transient final DebuggerTokenListener interruptingListener;
    private transient final CountDownLatch latch;
    private transient DebuggerCommand command;
    
    /**
     * Default constructor.
     * 
     * @param interruptedToken the {@link Token}, which was interrupted
     * @param causingBreakpoint the {@link Breakpoint}, which caused the interruption
     * @param interruptingListener the {@link DebuggerTokenListener}, which triggered the interruption
     */
    public InterruptedInstanceImpl(@Nonnull Token interruptedToken,
                                   @Nonnull Breakpoint causingBreakpoint,
                                   @Nonnull DebuggerTokenListener interruptingListener) {
        
        this.interruptedToken = interruptedToken;
        this.causingBreakpoint = causingBreakpoint;
        this.id = UUID.randomUUID();
        
        this.interruptingListener = interruptingListener;
        
        //
        // create our latch with count one
        // -> it needs to be decremented one time before all acquires will continue
        //
        // We use a CountDownLatch here (over a Semaphore), because we are interested
        // in a single-use, always-firing solution. The latch will directly fire, if it
        // already was released. 
        //
        this.latch = new CountDownLatch(1);
    }
    
    /**
     * Hidden deserialization constructor. This will not set the transient field.
     * 
     * @param interruptedToken the {@link Token}, which was interrupted
     * @param causingBreakpoint the {@link Breakpoint}, which caused the interruption
     * @param id the {@link UUID}
     */
    @JsonCreator
    protected InterruptedInstanceImpl(@JsonProperty("interruptedToken") Token interruptedToken,
                                      @JsonProperty("causingBreakpoint") Breakpoint causingBreakpoint,
                                      @JsonProperty("id") UUID id) {
        
        this.interruptedToken = interruptedToken;
        this.causingBreakpoint = causingBreakpoint;
        this.id = id;
        
        this.interruptingListener = null;
        this.latch = null;
    }
    
    //=================================================================
    //=================== InterruptedInstance methods =================
    //=================================================================
    
    @Override
    public UUID getID() {
        return this.id;
    }
    
    @Override
    public Token getInterruptedToken() {
        return interruptedToken;
    }
    
    @Override
    public Breakpoint getCausingBreakpoint() {
        return causingBreakpoint;
    }
    
    @Override
    public AbstractProcessInstance getInterruptedInstance() {
        return getInterruptedToken().getInstance();
    }
    
    //=================================================================
    //=================== Interrupter methods =========================
    //=================================================================
    
    @Override
    public DebuggerCommand interruptInstance() throws InterruptedException {
        
        logger.debug("Token {} is interrupted.", getInterruptedToken());
        
        //
        // wait until we get signaled...
        //
        this.latch.await();
        assert this.command != null;
        
        //
        // ...and return the signaled command
        //
        return this.command;
    }
    
    @Override
    public synchronized void releaseInstance(DebuggerCommand command) {
        
        logger.debug("Token {} is continued.", getInterruptedToken());
        
        //
        // keep the command...
        //
        this.command = command;
        
        //
        // ...and signal our latch to resume the interrupted instance
        //
        assert this.latch.getCount() == 1L;
        this.latch.countDown();
    }
    
    //=================================================================
    //=================== Implementation-specific methods =============
    //=================================================================
    
    /**
     * This is a transient field. Deserialized and serialized representations may not have
     * an instance and will cause an {@link DereferencedObjectException}.
     * 
     * @return the {@link DebuggerTokenListener}, which triggered the interruption
     */
    @JsonIgnore
    public @Nonnull DebuggerTokenListener getInterruptingListener() {
        
        if (this.interruptingListener == null) {
            throw new DereferencedObjectException(DebuggerTokenListener.class, getID());
        }
        
        return interruptingListener;
    }
    
    /**
     * Returns true if this instances is already released.
     * 
     * @return true, when released
     */
    public boolean isReleased() {
        return this.latch.getCount() == 0;
    }
    
    /**
     * Returns the state of this interrupted instance.
     * 
     * @return a String describing the state
     */
    private String getState() {
        if (this.latch.getCount() == 0) {
            return "released";
        }
        
        return "interrupted";
    }
    
// CHECKSTYLE:OFF
    @Override
    public int hashCode() {
        return this.id.hashCode()
            + 7 * this.causingBreakpoint.hashCode()
            + 7 * this.interruptedToken.hashCode();
    }
// CHECKSTYLE:ON
    
    /**
     * Two {@link InterruptedInstanceImpl}s are equal, if they are
     * <ul>
     * <li>
     *   a) of the same type
     * </li>
     * <li>
     * and
     *   b) all of their fields, except the {@link DebuggerTokenListener} are equal.
     * </li>
     * </ul>
     * 
     * @param object the other object
     * @return true, if both are equal
     */
    @Override
    public boolean equals(Object object) {
        
        //
        // will never be equal to null
        //
        if (object == null) {
            return false;
        }
        
        //
        // or to a non-InterruptedInstance instance
        //
        if (object instanceof InterruptedInstanceImpl) {
            InterruptedInstanceImpl instance = (InterruptedInstanceImpl) object;
            
            //
            // same id
            //
            if (!this.getID().equals(instance.getID())) {
                return false;
            }
            
            //
            // same breakpoint
            //
            if (!this.getCausingBreakpoint().equals(instance.getCausingBreakpoint())) {
                return false;
            }
            
            //
            // same token
            //
            if (!this.getInterruptedToken().equals(instance.getInterruptedToken())) {
                return false;
            }
            
            return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        return String.format(
            "InterruptedInstance [%s|%s, Breakpoint: %s, Token: %s]",
            getID(),
            getState(),
            getCausingBreakpoint(),
            getInterruptedToken());
    }
}
