package study.planner.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import study.planner.controller.CreatorController;
import study.planner.controller.ModuleItemNotFoundException;
import study.planner.controller.QueryController;
import study.planner.controller.ModuleNotFoundException;

/**
 * JavaFX Controller for a task creation form dialog.
 * @author Adrian Wesolowski, Andy Davies
 */
public class TaskDialogController extends AbstractDialogController
{
    @FXML
    private ComboBox moduleChoice;
    @FXML
    private ComboBox moduleItemChoice;
    @FXML
    private ComboBox taskTypeChoice;
    @FXML
    private TextArea descBox;
    @FXML
    private CheckBox depChecked;
    @FXML
    private ComboBox progressTypeChoice;
    @FXML
    private TextField lengthText;
    @FXML
    private DatePicker deadlineDP;
    @FXML
    private ComboBox depTaskChoice;
    @FXML
    private TextField taskNameText;
    
    private final String pattern = "yyyy/MM/dd";
    private String taskName;
    private String moduleCode;
    private String moduleItemName;
    private String taskType;
    private String progressType;
    private String desc;
    private String taskDep;
    private String activityLen;
    private String deadline;
    
    /**
     * Checks whether the data input into the form are valid
     * @return true if valid, false if invalid
     */
    @Override
    protected boolean isFormValid(){
        boolean isValid = !moduleChoice.getSelectionModel().isEmpty() && 
                !moduleItemChoice.getSelectionModel().isEmpty() &&
                !taskTypeChoice.getSelectionModel().isEmpty() &&
                !progressTypeChoice.getSelectionModel().isEmpty() &&
                descBox.getText().matches("[a-zA-Z0-9 ,.]+") && 
                lengthText.getText().matches("[0-9]+") &&
                taskNameText.getText().matches("[a-zA-Z0-9 ,.]+");
        
        return isValid;
    }

    @Override
    protected void submitForm(ActionEvent e)
    {
        desc = descBox.getText();
        activityLen = lengthText.getText();
        StringConverter<LocalDate> converter = deadlineDP.getConverter();
        deadline = deadlineDP.getValue() != null ? 
                converter.toString(deadlineDP.getValue()) : null;
        taskName = taskNameText.getText();
        taskDep = depTaskChoice.getValue() != null ? depTaskChoice
                .getValue().toString() : null;
        ArrayList<String> taskDeps = new ArrayList<>();
        taskDeps.add(taskDep);
        if(isFormValid())
        {
            try
            {
                CreatorController.constructTask(taskName, moduleCode, 
                        moduleItemName, taskType, desc, taskDeps, progressType,
                        activityLen, deadline);
            }
            catch(ModuleNotFoundException ex)
            {
                ex.printStackTrace();
            }

            closeWindow();
        }
        else{
            e.consume();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Error with a field. please double check");
            a.show();
        }
            
    }
    
    ChangeListener<String> selectModule = new ChangeListener<String>()
    {
        @Override
        public void changed(ObservableValue<? extends String> observable, 
                String oldValue, String newValue)
        {
            try
            {
                changeDepBox(false);
                moduleCode = newValue.split(" ")[0];
                ArrayList<String> moduleItems = QueryController
                        .getModuleItemsNames(moduleCode);
                moduleItemChoice.setItems(FXCollections
                        .observableArrayList(moduleItems));
            }
            catch(ModuleNotFoundException ex)
            {
                ex.printStackTrace();
            }

        }
    };
    ChangeListener<String> selectModuleItem = new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> observable, 
                String oldValue, String newValue){
            moduleItemName = newValue;
            if(moduleItemName != null){
                try{
                    changeDepBox(false);
                    ArrayList<String> taskNames = QueryController
                            .getTaskNames(moduleCode, moduleItemName, true);
                    depTaskChoice.setItems(FXCollections
                            .observableArrayList(taskNames));
                }
                catch(ModuleNotFoundException | ModuleItemNotFoundException ex){
                    ex.printStackTrace();
                } 
            }
        }
    };
    ChangeListener<String> selectTaskType = new ChangeListener<String>()
    {
        @Override
        public void changed(ObservableValue<? extends String> observable, 
                String oldValue, String newValue)
        {
            taskType = newValue;
        }
    };
    
    ChangeListener<Boolean> dependencyCheck = new ChangeListener<Boolean>()
    {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, 
                Boolean oldValue, Boolean newValue)
        {
            changeDepBox(newValue);
        }
    };
    
    ChangeListener<String> selectProgressType = new ChangeListener<String>()
    {
        @Override
        public void changed(ObservableValue<? extends String> observable, 
                String oldValue, String newValue)
        {
            progressType = newValue;
        }
    };
    
    private void changeDepBox(boolean enable)
    {
        if(!enable)
        {
            depChecked.selectedProperty().setValue(Boolean.FALSE);
            depTaskChoice.getSelectionModel().clearSelection();
            depTaskChoice.setValue(null);
            depTaskChoice.setDisable(true);
        }
        else
        {
            depChecked.selectedProperty().setValue(Boolean.TRUE);
            depTaskChoice.setDisable(false);
        }
    }
    
    
    @FXML
    @Override
    public void initialize()
    {
        super.initialize();
        moduleChoice.getSelectionModel().selectedItemProperty()
                .addListener(selectModule);
        moduleItemChoice.getSelectionModel().selectedItemProperty()
                .addListener(selectModuleItem);
        taskTypeChoice.getSelectionModel().selectedItemProperty()
                .addListener(selectTaskType);
        depChecked.selectedProperty().addListener(dependencyCheck);
        progressTypeChoice.getSelectionModel().selectedItemProperty()
                .addListener(selectProgressType);
        deadlineDP.setConverter(dateConverter);
        deadlineDP.setDayCellFactory(picker -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate date, boolean empty)
            {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        deadlineDP.setEditable(false);
        deadlineDP.setPromptText(pattern.toLowerCase());
    }
    
    StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        @Override
        public String toString(LocalDate date) 
        {
            if(date != null)
                return formatter.format(date);
            else
                return "";
        }

        @Override
        public LocalDate fromString(String string) 
        {
            if(string != null && !string.isEmpty())
                return LocalDate.parse(string, formatter);
            else
                return null;
        }
        
    };
    
    @Override
    public void loadData(Object... o)
    {
        if(o.length == 3)
        {
            if (o[0] instanceof ArrayList && o[1] instanceof ArrayList && o[2] instanceof ArrayList)
            {
                loadData((ArrayList<String>) o[0], (ArrayList<String>) o[1], (ArrayList<String>) o[2]);
            }       
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }
    public void loadData(ArrayList<String> modules, ArrayList<String> taskTypes, ArrayList<String> progressTypes)
    {
        moduleChoice.setItems(FXCollections.observableArrayList(modules));
        taskTypeChoice.setItems(FXCollections.observableArrayList(taskTypes));
        progressTypeChoice.setItems(FXCollections.observableArrayList(progressTypes));
    } 
}
