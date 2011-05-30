package org.jodaengine.ext.debugging.listener;

import static org.jodaengine.ext.debugging.api.DebuggerArtifactService.DEBUGGER_ARTIFACT_NAMESPACE;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.archive.AbstractDarHandler;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.DebuggerService;
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
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class DebuggerDarHandler extends AbstractDarHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String SUBFOLDER = "debugger" + DELIMITER;
    
    @Override
    public void processSingleDarFileEntry(ZipFile darFile,
                                          ZipEntry entry,
                                          DeploymentBuilder builder) {
        
        if (!entry.isDirectory() && entry.getName().startsWith(SUBFOLDER)) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(darFile.getInputStream(entry));
                int lastDelimiter = entry.getName().lastIndexOf(DELIMITER);
                
                //
                // register the artifact in the correct namespace
                //
                String artifactName = DEBUGGER_ARTIFACT_NAMESPACE + entry.getName().substring(lastDelimiter + 1);
                logger.info("Register svg artifact {} in process scope", artifactName);
                builder.addInputStreamArtifact(artifactName, inputStream);
                
                //
                // do not close
                //
//                inputStream.close();
            } catch (IOException e) {
                logger.error("Could not read file {} from archive", entry.getName());
            }
        }
    }

}
