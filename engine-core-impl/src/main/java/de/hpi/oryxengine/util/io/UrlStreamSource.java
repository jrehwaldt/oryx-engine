package de.hpi.oryxengine.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * A stream source for input via an url access. Provides a wrapper for transparent access.
 */
public class UrlStreamSource implements StreamSource {

    private URL url;

    public UrlStreamSource(URL url) {

        this.url = url;
    }

    @Override
    public InputStream getInputStream() {

        try {
            return url.openStream();
        } catch (IOException e) {
            String errorMessage = "The URL '" + url + "' could not be opened.";
            throw new DalmatinaRuntimeException(errorMessage, e);
        }
    }

    @Override
    public String getName() {

        return url.toString();
    }

    @Override
    public String toString() {
    
        return getName();
    }
}
