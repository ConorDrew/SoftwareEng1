package study.planner.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Activities that are needed to be done
 * @author Conor Drew and Adrian Wesolowski
 * @param <T> type which is capable to be quantifiable
 */
public class Activity<T extends Countable> implements Serializable
{
    private String description;
    private ArrayList<Note> notes;
    private Task.TaskType type;
    private ProgressType pType;
    private Progress<T> progress;
    private int timeSpent;
    private ArrayList<Task> assignedToTasks;

    /**
     * Default constructor
     */
    Activity()
    {
        description = null;
        notes = new ArrayList<>();
        progress = null;
        timeSpent = 0;
        assignedToTasks = new ArrayList<>();
    }

    /**
     * Standard minimum constructor
     * @param t task which is part of activity
     * @param type type of the activity represented textually
     * @param current current progress to complete the activity (default: empty)
     * @param quantity required progress to complete the activity
     */
    public Activity(Task t, String type, T current, T quantity)
    {
        this.description = t.getDescription();
        notes = new ArrayList<>();
        pType = ProgressType.valueOf(type);
        this.type = t.getTaskType();
        try
        {
            Class c = pType.getProgressType();
            if(c.isInstance(current) && c.isInstance(quantity))
            {
                progress = new Progress<>(current, quantity);
            }
        }
        catch(ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Progress type and passed parameters are not compatible.");
        }
        timeSpent = 0;
        assignedToTasks = new ArrayList<>();
        assignedToTasks.add(t);
    }

    /**
     * Get type of the task related to Activity
     * @return type
     */
    public Task.TaskType getTaskType()
    {
        return type;
    }
    
    /**
     * Get progress type of the Activity
     * @return progress type
     */
    public ProgressType getType()
    {
        return pType;
    }
    
    /**
     * Get description of the Activity
     * @return description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets description for activity
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Adds a note to Note Array for activity
     * @param n Note to be added
     */
    public void addNote(Note n){
        notes.add(n);
    }
    /**
     * Get current progress of the Activity
     * @return generically typed current progress
     */
    public T getCurrentProgress()
    {
        return progress.getCurrentProgress();
    }
    
    /**
     * Get the required amount for the Activity to be regarded as complete
     * @return generically typed required amount to progress
     */
    public T getRequired()
    {
        return progress.getRequired();
    }

    /**
     * Progress the activity by certain amount
     * @param amount
     * @return true if activity has been completed; false otherwise
     */
    public boolean advanceProgress(int amount)
    {
        progress.advanceProgress(amount);
        if(progress.checkProgress())
        {
            for(Task t : assignedToTasks)
            {
                t.activityComplete();
            }
            return true;
        }
        else
            return false;
    }
    
    /**
     * Checks the progress on the activity
     * @return True if task is completed, false if not
     */
    public boolean checkProgress()
    {
        return progress.checkProgress();
    }
    
    /**
     *  Assign a task to the activities list
     * @param t Task to be added
     * @return True if task was added
     */
    public boolean assignToTask(Task t){
        if(!assignedToTasks.contains(t))
            assignedToTasks.add(t);
        return true; //Holder for method to work
    }
    
    /**
     * Get all associated tasks
     * @return assigned tasks to the activity
     */
    public ArrayList<Task> getTasks()
    {
        return assignedToTasks;
    }
    
    /**
     * Return string representation of the activity object
     * @return text about activity
     */
    @Override
    public String toString()
    {
        StringBuilder buildStr = new StringBuilder();
        buildStr.append("Description: ").append(description);
        buildStr.append("\nTotal: ").append(progress.getCurrentProgress().toString());
        buildStr.append("\nDone: ").append(progress.getRequired().toString());
        //buildStr.append("\nTotal time spent: ").append(timeSpent);
        buildStr.append("\nRelated tasks: ");
        for(Task t : assignedToTasks)
        {
            buildStr.append("\n").append(t.getName());
        }
        buildStr.append("\n");
        return buildStr.toString();
    }
    
    /**
     * Enum which maps types of Progress to classes which can be used to
     * quantify it.
     */
    public enum ProgressType
    {
        TIME("study.planner.model.CountableDuration"),
        PARTS("study.planner.model.CountableInteger");
        private final String type;
        ProgressType(String type)
        {
            this.type = type;
        }
        public Class getProgressType() throws ClassNotFoundException
        {
            return Class.forName(type);
        }
    }
    /**
     * Inner class which is a structure designed to measure progress
     * @param <T> class which implements Countable interface
     */
    private class Progress<T extends Countable> implements Serializable
    {
        private T current;
        private T required;
        Progress(T current, T required)
        {
            this.current = current;
            this.required = required;
        }
        public T getCurrentProgress()
        {
            return current;
        }
        public T getRequired()
        {
            return required;
        }
        public void advanceProgress(int amount)
        {
            current.add(amount);
        }
        public boolean checkProgress()
        {
            return current.greaterOrEqual(required.getValue());
        }
    }
}
