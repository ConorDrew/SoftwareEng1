/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.controller;

/**
 *
 * @author adq
 */
public class TaskNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>TaskNotFoundException</code> without
     * detail message.
     */
    public TaskNotFoundException() {
    }

    /**
     * Constructs an instance of <code>TaskNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TaskNotFoundException(String msg) {
        super(msg);
    }
}
