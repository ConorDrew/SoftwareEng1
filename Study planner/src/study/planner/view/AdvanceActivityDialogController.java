package study.planner.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import study.planner.controller.ActivityNotFoundException;
import study.planner.controller.CreatorController;

/**
 * JavaFX Controller for progressing activity.
 * @author Adrian Wesolowski
 */
public class AdvanceActivityDialogController extends AbstractDialogController
{
    @FXML
    private ComboBox activityComboBox;
    @FXML
    private ComboBox measureComboBox;
    @FXML
    private TextField progressQtyText;
    private String progressQty;
    /*@FXML
    private Button createBtn;
    @FXML
    private Button cancelBtn;*/
    
    @Override
    protected boolean isFormValid()
    {
        return true;
    }
    
    @Override
    protected void submitForm(ActionEvent e)
    {
        progressQty = progressQtyText.getText();
        if(isFormValid())
        {
            try 
            {
                CreatorController.advanceActivity((String) activityComboBox.getSelectionModel().getSelectedItem(), Integer.parseInt(progressQty));
            } 
            catch (ActivityNotFoundException ex) 
            {
                Logger.getLogger(AdvanceActivityDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            closeWindow();
        }
        else
            e.consume();
    }
    
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
    public void loadData(Object... o)
    {
        if(o.length == 2)
        {
            if(o[0] instanceof String && o[1] instanceof String)
            {
                loadData((String) o[0], (String) o[1]);
            }
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }
    
    public void loadData(String activityName, String measure)
    {
        activityComboBox.getItems().add(activityName);
        activityComboBox.setValue(activityName);
        activityComboBox.setDisable(true);
        measureComboBox.getItems().add(measure);
        measureComboBox.setValue(measure);
        measureComboBox.setDisable(true);
    }
}
