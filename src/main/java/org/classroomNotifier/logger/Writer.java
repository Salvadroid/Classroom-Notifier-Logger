package org.classroomNotifier.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Writer {
    private String filePath;
    private PrintWriter writer;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Writer(String filePath) {
        this.filePath = filePath;
        try {
            this.writer = new PrintWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        writer.println("Fecha: " + timestamp + " - " + message);
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
} 