package study.planner.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import study.planner.controller.DashboardController;
import study.planner.controller.ModuleItemNotFoundException;
import study.planner.controller.NoteController;
import study.planner.controller.QueryController;
import study.planner.controller.StudyPlannerController;
// possibly: move exceptions and/or handling to the controller
import study.planner.controller.ModuleNotFoundException;
import study.planner.controller.TaskNotFoundException;

/**
 * FXML Controller class
 *
 * @author Conor Drew and Adrian Wesolowski
 */
public class ViewController implements Initializable {

    @FXML
    private Text getName;
    
    @FXML
    private Text getID;
    
    @FXML
    private Text getSchool;
    
    @FXML
    private Text getYear;

    @FXML
    private FlowPane content;
    
    @FXML
    private ListView<String> notificationBox;
    
    @FXML
    private ListView<String> alertBox;

    @FXML
    private AnchorPane navigationBox;
    
    @FXML
    private MenuButton modulesBtn;
    
    @FXML
    private Button studyDashboardBtn;
    
    @FXML
    private TabPane dashboardTabPane;
    
    @FXML
    private Button semesterProfileBtn;
    
    @FXML
    private MenuButton activitiesBtn;
    
    @FXML
    private MenuButton tasksBtn;
    
    @FXML
    private MenuItem newTaskOption;
    
    @FXML
    private MenuButton milestonesBtn;
    
    @FXML
    private MenuItem newMilestoneOption;
    
    private Stage child;
    private Map<String, Map<String, Map<String, String>>> deadlines;
    private final String pattern = "dd-MM-yyyy";
    
