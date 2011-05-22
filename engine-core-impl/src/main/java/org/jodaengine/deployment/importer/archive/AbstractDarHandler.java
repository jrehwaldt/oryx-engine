package org.jodaengine.deployment.importer.archive;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;

/**
 * The Class AbstractDarHandler that realizes a chain of responsibility for .dar-Files.
 */
public abstract class AbstractDarHandler {
    protected AbstractDarHandler next;
    protected static final String DELIMITER = File.separator;
    
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
     * Handles every single entry of the .dar-File.
     *
     * @param darFile the dar file
     * @param builder the builder
     */
    public void readDarFileSpecifically(ZipFile darFile, DeploymentBuilder builder) {


        Enumeration<? extends ZipEntry> darFileEntries = darFile.entries();
        
        while (darFileEntries.hasMoreElements()) {
            processSingleDarFileEntry(darFile, darFileEntries.nextElement(), builder);
        }
        

    }
    
    /**
     * Processes a single dar file entry. Implement this to do special file handling.
     *
     * @param darFile the dar file. It can be used to retrieve the {@link InputStream} for the specific {@link ZipEntry}
     * @param entry the entry of the dar File
     * @param builder the builder
     */
    public abstract void processSingleDarFileEntry(ZipFile darFile, ZipEntry entry, DeploymentBuilder builder);

}
