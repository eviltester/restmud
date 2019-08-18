package uk.co.compendiumdev.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogFile {
    private final String fileName;
    private boolean createBackups;
    private File logFile;

    public SimpleLogFile(String logFileName) {
        this.fileName = logFileName;
        this.createBackups=true;
        logFile=null;
    }

    public SimpleLogFile withBackups(boolean createBackups) {
        this.createBackups = createBackups;
        return this;
    }

    public SimpleLogFile create() {

        logFile = new File(this.fileName);

        if (logFile.exists()) {
            // backup any existing log files unless arg nobackups is present
            if (this.createBackups) {
                try {
                    BasicFileAttributes attr = Files.readAttributes(logFile.toPath(), BasicFileAttributes.class);

                    String newFileName = timestampForFile(attr.creationTime().toMillis()) + this.fileName;
                    Files.move(logFile.toPath(), new File(newFileName).toPath());

                } catch (IOException e) {
                    System.out.println("Could not backup " + this.fileName);
                    e.printStackTrace();
                }

            }
        }


        // create file now to allow writing
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create " + this.fileName + " at " + logFile.getAbsolutePath());
            e.printStackTrace();
            logFile=null; // did not create, disallow writing
        }

        return this;
    }


    private String timestampForFile(long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss-");
        Date date = new Date(l);
        return formatter.format(date);
    }

    public void append(String toWrite) {
        if(logFile==null){
            return;
        }

        try {
            Files.write(logFile.toPath(), toWrite.getBytes(), StandardOpenOption.APPEND);
        } catch (final IOException e) {
                System.out.println("Could not write to " + logFile.getName());
                e.printStackTrace();
        }
    }
}
