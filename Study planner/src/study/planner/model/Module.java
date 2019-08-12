package study.planner.model;

import java.io.Serializable;
import java.util.ArrayList;
//import study.planner.ModuleItem.ItemType;

/**
 * Hold the modules that a student is enrolled in
 * @author Conor Drew and Adrian Wesolowski
 */
public class Module implements Serializable{
    private String moduleCode;
    // modules are attributed to levels, not years
    private int level;
    private int weight;
    private String moduleName;
    private ArrayList<ModuleItem> moduleItems;
    private Semester semester;
    

    /**
     * Constructor that Creates a module with no prams
     */
    public Module() {
        moduleItems = new ArrayList<>();
    }

    /**
     * Minimal constructor for viable data about the module
     * @param moduleCode
     * @param moduleName
     * @param moduleLevel
     * @param weight
     * @param semester 
     */
    public Module(String moduleCode, String moduleName, int moduleLevel, int weight, String semester)
    {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.level = moduleLevel;
        this.weight = weight;
        moduleItems = new ArrayList<>();
        this.semester = Semester.valueOf(semester);
    }
    /**
     * Constructor that creates a module with a list of module items
     * @param moduleItems Takes a list of module Items
     */
    public Module(ArrayList<ModuleItem> moduleItems) {
        this.moduleItems = moduleItems;
    }
    /**
     * Get all module items
     * @return 
     */
    public ArrayList<ModuleItem> getModuleItems()
    {
        return moduleItems;
    }
    /**
     * Get module code
     * @return module code
     */
    public String getCode()
    {
        return moduleCode;
    }
    /**
     * Get module name
     * @return module name
     */
    public String getName()
    {
        return moduleName;
    }
    /**
     * Checks if the total weight is 100%
     * @return True if total is 100
     */
    private boolean checkWeightTotal(){
        int weightTotal = 0;
        //adds all the 
        for (ModuleItem moduleItem : moduleItems) {
            weightTotal += moduleItem.getWeight();
        }
        return weightTotal == 100;
    }
    
    /**
     * Add module to module array
     * @param m Takes a module Item
     */
    public void addModuleItem(ModuleItem m){
        this.moduleItems.add(m);
    }
    /**
     * Add an array list of module items to the array
     * @param c 
     */
    public void addModuleItems(ArrayList<ModuleItem> c)
    {
        this.moduleItems.addAll(c);
    }
    
    
    /**
     * Prints module to string.
     * @return Module in string form
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(ModuleItem s : moduleItems){
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Enum which represents the semester of the module 
     */
    public enum Semester 
    {
        SEM1,
        SEM2,
        YEAR_LONG;
    }
    
    
}
