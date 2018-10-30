package log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ServerLogSystem {
    private static final String LOG_FILE = "serverLogs.txt";

    public ServerLogSystem(){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(LOG_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.close();
    }

    public void writeLog(String log){
        try {
            Files.write(Paths.get(LOG_FILE), (log+"\n").getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
