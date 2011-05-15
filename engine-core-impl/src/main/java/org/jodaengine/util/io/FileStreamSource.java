package org.jodaengine.util.io;

import org.jodaengine.exception.JodaEngineRuntimeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A stream source for input via a {@link File}. Provides a wrapper for transparent access.
 */
public class FileStreamSource implements StreamSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private File file;

    /**
     * Instantiates a new file stream source, thereby checking that the file consists.
     *
     * @param file the file
     */
    public FileStreamSource(File file) {

        if (!file.exists()) {
            String errorMessage = "The file '" + file.getPath() + "' is not a file or does not exist.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }
            
        this.file = file;
        
    }

    /**
     * Instantiates a new file stream source, thereby checking that the file consists.
     *
     * @param filePathName the file path name
     */
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
            throw new JodaEngineRuntimeException(errorMessage, e);
        }
    }
}
