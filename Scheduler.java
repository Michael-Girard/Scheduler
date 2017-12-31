/**
 * @author Michael Girard
 */
package com.github.michael_girard.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Scheduler {
    /*
        timeSpan is an enumeration used to determine the type of
        scheduler displayed. Valid values are ChronoUnit.WEEKS and 
        ChronoUnit.MONTHS
    */
    private ChronoUnit timeSpan;         
    private LocalDateTime startDateTime;
    private List<ScheduleInfo> appointments;
    private Scene scene;
    private ResourceBundle bundle = null;
    private Font fontType = Font.getDefault();
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    //Create a scheduler starting at the current date
    public Scheduler(ChronoUnit span){
        this(span, LocalDateTime.now(), null);
    }
    
    //Create a scheduler starting at a specified date
    public Scheduler(ChronoUnit span, LocalDateTime start){
        this(span, start, null);
    }
    
    //Create a scheduler starting at the current date with a list of appointments
    public Scheduler(ChronoUnit span, List<ScheduleInfo> apptList){
        this(span, LocalDateTime.now(), apptList);
    }
    
    //Create a scheduler starting at a specified date with a list of appointments
    public Scheduler(ChronoUnit span, LocalDateTime start, List<ScheduleInfo> apptList){
        timeSpan = span;
        startDateTime = start;
        appointments = apptList;
        generateContent(null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ChronoUnit getTimeSpan() {
        return timeSpan;
    }
    
    public void setTimeSpan(ChronoUnit timeSpan) {
        switch(timeSpan){
            case WEEKS: this.timeSpan = timeSpan; break;
            case MONTHS: this.timeSpan = timeSpan; break;
            default: throw new UnsupportedOperationException("Invalid ChronoUnit supplied to the scheduler. Valid values are WEEKS and MONTHS.");
        }
    }
    
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public List<ScheduleInfo> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<ScheduleInfo> appointments) {
        this.appointments = appointments;
    }
    
    public boolean addAppointment(ScheduleInfo appointment){
        return appointments.add(appointment);
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void setScene(Scene schedulerScene) {
        this.scene = schedulerScene;
    }

    public Font getFontType() {
        return fontType;
    }

    public void setFontType(Font fontType) {
        this.fontType = fontType;
    }
    
    public Locale getLocale(){
        return bundle.getLocale();
    }
    
    /*
        Method allows the modification of the bundle for internationalization
    */
    public void setBundle(String bundleName, Locale locale){
        bundle = ResourceBundle.getBundle(bundleName, locale);
    }
    //</editor-fold>
    
    public void showOnStage(Stage stage){
        if (scene == null){
            generateContent(stage);
        }
        stage.setScene(scene);
        stage.show();
    }
    
    private void generateContent(Stage stage){
        boolean bundleExists = bundle != null;
        /*
            Root Pane
        */
        BorderPane root = new BorderPane();
        
        if (stage != null){
            /*
                Menu
            */
            MenuBar menu = new MenuBar();
            Menu menuFile = new Menu(bundleExists ? bundle.getString("menuFile") : "File");
            MenuItem miExit = new MenuItem(bundleExists ? bundle.getString("miExit") : "Exit");
            menuFile.getItems().add(miExit);    //Add Exit to File
            menu.getMenus().addAll(menuFile);   //Add File to the Menu

            miExit.setOnAction(e -> {
                try{
                    stage.hide();
                }
                catch(NullPointerException ex){
                    ex.printStackTrace();
                }
            });

            root.setTop(menu);                  //Add the Menu to the root
        }
        /*
            Node for containing the navigation controls and the schedule
        */
        BorderPane contentPane = new BorderPane();
        contentPane.prefWidthProperty().bind(root.widthProperty());
        root.setCenter(contentPane);
        
        /*
            Grid Containing Navigation Controls
        */
        GridPane controlGrid = new GridPane();
        controlGrid.setPadding(new Insets(5, 0, 5, 0));
        controlGrid.setVgap(5);
        
        //Create column constraints
        int numColumns = 3;
        List<ColumnConstraints> cc = controlGrid.getColumnConstraints();
        for (int currentColumn = 0; currentColumn < numColumns; currentColumn++){
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(Math.ceil(100.0 / numColumns));
            cc.add(col);
        }
        
        
        
        //Create Labels and Buttons
        Label lblTimeSpan = new Label("ERROR"); //These labels will be adjusted depending
        Label lblBack = new Label("ERROR");     //on the timeSpan for the schedule 
        Label lblNext = new Label("ERROR");     
        Button btnBack = new Button(bundleExists ? bundle.getString("btnBack") : "Back");
        Button btnNext = new Button(bundleExists ? bundle.getString("btnNext") : "Next");
        Separator separator = new Separator();
        
        controlGrid.add(lblBack, 0, 0);
        controlGrid.add(lblTimeSpan, 1, 0);
        controlGrid.add(lblNext, 2, 0);
        controlGrid.add(btnBack, 0, 1);
        controlGrid.add(btnNext, 2, 1);
        controlGrid.add(separator, 0, 2, 5, 1);
        
        GridPane.setHalignment(lblBack, HPos.CENTER);
        GridPane.setHalignment(lblTimeSpan, HPos.CENTER);
        GridPane.setHalignment(lblNext, HPos.CENTER);
        GridPane.setHalignment(btnBack, HPos.CENTER);
        GridPane.setHalignment(btnNext, HPos.CENTER);
        
        /*
            Grid containing the schedule
        */
        
        
        contentPane.setTop(controlGrid);
        
        /*
            Create the scene
        */
        if (scene == null){
            scene = new Scene(root, 0, 0);
        }
        else{
            scene.setRoot(root);
        }
    }
}
