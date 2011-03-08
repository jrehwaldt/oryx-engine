package de.hpi.oryxengine.process.instance;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/**
 * The Class ProcessInstanceContextImplTest.
 */
public class ProcessInstanceContextImplTest {

    /** The list. */
    ArrayList<String> list;

    /** The list2. */
    ArrayList<String> list2;

    /**
     * F.
     */
    @Test
    public void f() {

        list.removeAll(list2);
        assertEquals(list.size(), 1);
    }

    /**
     * Before class.
     */
    @BeforeClass
    public void beforeClass() {

        String a = "a";
        String b = "b";

        list = new ArrayList<String>();
        list2 = new ArrayList<String>();

        list.add(a);
        list.add(a);
        list.add(b);
        list2.add(a);
        list2.add(b);
    }

    /**
     * After class.
     */
    @AfterClass
    public void afterClass() {

    }

}
