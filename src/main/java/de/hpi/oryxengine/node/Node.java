package de.hpi.oryxengine.node;

import java.util.ArrayList;

import de.hpi.oryxengine.navigator.Navigator;

// TODO: Auto-generated Javadoc
/**
 * The Interface NodeInterface.
 */
public interface Node {
	
	/**
	 * Execute the activity of the node.
	 */
	void execute(Navigator navigator);
	
	/**
	 * Next.
	 *
	 * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes). 
	 */
	ArrayList<NodeImpl> getNextNodes();

}
