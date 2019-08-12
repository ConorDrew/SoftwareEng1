package study.planner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
/**
 * Contains the tasks for the students to use. 
 * @author Conor Drew, Andy Davies and Adrian Wesolowski
 */
public class Task implements Serializable, Schedulable<LocalDate>{
    private static TaskDependencyGraph graph = new TaskDependencyGraph();
    private TaskType taskType;
    private String name;
    private String description;
    private ArrayList<Note> notes;
    private Activity activity;
    private ArrayList<Task> dependencies;
    private ArrayList<Milestone> assignedToMilestones;
    private boolean isComplete;
    private LocalDate deadline;

    /**
     * Minimum constructor which requires an activity to be assigned later
     * @param name
     * @param taskType
     * @param description 
     */
    public Task(String name, String taskType, String description)
    {
        this.name = name;
        this.taskType = TaskType.valueOf(taskType);
        this.description = description;
        notes = new ArrayList<>();
        activity = null;
        dependencies = new ArrayList<>();
        assignedToMilestones = new ArrayList<>();
        isComplete = false;
        deadline = null;
    }
    /**
     * Minimum standard constructor
     * @param name
     * @param taskType
     * @param description
     * @param a
     * @param deadline 
     */
    public Task(String name, String taskType, String description, Activity a, String deadline)
    {
        this.name = name;
        this.taskType = TaskType.valueOf(taskType);
        this.description = description;
        notes = new ArrayList<>();
        activity = a;
        //milestones = new ArrayList<>();
        assignedToMilestones = new ArrayList<>();
        dependencies = new ArrayList<>();
        isComplete = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
        // add the reference to the graph
        //graph.addTask(this);
    }
    /**
     * Constructor with specified task dependency
     * @param name
     * @param taskType
     * @param description
     * @param a
     * @param dep 
     */
    public Task(String name, String taskType, String description, Activity a, Task dep)
    {
        this.name = name;
        this.taskType = TaskType.valueOf(taskType);
        this.description = description;
        notes = new ArrayList<>();
        activity = a;
        //milestones = new ArrayList<>();
        assignedToMilestones = new ArrayList<>();
        dependencies = new ArrayList<>();
        addDependency(dep);
        isComplete = false;
        deadline = null;
        //graph.addTask(this, dep);
    }
    /**
     * Gets the task dependency graph
     * @return 
     */
    public static TaskDependencyGraph getDependencyGraph()
    {
        return graph;
    }
    /**
     * Updates the dependencies of this particular task.
     */
    void updateDependencies()
    {
        dependencies = graph.getDependencies(this);
    }
    /**
     * Updates the graph based on the dependencies saved in the list.
     * Warning: the reconstruction will not produce the same graph - it will only
     * reconstruct the dependency relationships without care of transitive properties
     */
    void updateGraph()
    {
        graph.addTask(this);
        for(Task t : dependencies)
        {
            graph.addTask(t);
            graph.setDependency(this, t);
        }
    }
    /**
     * Adds a dependency to this Task object
     * Usage: mainly by the graph
     * @param t 
     */
    void addDependency(Task t)
    {
        if(!dependencies.contains(t))
            dependencies.add(t);
    }
    /**
     * Removes a dependency from the list
     * Used mainly by the graph
     * @param t
     * @return 
     */
    boolean removeDependency(Task t)
    {
        return dependencies.remove(t);
    }
    /**
     * Assign a deadline to the task
     * @param deadline 
     */
    public void assignDeadline(String deadline)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
    }
    /**
     * Assign an activity to the task
     * @param a 
     */
    public void assignActivity(Activity a)
    {
        this.activity = a;
    }
    /**
     * Adds a milestone to which the task is assigned
     * @param m 
     */
    public void addMilestone(Milestone m)
    {
        assignedToMilestones.add(m);
    }
    /**
     * Sets task type from Enums
     * @param taskType Enum
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    /**
     * Used to signal that activity is complete
     */
    void activityComplete()
    {
        isComplete = true;
        if(isTaskComplete())
        {
            for(Milestone m : assignedToMilestones)
            {
                m.checkProgress();
            }
        }
    }
    /**
     * set a description for Task
     * @param description the description to be added
     */
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription()
    {
        return description;
    }
    /**
     * add note to Note array for task
     * @param note Note object
     */
    public void addNote(Note note){
        this.notes.add(note);
    }
    public void removeNote(int index){
        this.notes.remove(index);
    }
    public void replaceNote(int index, Note note){
        this.notes.set(index, note);
        
    }
    public ArrayList getNotes(){
        return notes;
    }
    
    /**
     * Adds milestone to the task
     * @param ms milestone object
     */
    /*public void addMilestone(Milestone ms){
        this.milestones.add(ms);
    }*/
    /**
     * Get the progress of the task regardless of dependencies and based solely
     * on the activity progress
     * @return 
     */
    public boolean getProgress()
    {
        return isComplete;
    }
    /**
     * Checks whether the task is completed, including it's dependencies
     * @return true if the task and its dependencies are considered complete;
     * otherwise, false
     */
    public boolean isTaskComplete()
    {
        if(dependencies == null)
            return getProgress();
        if(dependencies.isEmpty())
            return getProgress();
        boolean allComplete = true;
        for(Task t : dependencies)
        {
            allComplete = allComplete && t.checkProgress();
        }
        return allComplete;
    }
    
    /**
     * Checks the progress of the task
     * @return True if task is completed
     */
    public boolean checkProgress(){
        
        return isComplete && isTaskComplete(); //Holder for method to work
    }
    
    /**
     * Checks if this task is Dependent
     * @return true if dependent
     */
    public boolean isDependent(){
        
        return !dependencies.isEmpty();
    }

    /**
     * Sets a Task to be dependent
     * @param dependency Task Object
     */
    /*
    public void setDependency(Task dependency) {
        // check whether it is not the same task
        // to do: check whether there is a transitive dependency (not allowed)
        graph.setDependency(this, dependency);
    }*/

    /**
     * Gets the Dependencies of this task
     * @return dependencies
     */
    public ArrayList<Task> getDependencies() {
        return dependencies;
    }
    /**
     * Gets the task type
     * @return 
     */
    public TaskType getTaskType()
    {
        return taskType;
    }
    
    /**
     * Gets the string representation of the task
     * @return text representing task
     */
    @Override
    public String toString()
    {
        StringBuilder buildStr = new StringBuilder();
        buildStr.append("Description: ").append(this.getDescription());
        buildStr.append("\nComplete: ").append(this.checkProgress() ? "yes" : "no");
        buildStr.append("\n");
        return buildStr.toString();
    }
    /**
     * Gets the task name
     * @return 
     */
    public String getName() {
        return name;
    }
    /**
     * Get the activity related to the task
     * @return 
     */
    public Activity getActivity() 
    {
        return activity;
    }
    @Override
    public void setDeadline(LocalDate deadline) 
    {
        this.deadline = deadline;
    }

    @Override
    public void setDeadline(String deadline) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
    }

    @Override
    public LocalDate getDeadline() 
    {
        return deadline;
    }

    @Override
    public boolean isDue() 
    {
        LocalDate now = LocalDate.now();
        return deadline.compareTo(now) > 0;
    }
    
    /**
     * Enums for the different type of tasks
     */
    public enum TaskType
    {
        CODING,
        WRITING,
        READING;
        
        public static ArrayList<String> strValues()
        {
            ArrayList<String> result = new ArrayList<>();
            for(TaskType t : values())
            {
                result.add(t.name());
            }
            return result;
        }
    }
}
