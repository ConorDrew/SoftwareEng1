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
public class ModuleNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ModuleNotFoundException</code> without
     * detail message.
     */
    public ModuleNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ModuleNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModuleNotFoundException(String msg) {
        super(msg);
    }
}
