package org.jodaengine.ext.debugging.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * A RESTeasy helper class defining the multipart form data for the svg artifact upload.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-07
 */
public class SvgArtifactFormData {
    
    private byte[] svgArtifact;
    
    /**
     * byte[] getter.
     * @return the svg artifact
     */
    public byte[] getSvgArtifact() {
        return svgArtifact;
    }
    
    /**
     * String getter.
     * @return the svg artifact
     */
    public String getSvgArtifactString() {
        return new String(svgArtifact);
    }
    
    /**
     * Setter.
     * @param data the svg artifact
     */
    @FormParam("svgArtifact")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public void setSvgArtifact(final byte[] data) {
        this.svgArtifact = data;
    }
}
