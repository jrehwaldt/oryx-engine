package org.jodaengine.resource.allocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.jodaengine.allocation.AbstractForm;
import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.util.io.IoUtil;
import org.jodaengine.util.io.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Lists;

/**
 * The implementation of the {@link Form Form Interface}.
 * 
 * This implementation receives a deployed {@link AbstractProcessArtifact ProcessArtifact}.
 */
public class FormImpl extends AbstractForm {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AbstractProcessArtifact processArtifact;
    private Map<String, JodaFormField> jodaFormFields;
    private String id;
    private StreamSource streamSource;
    private String formContent = null;

    /**
     * Instantiates a new form impl. with a process artifact. Reads all "joda:"-Attributes and builds the formField
     * objects out of it.
     * 
     * @param name
     *            the name
     * @param streamSource
     *            the stream source
     */
    public FormImpl(String name, StreamSource streamSource) {

        this.id = name;
        this.streamSource = streamSource;
        this.jodaFormFields = new HashMap<String, JodaFormField>();
        parseToFormFields();
    }

    @Override
    public String getFormContentAsHTML() {

        if (formContent == null) {
            this.formContent = readContentFromStream();
        }
        return formContent;
    }

    private String readContentFromStream() {

        try {

            return IoUtil.convertStreamToString(getInputStream());

        } catch (IOException ioException) {

            String errorMessage = "The InputStream '" + processArtifact + "' could not be converted into a String.";
            logger.error(errorMessage, ioException);
            throw new JodaEngineRuntimeException(errorMessage, ioException);
        }
    }

    /**
     * Parses the form fields to the internal representation and removes joda-Attributes.
     */
    private void parseToFormFields() {

        Config.CurrentCompatibilityMode.setFormFieldNameCaseInsensitive(false);
        formContent = getFormContentAsHTML();
        Source source = new Source(formContent);
        FormFields formFields = source.getFormFields();
        JodaFormFieldConverter converter = new JodaFormFieldConverter();
        OutputDocument document = new OutputDocument(source);

        for (FormField field : formFields) {
            StartTag tag = field.getFormControl().getFirstStartTag();
            Attributes attributes = tag.getAttributes();

            JodaFormField formField = converter.convert(attributes);
            converter.cleanJodaAttributes(attributes, document);
            this.jodaFormFields.put(field.getName(), formField);
        }
        
        // set content to the actual one wihtout joda-attributes
        formContent = document.toString();
        
        
    }

    @Override
    public List<JodaFormField> getFormFields() {

        return Lists.newArrayList(jodaFormFields.values());
    }

    @Override
    public JodaFormField getFormField(String fieldName) {

        return jodaFormFields.get(fieldName);
    }

    @Override
    public String getID() {

        return this.id;
    }

    @Override
    public InputStream getInputStream() {

        return this.streamSource.getInputStream();
    }
}
