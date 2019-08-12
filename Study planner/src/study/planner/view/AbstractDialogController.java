package study.planner.view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Baseline JavaFX Dialog Controller class
 * @author Adrian Wesolowski
 */
public abstract class AbstractDialogController
{
    @FXML
    protected Button createBtn;
    @FXML
    protected Button cancelBtn;
    
    /**
     * Load the FXML provided to create a dialog along with its controller
     * @param stage
     * @param fxmlName
     * @param title
     * @return the JavaFX controller to the newly created dialog
     * @throws IOException 
     */
    public static AbstractDialogController injectFXML(Stage stage, String fxmlName, String title) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(AbstractDialogController.class.getResource(fxmlName));
        stage.setTitle(title);
        stage.setScene(new Scene((Pane) loader.load()));
        stage.getIcons().add(new Image("/images/uea-logo.gif"));
        return loader.<AbstractDialogController>getController();   
    }
    
    /**
     * Closes the window and, thus, the dialog.
     */
    protected void closeWindow()
    {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    /**
     * Event handler for submitting the form.
     * @param e event data
     */
    protected abstract void submitForm(ActionEvent e);
    protected abstract boolean isFormValid();
    /**
     * FXML initialisation method. Override, call this method from the child
     * class and then initialise other fields.
     */
    public void initialize()
    {
        createBtn.setOnAction(actionEvent -> submitForm(actionEvent));
        cancelBtn.setOnAction(actionEvent -> closeWindow());
    }
    /**
     * Preload controller with data from parent window.
     * The child class decides how to use it - the most obvious use would be to
     * use this method as a boilerplate for another more detailed loadData 
     * method with known list of parameters.
     * @param o parameter list
     */
    public abstract void loadData(Object... o) throws IllegalArgumentException;
}
