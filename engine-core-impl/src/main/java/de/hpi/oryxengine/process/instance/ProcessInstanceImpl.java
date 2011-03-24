package de.hpi.oryxengine.process.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class ProcessInstanceImpl. See {@link ProcessInstance}
 */
public class ProcessInstanceImpl implements ProcessInstance {

    private ProcessDefinition definition;
    private ProcessInstanceContext context;
    private UUID id;
    private List<Token> assignedTokens;

    public ProcessInstanceImpl(ProcessDefinition definition) {

        this.definition = definition;
        this.id = UUID.randomUUID();
        this.assignedTokens = new ArrayList<Token>();
        this.context = new ProcessInstanceContextImpl();
    }

    @Override
    public void addToken(Token t) {

        this.assignedTokens.add(t);

    }

    @Override
    public ProcessInstanceContext getContext() {

        return context;
    }

    @Override
    public List<Token> getTokens() {

        return assignedTokens;
    }

    @Override
    public ProcessDefinition getDefinition() {

        return definition;
    }

    @Override
    public UUID getID() {

        return id;
    }

    @Override
    public Token createToken(Node node, Navigator nav) {

        Token newToken = new TokenImpl(node, this, nav);
        this.assignedTokens.add(newToken);
        return newToken;
    }

}
