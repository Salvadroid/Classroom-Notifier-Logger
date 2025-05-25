package org.classroomNotifier.test;

import org.classroomNotifier.init.LoggerFactory;
import org.classroomNotifier.logger.Logger;
import classroom.notifier.FactoryClassroom; // TODO: Replace with your actual application factory
import classroom.notifier.ClassroomNotifier; // TODO: Replace with your actual application interface

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing application...");
        
        // Initialize logger factory and create logger
        LoggerFactory loggerFactory = LoggerFactory.getInstance("src/test/resources/memory.txt");
        Logger logger = loggerFactory.createLogger();

        // Initialize application factory and create application
        FactoryClassroom appFactory = new FactoryClassroom(); // TODO: Replace with your actual factory
        ClassroomNotifier application = appFactory.createApplication();
        
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