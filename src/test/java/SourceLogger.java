package org.classroomNotifier.test;

import org.classroomNotifier.logger.Logger;
import org.classroomNotifier.init.LoggerFactory;

public class SourceLogger {
    private Logger logger;
    private static final String MEMORY_PATH = "src/test/resources/memory.txt";
    
    public SourceLogger() {
        LoggerFactory factory = LoggerFactory.getInstance(MEMORY_PATH);
        this.logger = factory.createLogger();
    }
    
    public void logMessage(String message) {
        logger.update(message);
    }
} 