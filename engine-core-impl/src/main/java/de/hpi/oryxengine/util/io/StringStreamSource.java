package de.hpi.oryxengine.util.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * The source of this {@link StreamSource} is a String. By calling {@link #getInputStream() getInputStream} this class
 * will retrieve the string a {@link ByteArrayInputStream}.
 */
public class StringStreamSource implements StreamSource {

    String string;

    /**
     * Instantiates a new {@link StringStreamSource}.
     *
     * @param string - the {@link String} that represents the {@link StreamSource}
     */
    public StringStreamSource(String string) {

        this.string = string;
    }

    @Override
    public String toString() {

        return "String";
    }

    @Override
    public InputStream getInputStream() {

        return new ByteArrayInputStream(string.getBytes());
    }

    @Override
    public String getType() {

        return "String";
    }
}
