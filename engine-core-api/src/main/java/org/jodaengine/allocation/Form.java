package org.jodaengine.allocation;

import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 * The form represents a graphical user interface for task.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Form {

    /**
     * Retrieves da content of the form in the HTML format for the rendering on the web.
     * 
     * @return a {@link String} representing the form content in the HTML format
     */
    String getFormContentAsHTML(); 
    
    /**
     * Gets a form field with the specified name.
     *
     * @param fieldName the field name
     * @return the form field
     */
    JodaFormField getFormField(String fieldName);
    
    /**
     * Gets all contained form fields.
     *
     * @return the form fields
     */
    List<JodaFormField> getFormFields();
}
