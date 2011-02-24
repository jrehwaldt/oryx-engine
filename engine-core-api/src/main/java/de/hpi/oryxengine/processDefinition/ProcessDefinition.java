package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.processstructure.Node;

public interface ProcessDefinition {

    // TODO [Gerardo] die Erstellung einer Prozessinstanz sollte doch die Prozessdefinition selbst übernehmen, oder??

    // void setTransition(Node start, Node end);
    void setStartNodes(ArrayList<Node> n);

    String getID();

    void setID(String s);

}
