package org.jodaengine.resource.allocation;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.FormField;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.util.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;


/**
 * The implementation of the {@link Form Form Interface}.
 * 
 * This implementation receives a deployed {@link AbstractProcessArtifact ProcessArtifact}.
 */
public class FormImpl implements Form {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AbstractProcessArtifact processArtifact;
    private Map<String, FormField> formFields;
    
    /**
     * Instantiates a new form impl. with a process artifact.
     *
     * @param processArtifact the process artifact
     */
    public FormImpl(@Nonnull AbstractProcessArtifact processArtifact) {

        this.processArtifact = processArtifact;
        this.formFields = new HashMap<String, FormField>();
    }
    
    @Override
    public String getFormContentAsHTML() {

        try {
            
            return IoUtil.convertStreamToString(processArtifact.getInputStream());
            
        } catch (IOException ioException) {
          
            String errorMessage = "The InputStream '" + processArtifact + "' could not be converted into a String.";
            logger.error(errorMessage, ioException);
            throw new JodaEngineRuntimeException(errorMessage, ioException);
        }
    }

    @Override
    public void addFormField(FormField field) {

        this.formFields.put(field.getName(), field);
        
    }

    @Override
    public List<FormField> getFormFields() {

        return Lists.newArrayList(formFields.values());
    }

    @Override
    public FormField getFormField(String fieldName) {
        return formFields.get(fieldName);
    }
}
