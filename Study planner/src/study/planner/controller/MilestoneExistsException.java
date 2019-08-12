/**
 *
 * @author andy_davies
 */
package study.planner.controller;

public class MilestoneExistsException extends Exception{
    
    public MilestoneExistsException(){
        
    }
    
    public MilestoneExistsException(String msg){
        super(msg);
    }
}
