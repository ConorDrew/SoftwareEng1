package study.planner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Milestone class that stores optional aggregations of Task class
 * @author Conor Drew and Adrian Wesolowski
 */
public class Milestone implements Serializable, Schedulable<LocalDate>{
    private String name;
    private String description;
    private LocalDate deadline;
    private boolean progress;
    private ArrayList<Task> requiredTasks;
    
    /**
     * Constructor for a single-task milestone
     * @param name
     * @param description
     * @param deadline
     * @param t 
     */
    public Milestone(String name, String description, String deadline, Task t)
    {
        this.name = name;
        this.description = description;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
        this.requiredTasks = new ArrayList<>();
        this.requiredTasks.add(t);
        this.progress = checkProgress();
    }
    /**
     * Constructor for a Collection of Task objects
     * @param name
     * @param description
     * @param deadline
     * @param t 
     */
    public Milestone(String name, String description, String deadline, Collection<Task> t)
    {
        this.name = name;
        this.description = description;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
        this.requiredTasks = new ArrayList<>();
        this.requiredTasks.addAll(t);
        this.progress = checkProgress();
    }
    /**
     * Constructor for a Collection of Task objects with explicitly specified
     * LocalDate object
     * @param name
     * @param description
     * @param deadline
     * @param t 
     */
    public Milestone(String name, String description, LocalDate deadline, Collection<Task> t)
    {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.requiredTasks = new ArrayList<>();
        this.requiredTasks.addAll(t);
        this.progress = checkProgress();
    }
    /**
     * Add another task required for a milestone
     * @param t
     * @return true if added, false if not
     */
    public boolean addTask(Task t)
    {
        if(!requiredTasks.contains(t))
        {
            requiredTasks.add(t);
            checkProgress();
            return true;
        }
        return false;
    }
    
    /**
     * Sets description for the milestone
     * @param description String as the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets a deadline for the mileStone
     * @param deadline String as a deadline
     */
    public void setDeadline(String deadline) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.deadline = LocalDate.parse(deadline, formatter);
    }

    /**
     * Checks progress of the milestone
     * @return true if complete, false if not
     */
    public boolean checkProgress()
    {
        // check that all required tasks are complete
        boolean allComplete = true;
        for(Task t : requiredTasks)
        {
            if(!t.checkProgress())
            {
                allComplete = false;
                break;
            }
        }
        this.progress = allComplete;
        return allComplete;
    }
    /**
     * Sets if the progress of this milestone is done
     * @param progress True or false for if Done
     */
    public void setProgress(boolean progress) {
        this.progress = progress;
    }
    
    /**
     * Gets the description of the Milestone
     * @return returns the Description (String)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the deadline of the milestone
     * @return returns date of deadline (Time)
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Gets progress of milestone, 
     * @return if done return True
     */
    public boolean getProgress() {
        return progress;
    }

    /**
     * Get the amount of tasks of which the milestone is comprised
     * @return number of tasks required for completion
     */
    public int getTaskCount()
    {
        return requiredTasks.size();
    }
    /**
     * Get the amount of tasks which are completed
     * @return number of tasks required which are already completed
     */
    public int getCompletedTaskCount()
    {
        int counter = 0;
        for(Task t : requiredTasks)
        {
            if(t.checkProgress())
                counter++;
        }
        return counter;
    }
    /**
     * Get all the tasks of the milestone
     * @return an array list of tasks
     */
    public ArrayList<Task> getRequiredTasks()
    {
        return requiredTasks;
    }
    
    /**
     * Return the string representation of the milestone object
     * @return text about the milestone
     */
    @Override
    public String toString() {
        return "Milestone: " + name + "\n" + 
                "Description: " + description + "\n" +
                "Deadline: " + deadline + "\n" +
                "Progress: " + progress + "\n";
    }

    /**
     * Get name of the milestone
     * @return milestone name
     */
    public String getName() {
        return name;
    }

    @Override
    public void setDeadline(LocalDate deadline) {

        this.deadline = deadline;
    }

    @Override
    public boolean isDue() 
    {
        return deadline.compareTo(LocalDate.now()) > 0;
    }

}
