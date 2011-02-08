package de.hpi.oryxengine.node;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Interface NodeInterface.
 */
public interface NodeInterface {
	
	/**
	 * Execute the activity of the node.
	 */
	void execute();
	
	/**
	 * Next.
	 *
	 * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes). 
	 */
	ArrayList<AbstractNode> next();

}
