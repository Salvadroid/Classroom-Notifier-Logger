package org.classroomNotifier.logger;
import classroom.notifier.interfaces.Observer;

public class Logger implements Observer {
    private Writer writer;

    public Logger(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void update(Object message) {
        System.out.println("Logger received message: " + message);
        writer.write(message.toString());
        writer.close();
    }
} 