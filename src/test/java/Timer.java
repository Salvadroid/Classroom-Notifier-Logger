package org.classroomNotifier.test;

import org.classroomNotifier.logger.Logger;
import org.classroomNotifier.init.LoggerFactory;
import java.util.Timer;
import java.util.TimerTask;

public class Timer implements Runnable {
    private Integer timeInterval;
    private Runnable runnable;

    public Timer(Integer timeInterval, Runnable runnable) {
        this.runnable = runnable;
        this.timeInterval = timeInterval;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.timeInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runnable.run();
        }
    }
}