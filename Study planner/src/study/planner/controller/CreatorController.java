/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import study.planner.model.Activity;
import study.planner.model.Countable;
import study.planner.model.Milestone;
import study.planner.model.Module;
import study.planner.model.ModuleItem;
import study.planner.model.SemesterProfile;
import study.planner.model.Task;

/**
 *
 * @author adq
 */
public class CreatorController {

    /**
     * Signals the controller to advance progress of an activity by the amount
     * indicated by progress parameter.
     * @param activityName unique name of an activity
     * @param progress amount of hours, parts, etc. spent on activity
     * @return true if activity is completed, false if progress fell short
     * @throws study.planner.controller.ActivityNotFoundException
     */
    public static boolean advanceActivity(String activityName, int progress) throws ActivityNotFoundException {
        SemesterProfile sp = StudyPlannerController.getSelectedStudent().getSemesterProfile();
        Activity a = null;
        for (Activity i : sp.getActivities()) {
            if (i.getDescription().equals(activityName)) {
                a = i;
                break;
            }
        }
        if (a == null) {
            throw new ActivityNotFoundException();
        }
        boolean isComplete = a.advanceProgress(progress);
        // do something with the result (inform the user?)
        // or return the value
        return isComplete;
    }

    /**
     * Signals the controller to construct a Milestone instance and add it
     * to the relevant ModuleItem.
     * @param moduleCode
     * @param moduleItemName
     * @param mileName
     * @param desc
     * @param taskNames
     * @return true if Milestone has already been completed, false otherwise
     * @throws ModuleItemNotFoundException
     * @throws ModuleNotFoundException
     */
    public static boolean constructMilestone(String moduleCode, String moduleItemName, String mileName, String desc, ArrayList<String> taskNames) throws ModuleItemNotFoundException, ModuleNotFoundException {
        Module m = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModule(moduleCode);
        if (m == null) {
            throw new ModuleNotFoundException();
        }
        ModuleItem mi = null;
        for (ModuleItem i : m.getModuleItems()) {
            if (i.getName().equals(moduleItemName)) {
                mi = i;
            }
        }
        if (mi == null) {
            throw new ModuleItemNotFoundException();
        }
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks = mi.getTasks();
        allTasks.addAll(mi.getCompleteTasks());
        LocalDate d = LocalDate.now();
        LocalDate cmp = null;
        ArrayList<Task> mileTasks = new ArrayList<>();
        for (String taskName : taskNames) {
            for (Task t : allTasks) {
                if (t.getName().equals(taskName)) {
                    cmp = t.getDeadline();
                    if (cmp.compareTo(d) < 0) {
                        cmp = d;
                    }
                    mileTasks.add(t);
                }
            }
        }
        if (mileTasks.isEmpty()) {
            throw new IllegalArgumentException("No viable tasks have been found.");
        }
        Milestone mile = new Milestone(mileName, desc, cmp, mileTasks);
        mi.addMilestone(mile);
        for (Task t : mileTasks) {
            t.addMilestone(mile);
        }
        return mile.checkProgress();
    }

    //to do: separate activity creation to another subroutine
    /**
     * Signals the controller to construct a Task instance for selected student
     * @param taskName
     * @param moduleCode
     * @param moduleItemName
     * @param taskType
     * @param desc
     * @param depTasks
     * @param progressType
     * @param quantity
     * @param deadline
     * @throws study.planner.controller.ModuleNotFoundException
     */
    public static void constructTask(String taskName, String moduleCode, String moduleItemName, String taskType, String desc, ArrayList<String> depTasks, String progressType, String quantity, String deadline) throws ModuleNotFoundException {
        Module m = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModule(moduleCode);
        if (m == null) {
            throw new ModuleNotFoundException();
        }
        ModuleItem mi = null;
        // to do: create a method in ModuleItem to handle this
        // preferably, use something else than linear search (requires some other
        // structure in that case)
        for (ModuleItem item : m.getModuleItems()) {
            if (item.getName().equals(moduleItemName)) {
                mi = item;
                break;
            }
        }
        if (mi == null) {
            // search failed
            return;
        }
        Task t = new Task(taskName, taskType, desc);
        Activity.ProgressType pType = Activity.ProgressType.valueOf(progressType);
        Class<? extends Countable> c = null;
        Object required = null;
        Object current = null;
        try {
            c = pType.getProgressType();
            // constructor should initialise the first and only field
            Constructor<?> cstr = c.getConstructor();
            current = cstr.newInstance();
            required = cstr.newInstance();
            // get the setValue method
            Method setValue = c.getMethod("setValue", int.class);
            setValue.invoke(required, Integer.parseInt(quantity));
            setValue.invoke(current, 0);
        } catch (ClassNotFoundException ex) {
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(StudyPlannerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Activity a = new Activity(t, progressType, c.cast(current), c.cast(required));
        t.assignDeadline(deadline);
        t.assignActivity(a);
        ArrayList<Task> dependencies = null;
        if (depTasks != null) {
            dependencies = new ArrayList<>();
            for (String depTask : depTasks) {
                for (Task i : mi.getTasks()) {
                    if (i.getName().equals(depTask)) {
                        dependencies.add(i);
                    }
                }
                for (Task i : mi.getCompleteTasks()) {
                    if (i.getName().equals(depTask)) {
                        dependencies.add(i);
                    }
                }
            }
        }
        mi.addTask(t, dependencies);
        StudyPlannerController.getSelectedStudent().getSemesterProfile().addActivity(a);
        //
        //        //small function to add a NOTE to the task when made
        //        Note n = new Note("This is a test NOTE");
        //        Note z = new Note("2nd note added");
        //        t.addNote(n);
        //        t.addNote(z);
    }
    
}
