/**
 * @author Michael Girard
 */
package com.github.michael_girard.scheduler;

import java.time.temporal.ChronoUnit;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestScheduler extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TestScheduler");
        Scheduler scheduler = new Scheduler(ChronoUnit.MONTHS);
        scheduler.showOnStage(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
