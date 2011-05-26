package org.jodaengine.deployment.importer.archive;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.definition.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ProcessDefinitionHandler reads ProcessDefinitions from the .dar-File and adds them to the
 * {@link DeploymentBuilder}.
 */
public class ProcessDefinitionHandler extends AbstractDarHandler {

    private BpmnXmlParseListener[] parseListeners;

    /**
     * This constructor is used to load {@link BpmnXmlParseListener}s that were marked as extensions.
     * 
     * @param extensionService
     *            the service that provides the listeners
     */
    public ProcessDefinitionHandler(ExtensionService extensionService) {

        parseListeners = new BpmnXmlParseListener[0];
        if (extensionService != null) {
            List<BpmnXmlParseListener> parseListenersList = extensionService.getExtensions(BpmnXmlParseListener.class);
            parseListeners = parseListenersList.toArray(parseListeners);
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DEFINITIONS_SUBDIR = "definitions/";

    @Override
    public void processSingleDarFileEntry(ZipFile darFile, ZipEntry entry, DeploymentBuilder builder) {

        // if an entry is located in the definitions folder, it is treated as a process definition.
        // We could check here for the file ending instead of the folder.
        if (entry.getName().startsWith(DEFINITIONS_SUBDIR) && !entry.isDirectory()) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(darFile.getInputStream(entry));
                ProcessDefinitionImporter processDefinitionImporter = new BpmnXmlImporter(inputStream, parseListeners);
                ProcessDefinition definition = processDefinitionImporter.createProcessDefinition();
                builder.addProcessDefinition(definition);
                inputStream.close();
            } catch (IOException e) {
                logger.error("Could not read file {} from archive", entry.getName());
            }
        }

    }

}
