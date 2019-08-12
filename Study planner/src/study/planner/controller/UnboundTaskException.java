/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.controller;

/**
 * Exception class which should be thrown when references to tasks are called
 * in functions concerning a student which doesn't have such task.
 * @author Adrian Wesolowski
 */
public class UnboundTaskException extends Exception {

    public UnboundTaskException() {
        super();
    }
    
}
