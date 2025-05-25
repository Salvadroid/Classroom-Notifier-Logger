package org.classroomNotifier.test;

import org.classroomNotifier.logger.Logger;
import org.classroomNotifier.init.LoggerFactory;
import java.util.Timer;
import java.util.TimerTask;

public class TimerLogger {
    private Logger logger;
    private Timer timer;
    private static final long PERIOD = 5000; // 5 seconds
    private static final String MEMORY_PATH = "src/test/resources/memory.txt";
    
    public TimerLogger() {
        LoggerFactory factory = LoggerFactory.getInstance(MEMORY_PATH);
        this.logger = factory.createLogger();
        this.timer = new Timer();
    }
    
    public void run() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logger.update("Timer tick at: " + System.currentTimeMillis());
            }
        }, 0, PERIOD);
    }
    
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
} 