/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.controller;

import java.util.ArrayList;
import study.planner.model.Activity;
import study.planner.model.Milestone;
import study.planner.model.Module;
import study.planner.model.ModuleItem;
import study.planner.model.SemesterProfile;
import study.planner.model.Task;

/**
 *
 * @author adq
 */
public class QueryController {

    public static String getActivityType(String activityName) {
        ArrayList<Activity> activities = StudyPlannerController.getSelectedStudent().getSemesterProfile().getActivities();
        for (Activity a : activities) {
            if (a.getDescription().equals(activityName)) {
                return a.getType().name();
            }
        }
        return null;
    }

    public static String getMilestoneDetails(String mileName, boolean inclComplete) {
        String result = null;
        for (Module m : StudyPlannerController.getSelectedStudent().getSemesterProfile().getModules()) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Milestone mile : mi.getMilestones()) {
                    if (mile.getName().equals(mileName)) {
                        result = mile.toString();
                        break;
                    }
                }
                if (inclComplete) {
                    for (Milestone mile : mi.getCompleteMilestones()) {
                        if (mile.getName().equals(mileName)) {
                            result = mile.toString();
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<String> getModuleNames() {
        ArrayList<String> strModules = new ArrayList<>();
        for (Module m : StudyPlannerController.getSelectedStudent().getSemesterProfile().getModules()) {
            strModules.add(m.getCode() + " " + m.getName());
        }
        return strModules;
    }

    public static ArrayList<String> getTaskNames() {
        ArrayList<String> result = new ArrayList<>();
        for (Task t : StudyPlannerController.getSelectedStudent().getAllTasks()) {
            result.add(t.getName());
        }
        return result;
    }

    public static ArrayList<String> getTaskNames(String moduleCode, String moduleItemName, boolean inclComplete) throws ModuleNotFoundException, ModuleItemNotFoundException {
        ArrayList<String> result = new ArrayList<>();
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
        for (Task t : mi.getTasks()) {
            if (inclComplete) {
                result.add(t.getName());
            } else if (!t.checkProgress()) {
                result.add(t.getName());
            }
        }
        return result;
    }

    public static ArrayList<String> getModuleItemsNames(String moduleCode) throws ModuleNotFoundException {
        Module m = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModule(moduleCode);
        if (m == null) {
            throw new ModuleNotFoundException("Module " + moduleCode + " has not " + "been found in " + StudyPlannerController.getSelectedStudent().getSemesterProfile() + ".");
        }
        ArrayList<String> result = new ArrayList<>();
        for (ModuleItem item : m.getModuleItems()) {
            result.add(item.getName());
        }
        return result;
    }

    public static ArrayList<String> getTaskText() {
        ArrayList<String> result = new ArrayList<>();
        for (Task t : StudyPlannerController.getSelectedStudent().getAllTasks()) {
            result.add(t.getDescription());
        }
        return result;
    }

    // to do: change the method name to something more precise
    public static String[] getStudentData() {
        String[] str = new String[4];
        String tmp;
        str[0] = StudyPlannerController.getSelectedStudent().getName();
        str[1] = StudyPlannerController.getSelectedStudent().getID();
        tmp = StudyPlannerController.getSelectedStudent().getSemesterProfile().getName();
        str[2] = tmp.substring(0, 5);
        str[3] = String.valueOf(tmp.charAt(tmp.length() - 1));
        // to do: create a better approach to extracting school name and year
        return str;
    }

    public static ArrayList<String> getTaskDetails(String taskName, boolean inclComplete) throws TaskNotFoundException {
        SemesterProfile sp = StudyPlannerController.getSelectedStudent().getSemesterProfile();
        Task t = null;
        ArrayList<String> result = new ArrayList<>();
        for (Module m : sp.getModules()) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Task i : mi.getTasks()) {
                    if (i.getName().equals(taskName)) {
                        t = i;
                        result.add("Task Name: " + t.getName());
                        result.add("Task Description: " + t.getDescription());                       
                        result.add("Module: " + m.getName());
                        result.add("Task Type: " + mi.getName());   
                        result.add("Task topic: " + t.getActivity().getTaskType().toString());  
                        result.add("Task DeadLine: " + t.getDeadline().toString());  
                        result.add("Current progress: " + t.getActivity().getCurrentProgress());
                        
//                        if (i.isDependent()) {
//                            result.add("TASK IS A DEPENDENT:");
//                            result.add("Dependencies: " + i.getDependencies().toString());
//                            
//                        }
                        if (i.isDue()) {
                             result.add("TASK IS DUE");
                        }
                    }
                }
                if (inclComplete) {
                    for (Task i : mi.getCompleteTasks()) {
                        if (i.getName().equals(taskName)) {
                            t = i;
                        }
                    }
                }
            }
        }
        if (t == null) {
            throw new TaskNotFoundException();
        }
        return result;
    }

    public static ArrayList<String> getActivityDetails(String taskType, boolean inclComplete) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Activity> activities = StudyPlannerController.getSelectedStudent().getSemesterProfile().getActivities();
        for (Activity a : activities) {
            if (a.getTaskType().equals(Task.TaskType.valueOf(taskType))) {
                if (inclComplete) {
                    result.add(a.toString());
                } else if (!a.checkProgress()) {
                    result.add(a.toString());
                }
            }
        }
        return result;
    }

    public static ArrayList<String> getActivityTypes() {
        ArrayList<String> result = new ArrayList<>();
        for (Activity.ProgressType a : Activity.ProgressType.values()) {
            result.add(a.toString());
        }
        return result;
    }

    public static ArrayList<String> getCompletedTaskText() {
        ArrayList<String> result = new ArrayList<>();
        for (Task t : StudyPlannerController.getSelectedStudent().getAllCompleteTasks()) {
            result.add(t.getDescription());
        }
        return result;
    }

    public static ArrayList<String> getTaskTypes() {
        return Task.TaskType.strValues();
    }

    public static ArrayList<String> getMilestoneNames(boolean inclComplete) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Module> modules = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModules();
        for (Module m : modules) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Milestone mile : mi.getMilestones()) {
                    result.add(mile.getName());
                }
                if (inclComplete) {
                    for (Milestone mile : mi.getCompleteMilestones()) {
                        result.add(mile.getName());
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get module data in a format supported by the view.
     * @param moduleCode
     * @return string representation of all module data suited for display
     * @throws study.planner.model.ModuleNotFoundException
     */
    public static String getModuleData(String moduleCode) throws ModuleNotFoundException {
        Module m = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModule(moduleCode);
        if (m == null) {
            throw new ModuleNotFoundException("Module " + moduleCode + " has not " + "been found in " + StudyPlannerController.getSelectedStudent().getSemesterProfile() + ".");
        }
        return m.toString();
    }
    
}
