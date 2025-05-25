package org.classroomNotifier.logger;

// TODO: Replace with your actual application's interface
import classroom.notifier.implement.Observer;

public class Logger implements Observer {
    private Writer writer;
    private Observer observer;

    public Logger(Writer writer) {
        this.writer = writer;
    }

    public void setApplication(Observer observer) {
        this.observer = observer;
        observer.registerObserver(this);
    }

    @Override
    public void update(String message) {
        writer.write(message);
    }

    public void stop() {
        if (observer != null) {
            observer.removeObserver(this);
        }
    }
} 