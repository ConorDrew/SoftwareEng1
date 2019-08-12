/**
 *
 * @author andy_davies
 */
package study.planner.controller;

public class MilestoneNotFoundException extends Exception {
    
    public MilestoneNotFoundException(){
        
    }
    
    public MilestoneNotFoundException(String msg){
        super(msg);
    }
}
