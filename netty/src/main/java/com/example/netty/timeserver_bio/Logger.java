package com.example.netty.timeserver_bio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Logger {

    public static void info(String s) {
        try {
            File file = new File("log.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(file, true);
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println(s);
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }
}
