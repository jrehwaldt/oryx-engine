package de.hpi.oryxengine.process.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.structure.TransitionBuilder;

/**
 * The implementation of the {@link TransitionBuilder}.
 */
public class TransitionBuilderImpl implements TransitionBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Condition condition;
    private Node source, destination;

    @Override
    public TransitionBuilder transitionGoesFromTo(Node source, Node destination) {

        this.source = source;
        this.destination = destination;

        return this;
    }

    @Override
    public TransitionBuilder setCondition(Condition condition) {

        this.condition = condition;

        return this;
    }

    @Override
    public Transition buildTransition() {

        checkingTransitionConstraints();

        return buildResultTransition();
    }

    /**
     * Builds the transition to be retrieved. This method encapsulates the creation of the {@link Transition}.
     * 
     * @return the {@link Transition} to be retrieved
     */
    private Transition buildResultTransition() {

        Transition resultTransition;

        if (condition != null) {
            resultTransition = source.transitionToWithCondition(destination, condition);
        } else {
            resultTransition = source.transitionTo(destination);
        }

        return resultTransition;
    }

    /**
     * This method checks the constraints for creating a node.
     */
    private void checkingTransitionConstraints() {

        if (source == null || destination == null) {

            String errorMessage = "Both Source and Destination need to be set."
                + "Perform setActivityBlueprintFor(...) before.";

            logger.error(errorMessage);
            throw new DalmatinaRuntimeException(errorMessage);
        }
    }
}
