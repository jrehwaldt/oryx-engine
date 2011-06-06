package org.jodaengine.deployment.importer.archive;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Imports classes from a .dar-file, that are located in the /classes directory and any sub-directories.
 * 
 * These classes are imported as Resources for the process, such as custom classes, that can then be used in Script
 * Tasks, Forms, etc.
 */
public class ClassImportHandler extends AbstractDarHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String CLASSES_SUBDIR = "classes" + DELIMITER;
    private static final String CLASS_ENDING = ".class";

    @Override
    public void processSingleDarFileEntry(ZipFile darFile, ZipEntry entry, DeploymentBuilder builder) {

        if (entry.getName().startsWith(CLASSES_SUBDIR) && !entry.isDirectory()) {
            try {
                byte[] classData = new byte[(int) entry.getSize()];
                BufferedInputStream inputStream = new BufferedInputStream(darFile.getInputStream(entry));
                inputStream.read(classData);

                int prefixLength = CLASSES_SUBDIR.length();
                int suffixLength = entry.getName().length() - CLASS_ENDING.length();
                String fullClassName = entry.getName().substring(prefixLength, suffixLength);
                fullClassName = fullClassName.replace(DELIMITER, '.');
                builder.addClass(fullClassName, classData);

                logger.info("deployed class {}", fullClassName);
                inputStream.close();
            } catch (IOException e) {
                logger.error("Could not read file {} from archive", entry.getName());
            }
        }

    }

}
