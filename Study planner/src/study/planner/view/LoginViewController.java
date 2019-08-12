/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package study.planner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import study.planner.controller.StudyPlannerController;
import study.planner.controller.UserController;

/**
 * JavaFX Controller class for user authentication dialog
 *
 * @author Adrian Wesolowski
 */
public class LoginViewController extends AbstractDialogController
{

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize() 
    {
        super.initialize();
    }    

    @Override
    protected void submitForm(ActionEvent e) 
    {
        if(isFormValid())
        {
            if(StudyPlannerController.authenticate(usernameTextField.getText(), passwordField.getText()))
                closeWindow();
            else
            {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Authentication failed! Wrong username or password!");
                a.show();
                // failed to log in
            }
        }
    }

    @Override
    protected boolean isFormValid() 
    {
        return true;
        //return !UserController.usernameAvailable(usernameTextField.getText());
    }

    @Override
    public void loadData(Object... o) throws IllegalArgumentException 
    {
        // no loading necessary
    }  
}
