package de.hpi.oryxengine.repository.importer.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.util.xml.XmlElement;

/**
 * This ParseListener looks for some constraints in the XML file. It checks if the BPMN serialized XML file constians
 * the necessary elements.
 */
public class BpmnProcessDefinitionValidator implements BpmnXmlParseListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> lazyValidationErrorList;

    @Override
    public void parseProcess(XmlElement processXmlElement, ProcessDefinition processDefinition) {

        if (processXmlElement.getElements().isEmpty()) {
            String errorMessage = "The BPMN serialized XMl file contains no elements.";
            addValidationError(errorMessage, processXmlElement);
        }

        if (processXmlElement.getElements("startEvent").isEmpty()) {
            String errorMessage = "No startEvent is defined in the BPMN serialized XMl file.";
            addValidationError(errorMessage, processXmlElement);
        }

        throwValidationError();
    }

    @Override
    public void parseStartEvent(XmlElement startEventXmlElement, Node startNode) {

    }

    @Override
    public void parseExclusiveGateway(XmlElement exclusiveGatewayXmlElement, Node exclusiveGatewayNode) {

    }

    @Override
    public void parseParallelGateway(XmlElement parallelGatewayXmlElement, Node parallelGatewayNode) {

    }

    @Override
    public void parseTask(XmlElement taskXmlElement, Node taskNode) {

    }

    @Override
    public void parseEndEvent(XmlElement endEventXmlElemnt, Node endEventNode) {

    }

    private void addValidationError(String validationErrorMessage, XmlElement xmlElement) {

        getValidationErrorList().add(validationErrorMessage);
        String warningMessage = "The following validationError occurred while checking " + "'<"
            + xmlElement.getTagName() + " ... [" + xmlElement.getLine() + "]': \n" + validationErrorMessage;
        logger.warn(warningMessage);
    }

    private List<String> getValidationErrorList() {

        if (lazyValidationErrorList == null) {
            this.lazyValidationErrorList = new ArrayList<String>();
        }
        return lazyValidationErrorList;
    }

    private void throwValidationError() {

        if (getValidationErrorList().isEmpty()) {
            return;
        }

        String errorMessage = "The process definition is not valid. Following validationErrors occurred:\n";
        for (String theErrorMessage : getValidationErrorList()) {
            errorMessage = errorMessage.concat(theErrorMessage).concat("\n");
        }

        DalmatinaRuntimeException validationErrorException = new DalmatinaRuntimeException(errorMessage);
        logger.error(errorMessage, validationErrorException);
        throw validationErrorException;
    }
}
