package study.planner.model;

import java.io.Serializable;

/**
 * Create notes for tasks
 * @author Conor Drew
 */
public class Note implements Serializable{
    private String message;

    /**
     * Constructor that makes a new Note
     * @param message The message that wants to be saved
     */
    public Note(String message) {
        this.message = message;
    }
    
    /**
     * Sets a message for Task
     * @param message Sets a string message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Returns the content of the note
     * @return message
     */
    public String getMessage()
    {
        return message;
    }
 
    
    /**
     * Returns the String representation of the note which is just the message.
     * Same as getMessage()
     * @return message
     */
    @Override
    public String toString()
    {
        return message;
    }
    
    
    
}
