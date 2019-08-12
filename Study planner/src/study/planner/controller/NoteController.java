/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.controller;

import java.util.ArrayList;
import study.planner.model.Module;
import study.planner.model.ModuleItem;
import study.planner.model.Note;
import study.planner.model.SemesterProfile;
import study.planner.model.Task;

/**
 *
 * @author adq
 */
public class NoteController {

    public static boolean editNote(String taskName, String message, int index) {
        SemesterProfile sp = StudyPlannerController.getSelectedStudent().getSemesterProfile();
        Task t = null;
        Note note = new Note(message);
        for (Module m : sp.getModules()) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Task i : mi.getTasks()) {
                    if (i.getName().equals(taskName)) {
                        t = i;
                        t.replaceNote(index, note);
                    }
                }
            }
        }
        return true;
    }

    public static boolean addNote(String taskName, String message) {
        SemesterProfile sp = StudyPlannerController.getSelectedStudent().getSemesterProfile();
        Task t = null;
        Note note = new Note(message);
        for (Module m : sp.getModules()) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Task i : mi.getTasks()) {
                    if (i.getName().equals(taskName)) {
                        t = i;
                        t.addNote(note);
                    }
                }
            }
        }
        return true;
    }

    public static void removeNote(String taskName, int index) {
        SemesterProfile sp = StudyPlannerController.getSelectedStudent().getSemesterProfile();
        Task t = null;
        for (Module m : sp.getModules()) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Task i : mi.getTasks()) {
                    if (i.getName().equals(taskName)) {
                        t = i;
                        t.removeNote(index);
                    }
                }
            }
        }
    }

    
     public static ArrayList<String> getTaskNotes(String taskName, boolean inclComplete) throws TaskNotFoundException {
        SemesterProfile sp = StudyPlannerController.getSelectedStudent().getSemesterProfile();
        Task t = null;
        ArrayList<Note> noteList = null;
        ArrayList<String> result = new ArrayList<>();
        String notes = "";
        for (Module m : sp.getModules()) {
            for (ModuleItem mi : m.getModuleItems()) {
                for (Task i : mi.getTasks()) {
                    if (i.getName().equals(taskName)) {
                        t = i;
                        noteList = i.getNotes();
                        for (int j = 0; j < noteList.size(); j++) {
                            result.add(i.getNotes().get(j).toString());
                        }
                        
                    }
                }
                if (inclComplete) {
                    for (Task i : mi.getCompleteTasks()) {
                        if (i.getName().equals(taskName)) {
                            t = i;
                            noteList = i.getNotes();
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
    
}
