/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import study.planner.model.Milestone;
import study.planner.model.Module;
import study.planner.model.ModuleItem;
import study.planner.model.Task;

/**
 *
 * @author adq
 */
public class DashboardController {

    public static ArrayList<String> deadlinesPast(String datePattern, String date, String dateTo) {
        ArrayList<String> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate d;
        if (date != null) {
            d = LocalDate.parse(date, formatter);
        } else {
            d = LocalDate.now();
        }
        LocalDate limit = LocalDate.parse(dateTo, formatter);
        LocalDate cmp;
        String s;
        ArrayList<Module> modules = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModules();
        for (Module m : modules) {
            for (ModuleItem mi : m.getModuleItems()) {
                cmp = mi.getDeadline();
                if (limit.compareTo(cmp) < 0 && d.compareTo(cmp) > 0) {
                    s = formatter.format(cmp) + " " + mi.getName();
                    result.add(s);
                }
                for (Task t : mi.getTasks()) {
                    cmp = t.getDeadline();
                    if (limit.compareTo(cmp) < 0 && d.compareTo(cmp) > 0) {
                        s = formatter.format(cmp) + " " + t.getName();
                        result.add(s);
                    }
                }
                for (Milestone mile : mi.getMilestones()) {
                    cmp = mile.getDeadline();
                    if (limit.compareTo(cmp) < 0 && d.compareTo(cmp) > 0) {
                        s = formatter.format(cmp) + " " + mile.getName();
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    private static Map<String, Map<String, String>> moduleItemDeadlinesApproaching(Module m, String datePattern) {
        Map<String, Map<String, String>> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate d = LocalDate.now();
        LocalDate cmp;
        Map<String, String> val;
        String key;
        for (ModuleItem mi : m.getModuleItems()) {
            cmp = mi.getDeadline();
            if (d.compareTo(cmp) <= 0) {
                key = mi.getName();
                val = taskDeadlinesApproaching(mi, datePattern);
                val.putAll(milestoneDeadlinesApproaching(mi, datePattern));
                val.put(key, formatter.format(cmp));
                result.put(key, val);
            }
        }
        return result;
    }

    private static Map<String, String> milestoneDeadlinesApproaching(ModuleItem mi, String datePattern) {
        Map<String, String> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate d = LocalDate.now();
        LocalDate cmp;
        String key;
        String val;
        for (Milestone mile : mi.getMilestones()) {
            cmp = mile.getDeadline();
            if (d.compareTo(cmp) <= 0) {
                val = formatter.format(cmp);
                key = mile.getName();
                result.put(key, val);
            }
        }
        return result;
    }

    public static Map<String, Map<String, Boolean>> getItemProgress(String moduleCode, String moduleItemName, String itemName) throws ModuleNotFoundException, ModuleItemNotFoundException, TaskNotFoundException {
        Map<String, Map<String, Boolean>> result = new HashMap<>();
        Module m = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModule(moduleCode);
        ModuleItem mi = null;
        if (m == null) {
            throw new ModuleNotFoundException();
        }
        for (ModuleItem i : m.getModuleItems()) {
            if (i.getName().equals(moduleItemName)) {
                mi = i;
            }
        }
        if (mi == null) {
            throw new ModuleItemNotFoundException();
        }
        Task t = null;
        for (Task i : mi.getTasks()) {
            if (i.getName().equals(itemName)) {
                t = i;
            }
        }
        Milestone mile = null;
        if (t == null) {
            for (Milestone i : mi.getMilestones()) {
                if (i.getName().equals(itemName)) {
                    mile = i;
                }
            }
            if (mile == null) {
                return null;
            }
        }
        String key;
        Boolean val;
        Map<String, Boolean> valMap = new HashMap<>();
        if (mile != null) {
            for (Task dep : mile.getRequiredTasks()) {
                key = dep.getName();
                val = dep.checkProgress();
                valMap.put(key, val);
            }
            key = mile.getName();
            valMap.put(key, mile.checkProgress());
            result.put(key, valMap);
        } else {
            for (Task dep : t.getDependencies()) {
                key = dep.getName();
                val = dep.getProgress();
                valMap.put(key, val);
            }
            key = t.getName();
            valMap.put(key, t.getProgress());
            result.put(key, valMap);
        }
        return result;
    }

    public static Map<String, Map<String, Map<String, String>>> deadlinesApproaching(String datePattern) {
        Map<String, Map<String, Map<String, String>>> result = new HashMap<>();
        String key;
        Map<String, Map<String, String>> val;
        ArrayList<Module> modules = StudyPlannerController.getSelectedStudent().getSemesterProfile().getModules();
        for (Module m : modules) {
            key = m.getCode() + " " + m.getName();
            val = moduleItemDeadlinesApproaching(m, datePattern);
            result.put(key, val);
        }
        return result;
    }

    private static Map<String, String> taskDeadlinesApproaching(ModuleItem mi, String datePattern) {
        Map<String, String> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate d = LocalDate.now();
        LocalDate cmp;
        String key;
        String val;
        for (Task t : mi.getTasks()) {
            cmp = t.getDeadline();
            if (d.compareTo(cmp) <= 0) {
                val = formatter.format(cmp);
                key = t.getName();
                result.put(key, val);
            }
        }
        return result;
    }
    
}
