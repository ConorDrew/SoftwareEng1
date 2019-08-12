package study.planner.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * User model unit test
 * @author Adrian Wesolowski
 */
public class UserTest {
    
    private User testUser;
    private Student testStudent;
    
    public UserTest() {
        testUser = new User("tes18tte", "testpass", testStudent = new Student("Random Name", "123456789"));
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getUserID method, of class User.
     */
    @Test
    public void testGetUserID() {
        System.out.println("getUserID");
        User instance = testUser;
        String expResult = "tes18tte";
        String result = instance.getUserID();
        assertEquals(expResult, result);
    }

    /**
     * Test of authenticate method, of class User.
     */
    @Test
    public void testAuthenticate() {
        System.out.println("authenticate");
        String userID = "tes18tte";
        String plaintext = "testpass";
        User instance = testUser;
        Student expResult = testStudent;
        Student result = instance.authenticate(userID, plaintext);
        assertEquals(expResult, result);
    }
    
}
