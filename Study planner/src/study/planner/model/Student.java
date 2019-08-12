package study.planner.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class controls the Student information.
 * @author Conor Drew
 */
public class Student implements Serializable{
    private String name;
    private String id;
    private SemesterProfile semesterProfile;

    /**
     * Standard constructor
     * @param name
     * @param id 
     */
    public Student(String name, String id){
        this.name = name;
        this.id = id;
        semesterProfile = null;
    }
    /**
     * Sets the semester profile to this student
     * @param sp 
     */
    public void setSemesterProfile(SemesterProfile sp)
    {
        this.semesterProfile = sp;
    }
    /**
     * Gets the student name
     * @return 
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the student ID
     * @return 
     */
    public String getID() {
        return id;
    }
    /**
     * Gets the semester profile
     * @return 
     */
    public SemesterProfile getSemesterProfile() {
        return semesterProfile;
    }
    /**
     * Gets all the tasks concerning this student
     * @return 
     */
    public ArrayList<Task> getAllTasks()
    {
        SemesterProfile sp = this.getSemesterProfile();
        ArrayList<Task> result = new ArrayList<>();
        for(Module m : sp.getModules())
        {
            for(ModuleItem mi : m.getModuleItems())
            {
                result.addAll(mi.getTasks());
            }
        }
        return result;
    }
    /**
     * Gets all the complete tasks concerning this student
     * @return 
     */
    public ArrayList<Task> getAllCompleteTasks()
    {
        SemesterProfile sp = this.getSemesterProfile();
        ArrayList<Task> result = new ArrayList<>();
        for(Module m : sp.getModules())
        {
            for(ModuleItem mi : m.getModuleItems())
            {
                result.addAll(mi.getCompleteTasks());
            }
        }
        return result;
    }
}
