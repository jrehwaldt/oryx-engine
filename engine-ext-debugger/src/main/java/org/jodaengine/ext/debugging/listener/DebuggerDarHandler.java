package org.jodaengine.ext.debugging.listener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.archive.AbstractDarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an implementation for the {@link AbstractDarHandler},
 * which will process SVG process representations, if any available, when
 * a dar package is imported.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-26
 */
public class DebuggerDarHandler extends AbstractDarHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String SUBFOLDER = "debugger" + DELIMITER;
    
    @Override
    public void processSingleDarFileEntry(ZipFile darFile,
                                          ZipEntry entry,
                                          DeploymentBuilder builder) {
        
        if (!entry.isDirectory() && entry.getName().startsWith(SUBFOLDER)) {
            try {
                //
                // Mr. Guttenberg-Code
                //
                BufferedInputStream inputStream = new BufferedInputStream(darFile.getInputStream(entry));
                int lastDelimiter = entry.getName().lastIndexOf(DELIMITER);
                String svgName = entry.getName().substring(lastDelimiter + 1);
                builder.addInputStreamArtifact(SUBFOLDER + svgName, inputStream);
                
                inputStream.close();
            } catch (IOException e) {
                logger.error("Could not read file {} from archive", entry.getName());
            }
        }
    }

}