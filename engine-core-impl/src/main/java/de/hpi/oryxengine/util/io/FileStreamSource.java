package de.hpi.oryxengine.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * A stream source for input via a {@link File}. Provides a wrapper for transparent access.
 */
public class FileStreamSource implements StreamSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private File file;

    public FileStreamSource(File file) {

        if (!file.exists()) {
            String errorMessage = "The file '" + file.getPath() + "' is not a file or does not exist.";
            logger.error(errorMessage);
            throw new DalmatinaRuntimeException(errorMessage);
        }
            
        this.file = file;
        
    }

    public FileStreamSource(String filePathName) {

        this(new File(filePathName));
    }

    @Override
    public String getName() {

        return "File" + file.getPath() + "]";
    }

    @Override
    public String toString() {

        return getName();
    }

    @Override
    public InputStream getInputStream() {

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {

            String errorMessage = "The file '" + getName() + "' is not a file or does not exist.";
            logger.error(errorMessage, e);
            throw new DalmatinaRuntimeException(errorMessage, e);
        }
    }
}