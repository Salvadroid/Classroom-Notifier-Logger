package org.classroomNotifier.test;

import org.classroomNotifier.init.LoggerFactory;
import org.classroomNotifier.logger.Logger;
import com.example.core.ApplicationFactory; // TODO: Replace with your actual application factory
import com.example.core.Application; // TODO: Replace with your actual application interface

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing application...");
        
        // Initialize logger factory and create logger
        LoggerFactory loggerFactory = LoggerFactory.getInstance();
        Logger logger = loggerFactory.createLogger();
        
        // Initialize application factory and create application
        ApplicationFactory appFactory = new ApplicationFactory(); // TODO: Replace with your actual factory
        Application application = appFactory.createApplication();
        
        // Initialize test loggers
        SourceLogger sourceLogger = new SourceLogger();
        TimerLogger timerLogger = new TimerLogger();
        
        // Set up observer pattern
        application.addObserver(logger);
        application.addCurrentObserver(logger.getClass().getSimpleName());
        
        // Run timer
        timerLogger.run();
    }
} 