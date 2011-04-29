package de.hpi.oryxengine.resource.allocation;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.definition.AbstractProcessArtifact;
import de.hpi.oryxengine.util.IoUtil;

/**
 * The implementation of the {@link Form Form Interface}.
 * 
 * This implementation receives a deployed {@link AbstractProcessArtifact ProcessArtifact}.
 */
public class FormImpl implements Form {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AbstractProcessArtifact processArtifact;
    
    /**
     * Instantiates a new form impl. with a process artifact.
     *
     * @param processArtifact the process artifact
     */
    public FormImpl(@Nonnull AbstractProcessArtifact processArtifact) {

        this.processArtifact = processArtifact;
    }
    
    @Override
    public String getFormContentAsHTML() {

        try {
            
            return IoUtil.convertStreamToString(processArtifact.getInputStream());
            
        } catch (IOException ioException) {
          
            String errorMessage = "The InputStream '" + processArtifact + "' could not be converted into a String.";
            logger.error(errorMessage, ioException);
            throw new DalmatinaRuntimeException(errorMessage, ioException);
        }
    }
}
