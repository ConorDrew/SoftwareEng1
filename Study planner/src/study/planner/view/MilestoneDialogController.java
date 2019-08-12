package study.planner.view;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import study.planner.controller.CreatorController;
import study.planner.controller.ModuleItemNotFoundException;
import study.planner.controller.QueryController;
import study.planner.controller.ModuleNotFoundException;

/**
 * JavaFX Controller for a milestone creation form dialog.
 * @author Adrian Wesolowski, Andy Davies
 */
public class MilestoneDialogController extends AbstractDialogController{
    @FXML
    private ComboBox moduleChoice;
    @FXML
    private ComboBox moduleTaskChoice;
    @FXML
    private TextField nameBox;
    @FXML
    private TextArea mileDesc;
    @FXML
    private ListView taskList;
    @FXML
    private Button clearBtn;
    
    private String mileName;
    private String desc;
    private String moduleCode;
    private String moduleTaskType;
    private ArrayList<String> listOfTasks;

    /**
     * Checks whether the data input into the form are valid
     * @return true if valid, false if invalid
     */
    @Override
    protected boolean isFormValid(){
        boolean isValid = !moduleChoice.getSelectionModel().isEmpty() && 
                !moduleTaskChoice.getSelectionModel().isEmpty() &&
                nameBox.getText().matches("[a-zA-Z0-9 ,.]+") && 
                mileDesc.getText().matches("[a-zA-Z0-9 ,.]+") &&
                !listOfTasks.isEmpty();
        
        return isValid;
    }
    
    private void clearWindow(){
        moduleChoice.valueProperty().set(null);        
        moduleTaskChoice.valueProperty().set(null);

        nameBox.clear();
        mileDesc.clear();
        taskList.getItems().clear();
    }
    
    @Override
    protected void submitForm(ActionEvent e)
    {
        mileName = nameBox.getText();
        desc = mileDesc.getText();
        if(isFormValid()){
            try{
                // placeholder deadline
                CreatorController.constructMilestone(moduleCode, moduleTaskType, mileName, desc, listOfTasks);
            }catch(ModuleItemNotFoundException | ModuleNotFoundException ex){
                ex.printStackTrace();
            }
            closeWindow();
        }else{
            e.consume();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Error with a field. please double check");
            a.show();
        }
    }
    
    ListChangeListener<String> selectList = new ListChangeListener<String>(){
        @Override
        public void onChanged(Change<? extends String> c)
        {
            while(c.next())
            {
                for(String taskName : c.getRemoved())
                {
                    listOfTasks.remove(taskName);
                }
                for(String taskName : c.getAddedSubList())
                {
                    listOfTasks.add(taskName);
                }
            }
        }
    };
    
    ChangeListener<String> selectModule = new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
            try{
                moduleCode = newValue.split(" ")[0];
                ArrayList<String> moduleItems = QueryController.getModuleItemsNames(moduleCode);
                moduleTaskChoice.setItems(FXCollections.observableArrayList(moduleItems));
            }catch(ModuleNotFoundException ex){
                ex.printStackTrace();
            }
        }
    };
    
    ChangeListener<String> selectModuleTask = new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
            moduleTaskType = newValue;
            if(moduleTaskType != null){
                try{
                    ArrayList<String> taskNames = QueryController.getTaskNames(moduleCode, moduleTaskType, true);
                    taskList.setItems(FXCollections.observableArrayList(taskNames));
                }
                catch(ModuleNotFoundException | ModuleItemNotFoundException ex){
                    ex.printStackTrace();
                }
            }
        }
    };
    
    @FXML
    @Override
    public void initialize(){
        //createBtn.setOnAction(eCreate);
        //cancelBtn.setOnAction(actionEvent -> closeWindow());
        super.initialize();
        moduleChoice.getSelectionModel().selectedItemProperty().addListener(selectModule);
        moduleTaskChoice.getSelectionModel().selectedItemProperty().addListener(selectModuleTask);
        taskList.getSelectionModel().getSelectedItems().addListener(selectList);
        taskList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listOfTasks = new ArrayList<>();
        clearBtn.setOnAction(actionEvent -> clearWindow());
    }

    @Override
    public void loadData(Object... o)
    {
        if(o.length == 2)
        {
            if (o[0] instanceof ArrayList && o[1] instanceof ArrayList)
            {
                loadData((ArrayList<String>) o[0], (ArrayList<String>) o[1]);
            } 
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }
    
    public void loadData(ArrayList<String> modules, ArrayList<String> taskTypes){
        moduleChoice.setItems(FXCollections.observableArrayList(modules));
       // moduleTaskChoice.setItems(FXCollections.observableArrayList(taskTypes));
    }
}
