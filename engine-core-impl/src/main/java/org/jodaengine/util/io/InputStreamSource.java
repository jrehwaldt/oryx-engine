package org.jodaengine.util.io;

import java.io.InputStream;

/**
 * A stream source for input streams. Provides a wrapper for transparent access.
 */
public class InputStreamSource implements StreamSource {

    private InputStream inputStream;

    public InputStreamSource(InputStream inputStream) {

        this.inputStream = inputStream;
    }
    
    @Override
    public InputStream getInputStream() {

        return inputStream;
    }

    @Override
    public String toString() {

        return getName();
    }

    @Override
    public String getName() {

        return "InputStream[" + inputStream.toString() + "]";
    }
}
