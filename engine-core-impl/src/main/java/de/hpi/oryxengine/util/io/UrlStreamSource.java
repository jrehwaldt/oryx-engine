package de.hpi.oryxengine.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

// TODO [@Gerado] Javadoc should be added
public class UrlStreamSource implements StreamSource {

    URL url;

    public UrlStreamSource(URL url) {

        this.url = url;
    }

    public InputStream getInputStream() {

        try {
            
            return url.openStream();
        } catch (IOException e) {
            String errorMessage = "The URL '" + url + "' could not be opened.";
            throw new DalmatinaRuntimeException(errorMessage, e);
        }
    }

    @Override
    public String getType() {

        // TODO Auto-generated method stub
        return null;
    }
}
