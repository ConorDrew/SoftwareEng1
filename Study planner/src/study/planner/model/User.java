package study.planner.model;
import external.PasswordAuthentication;
import java.io.Serializable;

/**
 * Authentication model for the Student
 * @author Adrian Wesolowski
 */
public final class User implements Serializable
{
    private final String userID;
    private final String hash;
    private final static PasswordAuthentication pass = new PasswordAuthentication();
    private final Student student;
    
    /**
     * Constructs the User object
     * @param userID
     * @param plaintext
     * @param student 
     */
    public User(String userID, String plaintext, Student student)
    {
        this.userID = userID;
        hash = pass.hash(plaintext.toCharArray());
        this.student = student;
    }
    /**
     * Gets the user ID
     * @return 
     */
    public String getUserID()
    {
        return userID;
    }
    /**
     * Attempts to authenticate the user logging in
     * @param userID
     * @param plaintext
     * @return associated Student object if successful, null if not
     */
    public Student authenticate(String userID, String plaintext)
    {
        if(!this.userID.equals(userID))
            return null;
        if(pass.authenticate(plaintext.toCharArray(), hash))
            return student;
        else
            return null;
    }
}
