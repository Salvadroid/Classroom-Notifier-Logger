package org.classroomNotifier.logger;

// TODO: Replace with your actual application's interface
import com.example.core.ApplicationInterface;

public class Logger implements Observer {
    private Writer writer;
    private ApplicationInterface application;

    public Logger(Writer writer) {
        this.writer = writer;
    }

    public void setApplication(ApplicationInterface application) {
        this.application = application;
        application.registerObserver(this);
    }

    @Override
    public void update(String message) {
        writer.write(message);
    }

    public void stop() {
        if (application != null) {
            application.removeObserver(this);
        }
    }
} 