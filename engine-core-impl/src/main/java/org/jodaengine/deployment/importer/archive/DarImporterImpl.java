package org.jodaengine.deployment.importer.archive;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;

/**
 * The Class DarImporterImpl realizes the DarImporter interface with a chain of responsibility for the different file
 * types.
 */
public class DarImporterImpl implements DarImporter {

    private RepositoryService repo;
    private AbstractDarHandler firstHandler;

    /**
     * Instantiates a new dar importer impl and uses the {@link DeploymentBuilder} from the supplied
     * {@link RepositoryService}.
     * 
     * @param service
     *            the service
     */
    public DarImporterImpl(RepositoryService service) {

        this.repo = service;
        this.firstHandler = new ProcessDefinitionHandler();
    }

    @Override
    public Deployment importDarFile(File file) {

        DeploymentBuilder builder = repo.getDeploymentBuilder();

        ZipFile darFile;
        try {
            darFile = new ZipFile(file);
            firstHandler.processDarFile(darFile, builder);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        // put it through the chain of responsibility

        return builder.buildDeployment();
    }

}
