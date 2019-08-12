package study.planner.model;

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
public class CountableIntegerTest {
    
    private CountableInteger testInt;
    
    public CountableIntegerTest() {

    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testInt = new CountableInteger(42);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setValue method, of class CountableInteger.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        int n = 58;
        CountableInteger instance = new CountableInteger();
        instance.setValue(n);
        assertEquals((Integer) n, instance.getValue());
    }

    /**
     * Test of getValue method, of class CountableInteger.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        CountableInteger instance = testInt;
        Integer expResult = 42;
        Integer result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class CountableInteger.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        int term = 67;
        CountableInteger instance = testInt;
        Integer expResult = 109;
        instance.add(term);
        Integer result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of subtract method, of class CountableInteger.
     */
    @Test
    public void testSubtract() {
        System.out.println("subtract");
        int sub = 24;
        CountableInteger instance = testInt;
        Integer expResult = 18;
        instance.subtract(sub);
        Integer result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of greaterOrEqual method, of class CountableInteger.
     */
    @Test
    public void testGreaterOrEqual() {
        System.out.println("greaterOrEqual");
        Integer cmp = 45;
        CountableInteger instance = testInt;
        boolean expResult = false;
        boolean result = instance.greaterOrEqual(cmp);
        assertEquals(expResult, result);
    }
}
