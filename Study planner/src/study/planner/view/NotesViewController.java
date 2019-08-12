/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import study.planner.controller.NoteController;

public class NotesViewController extends AbstractDialogController{

    
    @FXML
    private Text taskName;

    @FXML
    private TextArea mileDesc;
    
    String taskTitle;
    private int index;
    
    
    @Override
    protected void submitForm(ActionEvent e) {
        //This is where you add the notes to the task note.
        String message = mileDesc.getText();
        if (!message.isEmpty()) {
            NoteController.addNote(taskTitle, message);
            closeWindow();
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("No Text in message");
            a.show();
        }

        
    }
    
    EventHandler<ActionEvent> eEdit = new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e)
            {
                //This is where you add the notes to the task note.
                String message = mileDesc.getText();
                if (!message.isEmpty()) {
                    NoteController.editNote(taskTitle, message, index);
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("No Text in message");
                    a.show();
                }
                
                closeWindow();
            }
    };
    
    @FXML
    @Override
    public void initialize()
    {            
        cancelBtn.setOnAction(actionEvent -> closeWindow());
        
    }
    @Override
    public void loadData(Object... o)
    {
        
        if(o.length == 2)
        {
            if(o[0] instanceof String && o[1] instanceof Integer)
            {
                loadData((String) o[0], (int) o[1]);
            }
            else
                throw new IllegalArgumentException();
        }
        else
        {
            if(o.length == 3)
            {
                if(o[0] instanceof String && o[1] instanceof Integer && o[2] instanceof Integer)
                {
                    loadData((String) o[0], (int) o[1], (int) o[2]);
                }
                else
                    throw new IllegalArgumentException();
            }
            else
                throw new IllegalArgumentException();
        }
    }
    
    public void newNote(String taskDetails){
        //to Add new command = 0
        taskTitle = taskDetails;
        taskName.setText(taskDetails);
        
        createBtn.setOnAction(actionEvent -> submitForm(actionEvent));
        
    }
    public void editNote(String taskDetails, int index, String message){
        //to edit command = 1
        taskTitle = taskDetails;
        taskName.setText(taskDetails);
        this.index = index;
        
        //editing data
        createBtn.setOnAction(eEdit); 
        createBtn.setText("EDIT");
        mileDesc.setText(message); // change to get old message
        
    }


    @Override
    protected boolean isFormValid() {
        // implement this
        return true;
    }
    

}
