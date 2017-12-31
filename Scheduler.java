/**
 * @author Michael Girard
 */
package com.github.michael_girard.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
    private List<ScheduleInfo> entries;
    private Scene scene;
    private ResourceBundle bundle = null;
    private Font fontType = Font.getDefault();
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    //Create a scheduler starting at the current date
    public Scheduler(ChronoUnit span){
        this(span, LocalDateTime.now(), new ArrayList());
    }
    
    //Create a scheduler starting at a specified date
    public Scheduler(ChronoUnit span, LocalDateTime start){
        this(span, start, new ArrayList());
    }
    
    //Create a scheduler starting at the current date with a list of appointments
    public Scheduler(ChronoUnit span, List<ScheduleInfo> apptList){
        this(span, LocalDateTime.now(), apptList);
    }
    
    //Create a scheduler starting at a specified date with a list of appointments
    public Scheduler(ChronoUnit span, LocalDateTime start, List<ScheduleInfo> apptList){
        switch(span){
            case WEEKS: this.timeSpan = span; break;
            case MONTHS: this.timeSpan = span; break;
            default: throw new UnsupportedOperationException("Invalid ChronoUnit supplied to the scheduler. Valid values are WEEKS and MONTHS.");
        }

        startDateTime = start;
        entries = apptList;
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

    public List<ScheduleInfo> getEntries() {
        return entries;
    }

    public void setEntries(List<ScheduleInfo> appointments) {
        this.entries = appointments;
    }
    
    public boolean addEntry(ScheduleInfo appointment){
        return entries.add(appointment);
    }
    
    public Scene getScene() {
        generateContent(null);
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
        generateContent(stage);
        switch(timeSpan){
            case WEEKS: stage.setTitle("Weekly Calendar"); break;
            case MONTHS: stage.setTitle("Monthly Calendar"); break;
            default: throw new RuntimeException();
        }
        stage.setScene(scene);
        stage.show();
    }
    
    private void generateContent(Stage stage){
        final int BORDERS = 5;
        final double FONT_SIZE_LARGE = Font.getDefault().getSize() * 2;
        final double FONT_SIZE = Font.getDefault().getSize() * 1.3;
        boolean bundleExists = bundle != null;      //Check if there's a bundle being used
        
        /*
            Root Pane
        */
        BorderPane root = new BorderPane();
        
        //If we're showing the calendar on its own stage, create a Menu
        if (stage != null){
            /*
                Menu
            */
            MenuBar menu = new MenuBar();
            Menu menuFile = new Menu(bundleExists ? bundle.getString("menuFile") : "File");
            MenuItem miExit = new MenuItem(bundleExists ? bundle.getString("miExit") : "Exit");
            menuFile.getItems().add(miExit);    //Add Exit to File
            menu.getMenus().addAll(menuFile);   //Add File to the Menu

            //<editor-fold defaultstate="collapsed" desc="miExit event handler">
            miExit.setOnAction(e -> {
                try{
                    stage.hide();
                }
                catch(NullPointerException ex){
                    ex.printStackTrace();
                }
            });
            //</editor-fold>

            root.setTop(menu);                  //Add the Menu to the root
        }
        /*
            Node for containing the navigation controls and the schedule
        */
        BorderPane contentPane = new BorderPane();
        contentPane.prefWidthProperty().bind(root.widthProperty()); //Set the contentPane to fill the parent's width
        root.setCenter(contentPane);
        
        /*
            Grid Containing Navigation Controls
        */
        GridPane controlGrid = new GridPane();
        controlGrid.setPadding(new Insets(BORDERS, 0, BORDERS, 0));
        controlGrid.setVgap(BORDERS);
        
        //Create column constraints
        int numColumns = 3;
        List<ColumnConstraints> cc = controlGrid.getColumnConstraints();
        for (int currentColumn = 0; currentColumn < numColumns; currentColumn++){
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(Math.ceil(100.0 / numColumns));
            cc.add(col);
        }
        
        //Create Labels and Buttons
        Label lblTimeSpan = new Label("lblTimeSpan");   //These labels will be adjusted depending
        Label lblBack = new Label("lblBack");           //on the timeSpan for the schedule 
        Label lblNext = new Label("lblNext");
        Button btnBack = new Button("<--");
        Button btnNext = new Button("-->");
        Separator separator = new Separator();
        
        lblTimeSpan.setFont(Font.font(FONT_SIZE_LARGE));
        lblBack.setFont(Font.font(FONT_SIZE));
        lblNext.setFont(Font.font(FONT_SIZE));
        
        lblTimeSpan.setId("lblTimeSpan");
        lblBack.setId("lblBack");
        lblNext.setId("lblNext");
        btnBack.setId("btnBack");
        btnNext.setId("btnNext");
        
        controlGrid.add(lblBack, 0, 0);
        controlGrid.add(lblTimeSpan, 1, 0, 1, 2);
        controlGrid.add(lblNext, 2, 0);
        controlGrid.add(btnBack, 0, 1);
        controlGrid.add(btnNext, 2, 1);
        controlGrid.add(separator, 0, 2, 5, 1);
        
        GridPane.setValignment(lblTimeSpan, VPos.CENTER);
        GridPane.setHalignment(lblBack, HPos.CENTER);
        GridPane.setHalignment(lblTimeSpan, HPos.CENTER);
        GridPane.setHalignment(lblNext, HPos.CENTER);
        GridPane.setHalignment(btnBack, HPos.CENTER);
        GridPane.setHalignment(btnNext, HPos.CENTER);
        
        /*
            Add the controlGrid to the contentPane
        */
        contentPane.setTop(controlGrid);
        
        /*
            Grid containing the schedule
        */
        GridPane calendarGrid = new GridPane();
        calendarGrid.setVgap(BORDERS);
        
        //Create column constraints
        numColumns = 7;
        cc = calendarGrid.getColumnConstraints();
        for (int currentColumn = 0; currentColumn < numColumns; currentColumn++){
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(Math.ceil(100.0 / numColumns));
            cc.add(col);
        }
        
        //Create and style day labels
        Label lblSunday = new Label(bundleExists ? bundle.getString("lblSunday") : "Sunday");
        Label lblMonday = new Label(bundleExists ? bundle.getString("lblMonday") : "Monday");
        Label lblTuesday = new Label(bundleExists ? bundle.getString("lblTuesday") : "Tuesday");
        Label lblWednesday = new Label(bundleExists ? bundle.getString("lblWednesday") : "Wednesday");
        Label lblThursday = new Label(bundleExists ? bundle.getString("lblThursday") : "Thursday");
        Label lblFriday = new Label(bundleExists ? bundle.getString("lblFriday") : "Friday");
        Label lblSaturday = new Label(bundleExists ? bundle.getString("lblSaturday") : "Saturday");
        
        GridPane.setHalignment(lblSunday, HPos.CENTER);
        GridPane.setHalignment(lblMonday, HPos.CENTER);
        GridPane.setHalignment(lblTuesday, HPos.CENTER);
        GridPane.setHalignment(lblWednesday, HPos.CENTER);
        GridPane.setHalignment(lblThursday, HPos.CENTER);
        GridPane.setHalignment(lblFriday, HPos.CENTER);
        GridPane.setHalignment(lblSaturday, HPos.CENTER);
        
        calendarGrid.add(lblSunday, 0, 0);
        calendarGrid.add(lblMonday, 1, 0);
        calendarGrid.add(lblTuesday, 2, 0);
        calendarGrid.add(lblWednesday, 3, 0);
        calendarGrid.add(lblThursday, 4, 0);
        calendarGrid.add(lblFriday, 5, 0);
        calendarGrid.add(lblSaturday, 6, 0);
        
        /*
            Add the calendarGrid to the contentPane
        */
        contentPane.setCenter(calendarGrid);
        
        //Create the calendar
        switch(timeSpan){
            case WEEKS: createWeeklyCalendar(contentPane); break;
            case MONTHS: createMonthlyCalendar(contentPane); break;
            default: throw new RuntimeException();      //Should never happen
        }
        
        
        
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
    
    private void createWeeklyCalendar(BorderPane contentPane){
        boolean bundleExists = bundle != null;      //Check if there's a bundle being used
        LocalDate currentDay;
        
        /*
            Using startDateTime, we need to find the Sunday beginning that week.
            If the day of the week in startDateTime is already Sunday, just use that.
            Otherwise, it needs to get the date for last sunday. This is done by
                calling LocalDate.with(DayOfWeek.SUNDAY), which moves to the next 
                Sunday, and then backing up a week to the right sunday with minusDays.
        */
        if (!startDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            currentDay = startDateTime.with(DayOfWeek.SUNDAY).minusDays(7).toLocalDate();  
        }
        else{
            currentDay = startDateTime.toLocalDate();  
        }
        
        /*
            Time to set the labels to their correct text and give the buttons event handlers. 
            I can get the controlPane from the contentPane, then get the children from the 
                controlPane, turn it into a stream, and check each node to see if it's a
                label or button. Then, I can use getId to see if I have the
                right node, then perform the necessary modification.
        */
        final LocalDate dateForLambda = currentDay;
        System.out.println(currentDay + " " + dateForLambda);
        ((GridPane)contentPane.getTop()).getChildren()
                .stream()
                .forEach(node -> {
                    if (node instanceof Label){
                        switch (((Label) node).getId()){
                            case "lblBack":
                                ((Label) node).setText(bundleExists ? bundle.getString("lblBack") : "Previous Week");
                                break;
                            case "lblNext":
                                ((Label) node).setText(bundleExists ? bundle.getString("lblNext") : "Next Week");
                                break;
                            case "lblTimeSpan":
                                ((Label) node).setText(bundleExists ? bundle.getString("lblTimeSpan") : 
                                        "Week of " + dateForLambda.getMonth() + " " + dateForLambda.getDayOfMonth());
                                break;
                        }
                    }
                    if (node instanceof Button){
                        switch(((Button) node).getId()){
                            case "btnBack":
                                ((Button) node).setOnAction(e -> {
                                    startDateTime = startDateTime.minusWeeks(1);
                                    createWeeklyCalendar(contentPane);
                                }); break;
                            case "btnNext":
                                ((Button) node).setOnAction(e -> {
                                    startDateTime = startDateTime.plusWeeks(1);
                                    createWeeklyCalendar(contentPane);
                                }); break;
                        }
                    }
                });
        
        /*
            And now to create the days. Each day contains a ScrollPane, which
                allows any number of appointments of any length to fit inside of
                each day.
            A FlowPane is inserted into each ScrollPane, since a ScrollPane can
                only have a single child. The FlowPane is set to wrap after each
                child, and appointments are added to the FlowPane.
        */
        GridPane calendarPane = (GridPane)contentPane.getCenter();
        final int DAYS_IN_WEEK = 7;
        int currentDayInWeek;
        
        for (currentDayInWeek = 0; currentDayInWeek < DAYS_IN_WEEK; currentDayInWeek++){
            //Make and style a scrollpane
            ScrollPane day = new ScrollPane();              
            day.setStyle("-fx-background: white;");
            day.prefHeightProperty().bind(calendarPane.heightProperty());
            
            //Make and style a FlowPane
            FlowPane daysAppointments = new FlowPane();     //...to hold this flowpane...
            daysAppointments.setPrefWrapLength(0);          //...to hold all the appointments
            daysAppointments.prefWidthProperty().bind(day.widthProperty().subtract(2));
            
            /*
                For each entry, check if the entry is for the current day.
            */
            if (entries != null){
                for (ScheduleInfo entry : entries){
                    if (entry.getStartDate().isEqual(currentDay)){
                        //Create a label to display the appointment in a scrollpane
                        Label lblAppointment = new Label(entry.toString());
                        lblAppointment.setStyle("-fx-border-color: black;");

    //                    lblAppointment.setUserData(entry);      //Add the appointment to the UserData, which lets me get it from the event handler
    //                    //Event handler to let operations happen on double-click
    //                    lblAppointment.setOnMousePressed(e ->{
    //                        if (e.getClickCount() >= 2){
    //                            //Not implemented
    //                        }
    //                    });
                        daysAppointments.getChildren().add(lblAppointment);
                    }
                }
            }
            
            day.setContent(daysAppointments);
            currentDay = currentDay.plusDays(1);
            calendarPane.add(day, currentDayInWeek, 1);
        }
        
        
    }
    
    private void createMonthlyCalendar(BorderPane contentPane){
        
    }
}
