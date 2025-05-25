package org.classroomNotifier.logger;

// TODO: Replace with your actual application's interface
import classroom.notifier.implement.Observer;

public class Logger implements Observer {
    private Writer writer;

    public Logger(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void update(Object message) {
        writer.write(message.toString());
    }
} 