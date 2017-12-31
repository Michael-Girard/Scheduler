/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.michael_girard.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Michael
 */
public class TestScheduler extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        final int NUMBER_OF_APPOINTMENTS = 100;
        final int CURRENT_YEAR = LocalDate.now().getYear();
        Scheduler scheduler = new Scheduler(ChronoUnit.MONTHS);
        
        for (int i = 0; i < NUMBER_OF_APPOINTMENTS; i++){
            LocalTime randomTime = LocalTime.of((int)(Math.random() * 22), 1);
            int randomMonth = (int)(Math.random() * 12 + 1);
            LocalDate randomDate = LocalDate.of(CURRENT_YEAR, randomMonth, 1);
            int daysInMonth = randomDate.getMonth().length(randomDate.isLeapYear());
            scheduler.addEntry(new SchedulerEntry(
                LocalDate.of(CURRENT_YEAR, randomMonth, Math.min((int)(Math.random() * 31 + 1), daysInMonth)),
                randomTime,
                randomTime.plusHours(1),
                "Random",
                "qwerty"
            ));       
        }
        scheduler.showOnStage();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
