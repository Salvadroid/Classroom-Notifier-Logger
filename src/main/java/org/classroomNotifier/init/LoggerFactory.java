package org.classroomNotifier.init;

import org.classroomNotifier.logger.Logger;
import org.classroomNotifier.logger.Writer;

public class LoggerFactory {
    private static LoggerFactory instance;
    private Writer writer;

    private LoggerFactory() {
        this.writer = new Writer("src/test/resources/memory.txt");
    }

    public static LoggerFactory getInstance() {
        if (instance == null) {
            instance = new LoggerFactory();
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