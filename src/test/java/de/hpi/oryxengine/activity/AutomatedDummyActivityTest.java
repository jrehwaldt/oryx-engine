package de.hpi.oryxengine.activity;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.AbstractActivityImpl.State;
import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class AutomatedDummyActivityTest {

  private PrintStream tmp;
  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private String S = "I'm dumb";
  private AutomatedDummyActivity a;
  private ProcessInstance processInstance;

  @BeforeTest
  public void setUp() throws Exception {

    tmp = System.out;
    System.setOut(new PrintStream(out));
    a = new AutomatedDummyActivity(S);
    processInstance = new ProcessInstanceImpl(new NodeImpl(a));
  }

  @Test
  public void testActivityInitialization() {
    assertNotNull(a, "It should not be null since it should be instantiated correctly");
  }

  // @Test
  // public void testStateAfterActivityInitalization(){
  // assertEquals("It should have the state Initialized", State.INIT,
  // a.getState());
  // }

  @Test
  public void testExecuteOutput() {
    a.execute(processInstance);
    assertTrue(out.toString().indexOf(S) != -1, "It should print out the given string when executed");
  }

  @Test
  public void testStateAfterExecution() {
    a.execute(processInstance);
    assertEquals(a.getState(), State.TERMINATED, "It should have the state Initialized");
  }

  @AfterTest
  public void tearDown() {
    System.setOut(tmp);
  }
}
