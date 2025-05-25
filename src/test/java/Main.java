package org.classroomNotifier.test;

import org.classroomNotifier.init.LoggerFactory;
import org.classroomNotifier.logger.Logger;
import classroom.notifier.FactoryClassroom;
import classroom.notifier.ClassroomNotifier;
import classroom.notifier.implement.InformadorDatos;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class Main {
    private static String EXTENSIONS_PATH = "src\\test\\resources\\extensions\\";

    public static void main(String[] args) {
        System.out.println("Initializing application...");
        
        // Initialize logger factory and create logger
        LoggerFactory loggerFactory = LoggerFactory.getInstance("src/test/resources/memory.txt");
        Logger logger = loggerFactory.createLogger();

        // Initialize test loggers
        SourceLogger sourceLogger = new SourceLogger("stockActual.json");

        // Initialize application factory and create application
        FactoryClassroom appFactory = new FactoryClassroom();
        ClassroomNotifier application = appFactory.Inicializar(sourceLogger, EXTENSIONS_PATH);
        
        // Set up observer pattern
        application.addObserver(logger);
        application.addCurrentObservers(logger.getClass().getSimpleName());        
    }
} 