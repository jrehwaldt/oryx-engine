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
 */
public class ClassImportHandler extends AbstractDarHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String CLASSES_SUBDIR = "classes/";
    private static final String CLASS_ENDING = ".class";

    @Override
    public void processSingleDarFileEntry(ZipFile darFile, ZipEntry entry, DeploymentBuilder builder) {

        // TODO remove this when implementation is finished
        System.out.println(entry.getName());

        if (entry.getName().startsWith(CLASSES_SUBDIR) && !entry.isDirectory()) {
            try {
                byte[] classData = new byte[(int) entry.getSize()];
                BufferedInputStream inputStream = new BufferedInputStream(darFile.getInputStream(entry));
                inputStream.read(classData);
                
                String fullClassName = entry.getName().substring(CLASSES_SUBDIR.length(), entry.getName().length() - CLASS_ENDING.length());
                fullClassName = fullClassName.replace('/', '.');
                builder.addClass(fullClassName, classData);
                
                //inputStream.close();
            } catch (IOException e) {
                logger.error("Could not read file {} from archive", entry.getName());
            }
        }
        
    }

}
