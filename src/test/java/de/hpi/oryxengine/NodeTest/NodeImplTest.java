package de.hpi.oryxengine.NodeTest;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import static org.mockito.Mockito.*;

public class NodeImplTest {
	private NodeImpl node, node2;
	private ProcessInstanceImpl instance;
	
	@BeforeClass
	public void setUp() {
		// by default, mocked void methods do nothing. thats what we want :)
		Activity activity = mock(Activity.class);
		
		node = new NodeImpl(activity);
		node.setId("1");
		node2 = new NodeImpl(activity);
		node2.setId("2");
		node.transitionTo(node2);
	}
	
	@BeforeTest
	public void setProcessInstance() {
		instance = simpleInstance();
	}
	
	@Test
	public void testInstancePromotion() {
		node.navigate(instance);
		
		assertEquals(instance.getCurrentNode(), node2);		
	}
	
	@Test
	public void testNewInstancesToNavigate() {
		List<ProcessInstance> instancesToNavigate = node.navigate(instance);
		
		assertTrue(instancesToNavigate.contains(instance));
		assertEquals(instancesToNavigate.size(), 1);
	}
	
	@AfterClass
	public void tearDown() {
		
	}
	
	private ProcessInstanceImpl simpleInstance() {		
		ArrayList<Node> startNodes = new ArrayList<Node>();
		startNodes.add(node);
		return new ProcessInstanceImpl(startNodes);
	}

}
