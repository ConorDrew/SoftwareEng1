package study.planner.model;

import java.time.Duration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test class for CountableDuration
 * @author Adrian Wesolowski
 */
public class CountableDurationTest {
    
    private CountableDuration testDur;
    
    public CountableDurationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testDur = new CountableDuration(Duration.ofMinutes(30));
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class CountableDuration.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        CountableDuration instance = testDur;
        Duration expResult = Duration.ofMinutes(30);
        Duration result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of setValue method, of class CountableDuration.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        int n = 5;
        CountableDuration instance = testDur;
        instance.setValue(n);
        assertEquals(Duration.ofMinutes(n), instance.getValue());
    }

    /**
     * Test of add method, of class CountableDuration.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        int term = 15;
        CountableDuration instance = testDur;
        Duration expResult = Duration.ofMinutes(45);
        Duration result = instance.add(term);
        assertEquals(expResult, result);
    }

    /**
     * Test of subtract method, of class CountableDuration.
     */
    @Test
    public void testSubtract() {
        System.out.println("subtract");
        int sub = 20;
        CountableDuration instance = testDur;
        Duration expResult = Duration.ofMinutes(10);
        Duration result = instance.subtract(sub);
        assertEquals(expResult, result);
    }

    /**
     * Test of greaterOrEqual method, of class CountableDuration.
     */
    @Test
    public void testGreaterOrEqual() {
        System.out.println("greaterOrEqual");
        Duration cmp = Duration.ofMinutes(28);
        CountableDuration instance = testDur;
        boolean expResult = true;
        boolean result = instance.greaterOrEqual(cmp);
        assertEquals(expResult, result);
    }
}
