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
public class InvalidSemesterProfileNameException extends Exception {

    /**
     * Creates a new instance of
     * <code>InvalidSemesterProfileNameException</code> without detail message.
     */
    public InvalidSemesterProfileNameException() {
    }

    /**
     * Constructs an instance of
     * <code>InvalidSemesterProfileNameException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public InvalidSemesterProfileNameException(String msg) {
        super(msg);
    }
}
