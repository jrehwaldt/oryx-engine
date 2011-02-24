package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.processDefinition.ProcessDefinition;
import de.hpi.oryxengine.processstructure.Node;

public class AbstractProcessDefinitionImpl implements ProcessDefinition {

    // TODO [Gerardo] Was ist denn hieran abstrakt

<<<<<<< HEAD:src/main/java/de/hpi/oryxengine/processDefinitionImpl/AbstractProcessDefinitionImpl.java
    private ArrayList<NodeImpl> startNodes;
    private String id;
=======
    protected ArrayList<Node> startNodes;
    protected String id;
>>>>>>> remotes/origin/modular-maven-setup:engine-core-impl/src/main/java/de/hpi/oryxengine/processDefinition/AbstractProcessDefinitionImpl.java

    public void setStartNodes(ArrayList<Node> nodes) {

        this.startNodes = nodes;
    }

    public ArrayList<Node> getStartNodes() {

        return startNodes;
    }

    public String getID() {

        return id;
    }

    public void setID(String s) {

        this.id = s;
    }

}
