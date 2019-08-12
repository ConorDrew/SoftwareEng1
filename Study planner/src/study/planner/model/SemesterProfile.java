package study.planner.model;

import study.planner.controller.InvalidSemesterProfileNameException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
// to do: properly incorporate the JSON.simple library into the project
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author Conor Drew, Andy Davies and Adrian Wesolowski
 */

// to do: probably incorporate SemesterProfile as a nested class of student
// as one profile can only be fit to one student
public class SemesterProfile implements Serializable{
    private String name;
    private ArrayList<Module> modules;
    private ArrayList<Activity> activities;
    private ArrayList<Activity> completedActivities;

    /**
     * Default constructor
     */
    public SemesterProfile()
    {
        this.name = null;
        this.modules = new ArrayList<>();
        this.activities = new ArrayList<>();
        this.completedActivities = new ArrayList<>();
    }
    /**
     * Adds an activity to store for convenience
     * @param a 
     */
    public void addActivity(Activity a)
    {
        if(!activities.contains(a) && !completedActivities.contains(a))
            activities.add(a);
    }
    /**
     * Gets all the activities concerning this student
     * @return 
     */
    public ArrayList<Activity> getActivities()
    {
        return activities;
    }
    
    /**
     * Attempt to move an activity to completion.
     * @param a
     * @return true if present and completed, false if not
     */
    public boolean completeActivity(Activity a)
    {
        if(activities.contains(a) && a.checkProgress())
        {
            activities.remove(a);
            completedActivities.add(a);
            return true;
        }
        return false;
    }
    
    /**
     *  Checks whether the format of name is valid
     *  Format: 3 character abbreviation of school followed by 
     *  type of studies (UG, PG), student ID, slash ('/') year (i.e. 2)
     *  Valid example: CMPUG100185967/2
     */
    public enum schoolType{
        // A few potential schools for expanding the application at a later
        // date
        CMP, CHE, ENG, ART, ENV, LAW, GEO, MAT, MED, BIO;
    }
    /**
     * Checks whether the given semester profile name is valid
     * @param name
     * @return
     * @throws InvalidSemesterProfileNameException 
     */
    private static boolean validName(String name) throws InvalidSemesterProfileNameException
    {     
        if(name.matches("(CMP)(UG|PG)[0-9]{9}/[1-3]"))
            return true;
        else
            throw new InvalidSemesterProfileNameException(name);
    }
    /**
     * Constructor for Semester Profile
     * @param name
     * @param modules
     */
    public SemesterProfile(String name, ArrayList<Module> modules) {
        this.name = name;
        this.modules = modules;
        this.activities = new ArrayList<>();
    }
    
    /**
     * Loads student's Semester profile.
     * Structure of JSON in short:
     * {
     *     "Name": "CMPUG10287645/3",
     *     "Modules":
     *     {
     *          "level":[module data],
     *          ...
     *          "last level":[module data]
     *     }
     * }
     * Module data is structured in this way:
     * It is a comma separated list of objects:
     * {
     *      "Code": "module codename",
     *      "Semester": "semester" (must match the mnemonic for the enum),
     *      "Weight": 20 (integer),
     *      "Name": "module name",
     *      "ModuleItems":[module items data]
     * }
     * Module Item Data are comma separated:
     * {
     *      "ItemType":"EXAM" (must match the mnemonic for the enum),
     *      "Description":"short description",
     *      "Deadline":"2017-05-04" (format undecided),
     *      "Weight": 10 (integer)
     * }
     * @param filename location of loader file
     * @return True if profile was added.
     */
    public boolean loadFromJSON(String filename)
    {
        try
        {
            FileReader f = new FileReader(filename);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(f);
            String name = (String) obj.get("Name");
            try
            {
                validName(name);
            }
            catch(InvalidSemesterProfileNameException e)
            {
                return false;
            }
            this.name = name;
            JSONObject levels = (JSONObject) obj.get("Modules");
            for(Object o : levels.entrySet())
            {
                Map.Entry m = (Map.Entry) o;
                String key = (String) m.getKey();
                JSONArray modules = (JSONArray) m.getValue();
                Iterator i = modules.iterator();
                while(i.hasNext())
                {
                    JSONObject module = (JSONObject) i.next();
                    Module tmp = new Module(
                            (String) module.get("Code"),
                            (String) module.get("Name"),
                            Integer.parseInt(key),
                            ((Long) module.get("Weight")).intValue(),
                            (String) module.get("Semester")
                    );
                    JSONArray moduleItems = (JSONArray) module.get("ModuleItems");
                    Iterator j = moduleItems.iterator();
                    while(j.hasNext())
                    {
                        JSONObject moduleItem = (JSONObject) j.next();
                        ModuleItem tmp2 = new ModuleItem(
                                (String) moduleItem.get("Deadline"),
                                (String) moduleItem.get("Description"),
                                ((Long) module.get("Weight")).intValue(),
                                (String) moduleItem.get("ItemType")
                        );
                        tmp.addModuleItem(tmp2);
                    }
                    this.modules.add(tmp);
                }
            }
            f.close();
        }
        catch(IOException e)
        {
            return false;
        }
        catch(ParseException e)
        {
            return false;
        }
        finally
        {
            
        }
        return true; //Holder for method to work
    }
    /**
     * Saves the semester profile to JSON format
     * @param filename
     * @return
     * @throws IOException 
     */
    public boolean dumpJSON(String filename) throws IOException
    {
        try
        {
            FileWriter f = new FileWriter(filename);
            // create a file which dumps a json file containing all info
            // about semester profile
            
            f.close();
        }
        catch(IOException e)
        {
            
        }
        finally
        {
            
        }
        return true;
    }
    /**
     * Returns the name of the profile
     * @return 
     */
    public String getName()
    {
        return name;
    }
    /**
     * Gets all the modules concerning the profile
     * @return list of modules
     */
    public ArrayList<Module> getModules()
    {
        return modules;
    }
    /**
     * Gets a particular module identified by module code
     * @param moduleCode
     * @return module matching the code, or null if not found
     */
    public Module getModule(String moduleCode)
    {
        // linear search
        for(Module m : modules)
        {
            if(m.getCode().equals(moduleCode))
                return m;
        }
        return null;
    }
}
