package org.jodaengine.deployment.importer.archive;

import java.io.File;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;

/**
 * The Class AbstractDarHandler that realizes a chain of responsibility for .dar-Files.
 */
public abstract class AbstractDarHandler {
    protected AbstractDarHandler next;
    
    /**
     * Sets the {@link AbstractDarHandler}, that is the following member of the chain of responsibility.
     *
     * @param handler the handler
     * @return the abstract dar handler
     */
    public AbstractDarHandler setNext(AbstractDarHandler handler) {
        this.next = handler;
        return next;
    }
    
    /**
     * Realizes the "chaining" of the process.
     *
     * @param darFile the dar file
     * @param builder the builder
     */
    public void processDarFile(ZipFile darFile, DeploymentBuilder builder) {
        readDarFileSpecifically(darFile, builder);
        if (next != null) {
            next.processDarFile(darFile, builder);
        }
    }
    
    /**
     * Implement this for the specific behaviour of your darFileHandler.
     *
     * @param darFile the dar file
     * @param builder the builder
     */
    public abstract void readDarFileSpecifically(ZipFile darFile, DeploymentBuilder builder);

}
