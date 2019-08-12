/**
 * Controller class for the Study Planner
 * @author Adrian Wesolowski & Andy Davies
 * @version 1.00
 */
package study.planner.controller;
//import java.io.FileInputStream;
//import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import study.planner.model.*;
import study.planner.view.*;

public class StudyPlannerController extends Application implements Serializable 
{
    static final long serialVersionUID = 100L;

    private static Student selectedStudent;
    private static UserController uc = new UserController();
    private static ViewController view;

    
    public static boolean authenticate(String userID, String password)
    {
        selectedStudent = uc.authenticate(userID, password);
        return selectedStudent != null;
    }
    public static Student getSelectedStudent()
    {
        return selectedStudent;
    }
    public static boolean isSemesterProfileAssigned()
    {
        return selectedStudent.getSemesterProfile() != null;
    }
    

    
    @Override
    public void start(Stage stage) throws Exception
    {
        Stage child = new Stage();
        LoginViewController controller = (LoginViewController) AbstractDialogController.injectFXML(child, "LoginView.fxml", "Study Planner Login");
        child.setOnHidden(event -> {
            try {
                mainViewInit(stage);
            } catch (Exception ex) {
                Logger.getLogger(StudyPlannerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        child.show();
    }
    
    private void mainViewInit(Stage stage) throws Exception
    {
        view = new ViewController();
        view.start(stage);
    }

    public static void loadSemesterProfile(File f)
    {
        String path = f.getPath();
        SemesterProfile sp = selectedStudent.getSemesterProfile();
        if(!sp.loadFromJSON(path))
            throw new RuntimeException("Failed to load Semester Profile!");
    }
    
    /**
     * Driver method for the application - the entirety of it should be run
     * from this method, not the view. However, the view should be requesting
     * information from the controller.
     * @param args 
     */
    public static void main(String[] args)
    {
        File f = new File("useryeu16sfu.ser");
        Student s1 = new Student("Adrian Wesolowski", "100185967");
        User u1 = new User("yeu16sfu", "ugabuga", s1);
        if(f.exists() && !f.isDirectory()) 
        {
            uc.loadUserData("useryeu16sfu.ser");
        }
        else
        {
            uc.addUser(u1);
            SemesterProfile sp = new SemesterProfile();
            if(!sp.loadFromJSON("example.json"))
                throw new RuntimeException("Failed to load Semester Profile!");
            s1.setSemesterProfile(sp);
        }
        Runtime.getRuntime().addShutdownHook
        (
            new Thread() 
            {
                public void run() { uc.saveStudentData();}
            }
        );
        // initiate view and use view and model methods to change the state
        // of both
        launch(args);
    }
}
