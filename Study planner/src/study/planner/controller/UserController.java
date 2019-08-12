package study.planner.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import study.planner.model.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for user authentication
 * @author Adrian Wesolowski
 */
public class UserController implements Serializable
{
    static final long serialVersionUID = 100L;
    private static ArrayList<User> users = new ArrayList<>();
    private User selectedUser;
    
    public UserController()
    {
        selectedUser = null;
    }
    
    public static boolean addUser(User u)
    {
        if(usernameAvailable(u.getUserID()))
            return users.add(u);
        else
            return false;
    }
    
    public static void loadUserData(String fileName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fis);
            User u = (User) in.readObject();
            users.add(u);
            in.close();
            fis.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(StudyPlannerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveStudentData()
    {
        try
        {
            String fileName = "user" + selectedUser.getUserID() + ".ser";
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(selectedUser);
            out.close();
            fos.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static boolean usernameAvailable(String userID)
    {
        for(User u : users)
        {
            if(u.getUserID().equals(userID))
                return false;
        }
        return true;
    }
    
    private boolean selectUser(User user)
    {
        if(users.contains(user))
        {
            selectedUser = user;
            return true;
        }
        else
            return false;
    }
    
    public Student authenticate(String userID, String password)
    {
        Student s = null;
        for(User u : users)
        {
            if(userID.equals(u.getUserID()))
            {
                s = u.authenticate(userID, password);              
            }
            if(s != null)
            {
                selectedUser = u;
                return s;
            }
        }
        return s;
    }
}
