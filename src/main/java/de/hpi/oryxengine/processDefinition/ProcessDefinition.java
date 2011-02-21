package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public interface ProcessDefinition {
	
  // TODO [Gerardo] die Erstellung einer Prozessinstanz sollte doch die Prozessdefinition selbst übernehmen, oder??
  
	//void setTransition(Node start, Node end);
	void setStartNodes(ArrayList<NodeImpl> n);
	
	String getID();
	void setID(String s);

}
