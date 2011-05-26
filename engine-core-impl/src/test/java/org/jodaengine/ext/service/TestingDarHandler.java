package org.jodaengine.ext.service;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.archive.AbstractDarHandler;
import org.jodaengine.ext.Extension;

/**
 * This implementation tests the availability of a {@link AbstractDarHandler} implementation
 * via the {@link ExtensionService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-26
 */
@Extension(TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME + "-dar-listener")
public final class TestingDarHandler extends AbstractDarHandler {

    private boolean processed = false;

    @Override
    public void processSingleDarFileEntry(ZipFile darFile, ZipEntry entry, DeploymentBuilder builder) {
        this.processed = true;
    }

    /**
     * Was this handler called to process.
     * 
     * @return true, if processSingleDarFileEntry() was invoked
     */
    public boolean isProcessed() {
        return processed;
    }
}
