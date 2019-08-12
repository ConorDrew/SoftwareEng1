package study.planner.model;

import study.planner.controller.UnboundTaskException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The module Item that makes up the Module
 * @author Conor Drew and Adrian Wesolowski
 */
public class ModuleItem implements Serializable, Schedulable<LocalDate>{
    private LocalDate deadline;
    private String name;
    private int weight;
    private ItemType itemType;
    private ArrayList<Task> tasks;
    private ArrayList<Task> completeTasks;
    private ArrayList<Milestone> milestones;
    private ArrayList<Milestone> completedMilestones;
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(deadline);
        out.writeObject(name);
        out.writeInt(weight);
        out.writeObject(itemType);
        out.writeObject(tasks);
        out.writeObject(completeTasks);
        out.writeObject(milestones);
        out.writeObject(completedMilestones);
    }
    /**
     * Loads the object from the determined state and reconstructs the dependency graph
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        deadline = (LocalDate) in.readObject();
        name = (String) in.readObject();
        weight = in.readInt();
        itemType = (ItemType) in.readObject();
        tasks = (ArrayList<Task>) in.readObject();
        completeTasks = (ArrayList<Task>) in.readObject();
        for(Task t : tasks)
        {
            t.updateGraph();
        }
        for(Task t : completeTasks)
        {
            t.updateGraph();
        }
        milestones = (ArrayList<Milestone>) in.readObject();
        completedMilestones = (ArrayList<Milestone>) in.readObject();
    }
    
    /**
     *  Constructor for Module Item
     * @param deadline When item is due
     * @param name Name of the item
     * @param weight Weight of the item
     * @param itemType Type of item
     */
    public ModuleItem(String deadline, String name, int weight, String itemType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
        this.name = name;
        this.weight = weight;
        this.itemType = ItemType.valueOf(itemType);
        this.tasks = new ArrayList<>();
        this.completeTasks = new ArrayList<>();
        this.milestones = new ArrayList<>();
        this.completedMilestones = new ArrayList<>();
    }
    /**
     * Adds the task to the graph and to the module item
     * @param t
     * @param dependencies 
     */
    public void addTask(Task t, ArrayList<Task> dependencies)
    {
        TaskDependencyGraph graph = Task.getDependencyGraph();
        if(!tasks.contains(t))
        {
            tasks.add(t);
            graph.addTask(t);
        }
        if(dependencies != null && !dependencies.isEmpty())
        {
            for(Task dep : dependencies)
            {
                graph.setDependency(t, dep);
            }            
        }
    }
    /**
     * Adds a dependency of child task to the parent task
     * @param parent
     * @param child
     * @return true if successful, false if not
     */
    public boolean addDependency(Task parent, Task child)
    {
        if(tasks.contains(parent) && tasks.contains(child))
        {
            return Task.getDependencyGraph().setDependency(parent, child);
        }
        return false;
    }
    
    /**
     * Attempt to complete a task
     * @param t
     * @return true if task is considered complete, false if not
     */   
    public boolean completeTask(Task t)
    {
        try
        {
            if(isTaskComplete(t))
            {
                tasks.remove(t);
                completeTasks.add(t);
                return true;
            }
            else
                return false;
        }
        catch(UnboundTaskException ex)
        {
            return false;
        }
            
    }
    /**
     * Adds a milestone to the collection
     * @param m 
     */
    public void addMilestone(Milestone m)
    {
        if(!milestones.contains(m) && !completedMilestones.contains(m))
            milestones.add(m);
    }
    /**
     * Check whether the milestone has been reached.
     * @param m Milestone to be checked
     * @return true if milestone has been reached and marked as complete; false otherwise
     */
    public boolean completeMilestone(Milestone m)
    {
        if(!milestones.contains(m))
            return false;
        boolean result = m.checkProgress();
        if(result)
        {
            milestones.remove(m);
            completedMilestones.add(m);
        }
        return result;
    }
    /**
     * Check whether the task can be considered completed. The method
     * should take dependent tasks into consideration.
     * @param t Task reference to check for completion
     * @throws UnboundTaskException
     * @return 
     */
    public boolean isTaskComplete(Task t) throws UnboundTaskException
    {
        if(!tasks.contains(t))
            throw new UnboundTaskException();
        return t.isTaskComplete();
    }
    /**
     * Removes a task from the array
     * @param t 
     */
    public void removeTask(Task t)
    {
        tasks.remove(t);
    }
    /**
     * Gets all the tasks related to the module item
     * @return list of tasks
     */
    public ArrayList<Task> getTasks()
    {
        return tasks;
    }
    /**
     * Gets all the complete tasks related to the module item
     * @return list of complete tasks
     */
    public ArrayList<Task> getCompleteTasks()
    {
        return completeTasks;
    }
    
    /**
     *  Sets the deadline for item
     * @param deadline String in form of YYYY/MM/DD
     */
    public void setDeadline(String deadline) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
    }

    /**
     * Sets name of item
     * @param name Name of item(String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets weight of item
     * @param weight weight of item (Int)
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    /**
     * Works out if the item is Due or not, if Deadline is after current
     * time/Date it will return true
     * @return return True if Item is due
     */
    public boolean isDue(){
        //sets date to current date/time when intilized
        LocalDate date = LocalDate.now();
        
        //checks if current date is after deadline.
        return deadline.compareTo(date) > 0;
    }
    
    /**
     * Gets deadline date
     * @return deadline date
     */
    public LocalDate getDeadline() {
        return deadline;
    }
    /**
     * gets Name of item
     * @return return Name of item
     */
    public String getName() {
        return name;
    }

    /**
     * gets weight of item
     * @return returns weight of item
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * gets item type
     * @return returns item type
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Prints the module Item to a string
     * @return nicely prints Module item to string
     */
    @Override
    public String toString() {
        return name + "\n" + 
                "Weight: " + weight + "\n" + 
                "Deadline: " + deadline + "\n" +
                "ItemType: " + itemType;
    }

    @Override
    public void setDeadline(LocalDate deadline) 
    {
        this.deadline = deadline;
    }

    /**
     * Gets the milestones related to the module item
     * @return list of milestones
     */
    public ArrayList<Milestone> getMilestones() {
        return milestones;
    }
    /**
     * Gets the complete milestones related to the module item
     * @return list of complete milestones
     */
    public ArrayList<Milestone> getCompleteMilestones() {
        return completedMilestones;
    }

    /**
     * Different types of items in module
     */
    public enum ItemType{
        EXAM,
        VERBAL,
        ESSAY,
        COURSEWORK,
        GROUP_PROJECT;
    }
    
}
