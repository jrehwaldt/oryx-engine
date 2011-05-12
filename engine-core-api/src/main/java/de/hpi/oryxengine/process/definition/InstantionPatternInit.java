package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;

public interface InstantionPatternInit {

    InstantionPatternInit init(CorrelationManager correlationManager,
                               NavigatorInside navigator,
                               ProcessDefinitionInside processDefinition);
}
