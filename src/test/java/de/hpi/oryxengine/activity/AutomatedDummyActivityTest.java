package de.hpi.oryxengine.activity;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hpi.oryxengine.activity.implementations.AutomatedDummyActivity;


public class AutomatedDummyActivityTest {
	
	private PrintStream tmp;
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private String s = "I'm dumb";
	private AutomatedDummyActivity a;
	
	@Before
	public void setUp() throws Exception {
		
		tmp = System.out;
		System.setOut(new PrintStream(out));
		a = new AutomatedDummyActivity(s);
	}
	
	@Test
	public void testActivityInitialization(){
		assertNotNull(a);
	}
	
	@Test
	public void testExecute(){
		a.execute();
		assertEquals(s, out.toString().trim());		
	}
	
	@After
	public void tearDown(){
		System.setOut(tmp);
	}
}
