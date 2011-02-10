package de.hpi.oryxengine.NavigatorTest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hpi.oryxengine.activity.implementations.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.impl.Navigator;
import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;


public class NavigatorTest{
	
	Navigator nav;
	NodeImpl node,node2;
	ProcessInstanceImpl processInstance;
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private PrintStream tmp;
	

	@Before
	public void setUp() throws Exception {
		
		tmp = System.out;
		System.setOut(new PrintStream(out));
		
		nav = new Navigator();
		
		AutomatedDummyActivity activity = new AutomatedDummyActivity("test");
		node = new NodeImpl(activity);
		node2 = new NodeImpl(activity);
		node.addNextNode(node2);
		ArrayList<NodeImpl> startNodes = new ArrayList<NodeImpl>();
		startNodes.add(node);
		processInstance = new ProcessInstanceImpl(startNodes);

	}
	
	@Test
	public void testSignalLength(){

		nav.signal(node);
		assertTrue(processInstance.getCurrentNodes().isEmpty());

	}
	
	@Test
	public void testSignalPrint(){
		
		nav.signal(node);
		assertEquals("test", out.toString().trim());
	}
	
	@Test
	public void testSignalSetOfInstance(){
		
		nav.signal(node);
		assertTrue(node2.getProcessInstance() == processInstance);
	}
	
	@After
	public void tearDown(){
		System.setOut(tmp);
	}

}