    EventHandler<ActionEvent> chooseActivityType = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            MenuItem mi = (MenuItem) e.getSource();
            String taskType = mi.getText();
            // don't include complete activities
            ArrayList<String> activityData = QueryController.getActivityDetails(taskType, false);
            content.getChildren().clear();
            for(String s : activityData)
            {
                String prefix = s.split(" ")[0];
                s = s.substring(prefix.length() + 1);
                Button advanceActivity = new Button("Progress");
                // identify button by activity name
                content.getChildren().add(new Text("\n" + s));
                content.getChildren().add(advanceActivity);
                s = s.split("\n")[0];
                advanceActivity.setUserData(s);
                advanceActivity.setOnAction(eProgressActivity);
            }

        }
    };
    EventHandler<ActionEvent> eProgressActivity = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            try
            {
                child = new Stage();
                AdvanceActivityDialogController controller = (AdvanceActivityDialogController) AbstractDialogController.injectFXML(child, "AdvanceActivityView.fxml", "Progress Activity");
                String activityName = (String) ((Button) e.getSource()).getUserData();
                controller.loadData(activityName, QueryController.getActivityType(activityName));
                child.setOnHidden(event -> updateInterface());
                child.show();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    };
    /**
     * Displays the module information in the content text area.
     */
    EventHandler<ActionEvent> chooseModule = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            MenuItem module = (MenuItem) e.getSource();
            String moduleData;
            try
            {
                String[] tmp = module.getText().split(" ");
                moduleData = QueryController.getModuleData(tmp[0]);
                content.getChildren().clear();
                // to do: something more sophisticated than just plaintext
                content.getChildren().add(new Text(moduleData));
            }
            catch(ModuleNotFoundException ex)
            {
                ex.printStackTrace();
            }
            // select the clicked module and change the view to display it
        }
    };
    // to do: perhaps a more general event handler for creating a new dialog
    /**
     * Handler for new milestone creation. Results in creating a dialog based on
     * MilestoneDialogController JavaFX Controller class.
     */
    EventHandler<ActionEvent> eNewMilestone = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            try
            {
                child = new Stage();
                MilestoneDialogController controller = (MilestoneDialogController) AbstractDialogController.injectFXML(child, "NewMilestoneView.fxml", "Create New Milestone");
                controller.loadData(QueryController.getModuleNames(), QueryController.getTaskTypes());
                child.setOnHidden(event -> updateInterface());
                child.show();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    };
    
    /**
     * Handler for new task creation. Results in creating a dialog based on
     * TaskDialogController JavaFX Controller class.
     */
    EventHandler<ActionEvent> eNewTask = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            try
            {
                child = new Stage();
                TaskDialogController controller = (TaskDialogController) AbstractDialogController.injectFXML(child, "NewTaskView.fxml", "Create New Task");
                // to do: perhaps an abstract controller class
                controller.loadData(QueryController.getModuleNames(), QueryController.getTaskTypes(), QueryController.getActivityTypes());
                child.setOnHidden(event -> updateInterface());
                child.show();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    };
    
    EventHandler<ActionEvent> eDisplayMilestone = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            content.getChildren().clear();
            MenuItem mi = (MenuItem) e.getSource();
            String milestoneDetails = QueryController.getMilestoneDetails((String) mi.getUserData(), true);
            content.getChildren().add(new Text(milestoneDetails));
        }
    };
    
    /**
     * Displays a task in the content text area.
     */
    EventHandler<ActionEvent> eDisplayTask = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            // display selected task
            content.getChildren().clear();
            try
            {
                MenuItem mi = (MenuItem) e.getSource();
                ArrayList<String> taskDetails = QueryController.getTaskDetails((String) mi.getUserData(), false);
                        
                ArrayList<String> notes = NoteController.getTaskNotes((String) mi.getUserData(), false);

                
                Button addNote = new Button("Add Note");
                content.getChildren().clear();
                for (int i = 0; i < taskDetails.size(); i++) {
                    content.getChildren().add(new Text(taskDetails.get(i)));                
                }
                
                content.getChildren().add(new Text("Notes:"));
                addNote.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        try{
                            child = new Stage();
                            NotesViewController controller2 = (NotesViewController) AbstractDialogController.injectFXML(child, "NotesView.fxml", "Add Note");
                            controller2.newNote((String) mi.getUserData());
                            child.setOnHidden(event -> updateInterface());
                            child.show();
                        }
                        catch(IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
                content.getChildren().add(addNote);
                content.getChildren().add(new Text("\n"));
                for (int i = 0; i < notes.size(); i++) {
                    int index = i;
                    content.getChildren().add(new Text(notes.get(i).toString()));
                    
                    Button deleteButton = new Button();
                    Button editButton = new Button();

                    deleteButton.setText("DELETE");
                    deleteButton.setOnAction((ActionEvent) -> {
                        NoteController.removeNote(mi.getUserData().toString(),index);
                        updateNotes();
                    });
                    editButton.setText("EDIT");
                    editButton.setOnAction((ActionEvent) -> {                        
                        try{
                            child = new Stage();
                            NotesViewController controller2 = (NotesViewController) AbstractDialogController.injectFXML(child, "NotesView.fxml", "Edit Note");
                            controller2.editNote((String) mi.getUserData(), index, notes.get(index));
                            child.setOnHidden(event -> updateInterface());
                            child.show();
                            
                        }
                        catch(IOException ex)
                        {
                            ex.printStackTrace();
                        }
                        updateNotes();
                    });
                    
                    content.getChildren().add(deleteButton);
                    content.getChildren().add(editButton);                    
                    
                    content.getChildren().add(new Text("\n"));
                }
                updateNotes();
            }
            catch(TaskNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }
        
    };

    EventHandler<ActionEvent> eUpdateDashboard = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent e)
        {
            dashboardTabPane.getTabs().clear();
            deadlines = DashboardController.deadlinesApproaching("dd-MM-yyyy");
            content.getChildren().clear();
            Tab tab;
            FlowPane spoiler;
            for(String m : deadlines.keySet())
            {
                tab = null;
                spoiler = new FlowPane(Orientation.VERTICAL);
                spoiler.setColumnHalignment(HPos.LEFT);
                spoiler.setPrefWrapLength(500);
                Set<String> a = deadlines.get(m).keySet();
                if(!a.isEmpty())
                {
                    tab = new Tab();
                    spoiler.getChildren().add(new Text("\n\nModule: " + m));
                    tab.setText(m);
                    tab.setContent(spoiler);
                }
                for(String mi : a)
                {

                    spoiler.getChildren().add(new Text("\nModule Item: " + mi));
                    spoiler.getChildren().add(new Text("\n" + deadlines.get(m).get(mi).get(mi) + "\n"));
                    FlowPane spoiler2 = new FlowPane(Orientation.VERTICAL);
                    spoiler.setColumnHalignment(HPos.LEFT);
                    spoiler.setPrefWrapLength(500);
                    Map<String, Map<String, Boolean>> progressMap = null;
                    for(Map.Entry<String, String> s : deadlines.get(m).get(mi).entrySet())
                    {
                        Text tmp = new Text("\n" + s.getKey());
                        tmp.setFont(Font.font("Arial", FontWeight.BOLD, 14));                        
                        spoiler2.getChildren().add(tmp);
                        tmp = new Text("\n" + s.getValue());
                        tmp.setFont(Font.font("Arial", FontWeight.BOLD, 14));                              
                        spoiler2.getChildren().add(tmp);
                        try
                        {
                            progressMap = DashboardController.getItemProgress(m.split(" ")[0], mi, s.getKey());
                            if(progressMap != null)
                            {
                                int current = 0;
                                int max = progressMap.get(s.getKey()).entrySet().size();
                                for(Map.Entry<String, Boolean> entry : progressMap.get(s.getKey()).entrySet())
                                {
                                    if(entry.getValue())
                                        current++;
                                    spoiler2.getChildren().add(new Text("\n" + entry.getKey()));
                                    spoiler2.getChildren().add(new Text("\nCompleted: " + (entry.getValue() ? "yes\n" : "no\n")));
                                }
                                ProgressBar progress = new ProgressBar();
                                progress.setProgress((double) current / (double) max);
                                spoiler2.getChildren().add(progress);
                                //spoiler2.getChildren().add(new Text("\n"));
                            }
                        }
                        catch (ModuleNotFoundException | ModuleItemNotFoundException | TaskNotFoundException ex) 
                        {
                            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    Button spoiler2Btn = new Button("Spoiler");
                    spoiler2.managedProperty().bind(spoiler2.visibleProperty());
                    spoiler2.setVisible(false);
                    spoiler.getChildren().add(spoiler2Btn);                    
                    spoiler.getChildren().add(spoiler2);
                    EventHandler<ActionEvent> eDisplaySpoiler2 = new EventHandler<ActionEvent>()
                    {
                        @Override
                        public void handle(ActionEvent e)
                        {
                            if(spoiler2.isVisible())
                                spoiler2.setVisible(false);
                            else
                                spoiler2.setVisible(true);
                        }
                    };
                    spoiler2Btn.setOnAction(eDisplaySpoiler2);
                }
                if(tab != null)
                    dashboardTabPane.getTabs().add(tab);
            }
            dashboardTabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> 
            {
                content.getChildren().clear();
                content.getChildren().add(nv.getContent());
            });
            updateAlerts();
        }
    };
    
    
    private void updateModules()
    {
        ArrayList<String> moduleNames = QueryController.getModuleNames();
        MenuItem temp;
        modulesBtn.getItems().clear();
        for(String entry : moduleNames)
        {  
            modulesBtn.getItems().add(temp = new MenuItem(entry));
            //modules.getChildren().add(temp);
            temp.setOnAction(chooseModule);
        }
    }
    
    private void updateNotes(){
        System.out.println("UPDATE");
    }
    
    private void updateActivities()
    {
        ArrayList<String> taskTypes = QueryController.getTaskTypes();
        activitiesBtn.getItems().clear();
        MenuItem tmp;
        for(String taskType : taskTypes)
        {
            activitiesBtn.getItems().add(tmp = new MenuItem(taskType));
            tmp.setOnAction(chooseActivityType);
        }
    }
    
    /**
     * Get all task names and assign on action handlers to menu items representing
     * those tasks.
     */
    private void updateTasks()
    {
        ArrayList<String> taskNames = QueryController.getTaskNames();
        tasksBtn.getItems().clear();
        tasksBtn.getItems().add(newTaskOption);
        for(String name : taskNames)
        {
            MenuItem temp = new MenuItem();
            temp.setUserData(name);
            // trim text
            // possibly: trim entire words, not characters
            // also, create a constant for default text wrap size
            if(name.length() > 16)
                name = name.substring(0, 16) + "...";
            temp.setText(name);
            tasksBtn.getItems().add(temp);
            temp.setOnAction(eDisplayTask);
        }
        newTaskOption.setOnAction(eNewTask);
    }
    
    private void updateMilestones(){
        ArrayList<String> mileNames = QueryController.getMilestoneNames(false);
        milestonesBtn.getItems().clear();
        milestonesBtn.getItems().add(newMilestoneOption);
        for(String name : mileNames)
        {
            MenuItem temp = new MenuItem();
            temp.setUserData(name);
            temp.setText(name);
            milestonesBtn.getItems().add(temp);
            temp.setOnAction(eDisplayMilestone);
        }
        newMilestoneOption.setOnAction(eNewMilestone);
    }
    
    private void updateDashboard()
    {
        studyDashboardBtn.setOnAction(eUpdateDashboard);
    }
    
    EventHandler<MouseEvent> eAlertDetails = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent e)
        {
            if(e.getClickCount() == 2)
            {
                String content = (String) ((ListCell) e.getSource()).getUserData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
                alert.show();
            }
        }
    };
    
    private void updateAlerts()
    {
        alertBox.getItems().clear();
        alertBox.setCellFactory(picker -> new ListCell<String>()
        {
            @Override
            public void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty)
                {
                    setText(null);
                    setOnMouseClicked(null);
                }
                else
                {
                    String[] lines = item.split("\n");
                    if(lines.length >= 4)
                    {
                        String text = item.split("\n")[3];
                        setUserData(item);
                        setText(text);
                        setOnMouseClicked(eAlertDetails);
                    }
                    else
                        setText(item);
                }
            }
        });
        if(deadlines == null)
        {
            alertBox.getItems().add("Upcoming Deadlines");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date;
        long daysBetween;
        StringBuilder moduleDetails;
        StringBuilder moduleItemDetails;
        StringBuilder details;
        String cellText;
        for(String m : deadlines.keySet())
        {
            moduleDetails = new StringBuilder();
            moduleDetails.append("Module: ").append(m);
            for(String mi : deadlines.get(m).keySet())
            {
                moduleItemDetails = new StringBuilder(moduleDetails);
                moduleItemDetails.append("\nModule Item: ").append(mi);
                for(Map.Entry<String, String> entry : deadlines.get(m).get(mi).entrySet())
                {
                    date = LocalDate.parse(entry.getValue(), formatter);
                    daysBetween = DAYS.between(LocalDate.now(), date);
                    if(daysBetween <= 14 && daysBetween > 0)
                    {
                        details = new StringBuilder(moduleItemDetails);
                        details.append("\nDeadline: ").append(entry.getValue()).append("\n");
                        cellText = entry.getKey() + " " + entry.getValue();
                        details.append(cellText);
                        alertBox.getItems().add(details.toString());
                    }
                }
            }           
        }
    }
    
    public void updateInterface()
    {
        updateModules();
        updateActivities();
        updateTasks();
        updateMilestones();
        updateDashboard();
        updateAlerts();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // possibly: move student update to updateInterface()
        String[] data = QueryController.getStudentData();
        updateStudent(data[0], data[1], data[2], data[3]);
        semesterProfileBtn.setOnAction(event -> loadSemesterProfile());
        semesterProfileBtn.setDisable(true);
        notificationBox.getItems().addAll("Module Update", "New Assignment");
        //alertBox.getItems().add("Upcoming Deadlines");
        updateInterface();
    }    
    
    public void updateStudent(String name, String id, String school, String year)
    {
        getName.setText(name);
        getID.setText(id);
        getSchool.setText(school);
        getYear.setText(year);
    }
    
    public void loadSemesterProfile()
    {
        File f = null;
        do
        {
            f = chooseSemesterProfileFile();           
        }
        while(f == null);
        StudyPlannerController.loadSemesterProfile(f);
    }
    
    public File chooseSemesterProfileFile()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Semester Profile File");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("JSON File", "*.json"),
                new ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fc.showOpenDialog(semesterProfileBtn.getScene().getWindow());
        return selectedFile;
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("UEA STUDY PLANNER");
        stage.getIcons().add(new Image("/images/uea-logo.gif"));
        stage.setScene(scene);
        stage.show();
        if(!StudyPlannerController.isSemesterProfileAssigned())
        {
            loadSemesterProfile();
        }
        // to do: periodic update of interface
        /*
        Task task = new Task<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                //int i = 0;
                while(true)
                {
                    Platform.runLater
                    (
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    updateInterface();
                                }
                                
                            }
                    );
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();*/
    }
}
