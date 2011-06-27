package org.jodaengine.process.structure;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The implementation of the {@link ControlFlowBuilder}.
 */
public class ControlFlowBuilderImpl implements ControlFlowBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Condition condition;
    private Node source, destination;
    private String id;

    @Override
    public ControlFlowBuilder controlFlowGoesFromTo(Node source, Node destination) {

        this.source = source;
        this.destination = destination;

        return this;
    }

    @Override
    public ControlFlowBuilder setCondition(Condition condition) {

        this.condition = condition;

        return this;
    }

    @Override
    public ControlFlow buildControlFlow() {

        checkingControlFlowConstraints();

        return buildResultControlFlow();
    }

    /**
     * Builds the ControlFlow to be retrieved. This method encapsulates the creation of the {@link ControlFlow}.
     * 
     * @return the {@link ControlFlow} to be retrieved
     */
    private ControlFlow buildResultControlFlow() {

        ControlFlow resultControlFlow;

        if (condition != null) {
            if (id != null) {
                resultControlFlow = source.controlFlowToWithCondition(id, destination, condition);
            } else {
                resultControlFlow = source.controlFlowToWithCondition(destination, condition);
            }
        } else {
            if (id != null) {
                resultControlFlow = source.controlFlowTo(id, destination);
            } else {
                resultControlFlow = source.controlFlowTo(destination);
            }
        }

        return resultControlFlow;
    }

    /**
     * This method checks the constraints for creating a node.
     */
    private void checkingControlFlowConstraints() {

        if (source == null || destination == null) {

            String errorMessage = "Both Source and Destination need to be set."
                + "Perform setActivityBlueprintFor(...) before.";

            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }
    }

    @Override
    public ControlFlowBuilder setId(String id) {

        this.id = id;
        return this;
    }
}
