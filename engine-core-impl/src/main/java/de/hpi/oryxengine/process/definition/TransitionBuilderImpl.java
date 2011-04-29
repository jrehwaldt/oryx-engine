package de.hpi.oryxengine.process.definition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.structure.TransitionBuilder;

/**
 * 
 * @author Gery
 *
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

        return getResultTransition();
    }

    private Transition getResultTransition() {

        Transition resultTransition;

        if (condition != null) {
            resultTransition = source.transitionToWithCondition(destination, condition);
        } else {
            resultTransition = source.transitionTo(destination);
        }

        return resultTransition;
    }

    private void checkingTransitionConstraints() {

        if (source == null || destination == null) {

            String errorMessage = "Both Source and Destination need to be set."
                + "Perform setActivityBlueprintFor(...) before.";

            logger.error(errorMessage);
            throw new DalmatinaRuntimeException(errorMessage);
        }
    }
}
