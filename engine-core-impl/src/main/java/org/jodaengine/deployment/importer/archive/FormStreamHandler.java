package org.jodaengine.deployment.importer.archive;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers all process artifacts that are located in the form-directory. Uses the fileName as the resource identifier.
 */
public class FormStreamHandler extends AbstractDarHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String FORMS_SUBDIR = "forms/";
    private static final String DELIMITER = "/";

    @Override
    public void processSingleDarFileEntry(ZipFile darFile, ZipEntry entry, DeploymentBuilder builder) {

        // TODO remove this when implementation is finished
        System.out.println(entry.getName());

        if (entry.getName().startsWith(FORMS_SUBDIR) && !entry.isDirectory()) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(darFile.getInputStream(entry));
                int lastDelimiter = entry.getName().lastIndexOf(DELIMITER);
                String formName = entry.getName().substring(lastDelimiter + 1);
                builder.addInputStreamArtifact(formName, inputStream);
                
                //inputStream.close();
            } catch (IOException e) {
                logger.error("Could not read file {} from archive", entry.getName());
            }
        }

    }

}
