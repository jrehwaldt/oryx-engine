package de.hpi.oryxengine.NavigatorTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.testng.annotations.*;
import org.mockito.Mockito.*;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.impl.NavigatorImpl;
import de.hpi.oryxengine.node.Node;
import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;


public class NavigatorTest{
	
	NavigatorImpl nav;
	NodeImpl node,node2;
	ProcessInstanceImpl processInstance;
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private PrintStream tmp;
	

	@BeforeClass
	public void setUp() throws Exception {
		
		tmp = System.out;
		System.setOut(new PrintStream(out));
		
		nav = new NavigatorImpl();
		
		AutomatedDummyActivity activity = new AutomatedDummyActivity("test");
		node = new NodeImpl(activity);
		node2 = new NodeImpl(activity);
		node.transitionTo(node2);
		ArrayList<Node> startNodes = new ArrayList<Node>();
		startNodes.add(node);
		processInstance = new ProcessInstanceImpl(startNodes);

	}
	
	@Test
	public void testSignalLength(){

		nav.startArbitraryInstance("1", processInstance);
		assert processInstance.getCurrentNode().equals(node2);

	}
	
//	@Test
//	public void testSignalPrint(){
//		nav.startArbitraryInstance("1", processInstance);
//
//		assert "test\ntest".equals(out.toString().trim());
//	}
	
	@AfterClass
	public void tearDown(){
		System.setOut(tmp);
	}

}
