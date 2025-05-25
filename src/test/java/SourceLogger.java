package org.classroomNotifier.test;

import org.classroomNotifier.logger.Logger;
import org.classroomNotifier.init.LoggerFactory;

public class SourceLogger {
    private Logger logger;
    
    public SourceLogger() {
        LoggerFactory factory = LoggerFactory.getInstance();
        this.logger = factory.createLogger();
    }
    
    public void logMessage(String message) {
        logger.update(message);
    }
} 