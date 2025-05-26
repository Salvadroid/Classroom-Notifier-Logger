package org.classroomNotifier.init;

import org.classroomNotifier.logger.Logger;
import org.classroomNotifier.logger.Writer;

public class LoggerFactory {
    private static LoggerFactory instance;
    private Writer writer;
    private String memoryPath;

    public LoggerFactory(String memoryPath) {
        this.memoryPath = memoryPath;
        this.writer = new Writer(memoryPath);
    }

    public static LoggerFactory getInstance(String memoryPath) {
        if (instance == null) {
            instance = new LoggerFactory(memoryPath);
        }
        return instance;
    }

    public Logger createLogger() {
        return new Logger(writer);
    }

    public Writer getWriter() {
        return writer;
    }
} 