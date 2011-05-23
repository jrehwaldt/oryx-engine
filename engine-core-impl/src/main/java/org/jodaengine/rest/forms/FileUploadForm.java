package org.jodaengine.rest.forms;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * A form class for file uploads.
 */
public class FileUploadForm {
    
    private byte[] fileData;
    
    public byte[] getFileData() {
        return fileData;
    }
    
    @FormParam("filedata")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public void setFileData(final byte[] fileData) {
        this.fileData = fileData;
    }

}
