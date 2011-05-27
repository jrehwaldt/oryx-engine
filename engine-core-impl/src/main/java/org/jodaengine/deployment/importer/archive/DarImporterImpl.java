package org.jodaengine.deployment.importer.archive;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.ext.service.ExtensionService;

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

        this(service, null);
    }
    
    /**
     * Instantiates a new dar importer impl and uses the {@link DeploymentBuilder} from the supplied
     * {@link RepositoryService}.
     * 
     * @param repositoryService
     *            the {@link RepositoryService}
     * @param extensionService
     *            the {@link ExtensionService}
     */
    public DarImporterImpl(RepositoryService repositoryService,
                           ExtensionService extensionService) {

        this.repo = repositoryService;
        this.firstHandler = new ProcessDefinitionHandler(extensionService);
        this.firstHandler.setNext(new FormStreamHandler()).setNext(new ClassImportHandler());
        
        //
        // add any available DarHandler extensions
        //
        if (extensionService != null) {
            List<AbstractDarHandler> handlers = extensionService.getExtensions(AbstractDarHandler.class);
            
            for (AbstractDarHandler handler: handlers) {
                this.firstHandler.addLast(handler);
            }
        }
        
    }

    @Override
    public Deployment importDarFile(File file) {

        DeploymentBuilder builder = repo.getDeploymentBuilder();

        ZipFile darFile;
        try {
            darFile = new ZipFile(file);
            // put it through the chain of responsibility
            firstHandler.processDarFile(darFile, builder);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return builder.buildDeployment();
    }

}
