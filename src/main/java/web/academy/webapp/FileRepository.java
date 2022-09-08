package web.academy.webapp;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringJoiner;

public class FileRepository {

    private final String pathname;

    public FileRepository(String pathname) {
        this.pathname = pathname;
    }

    public boolean fileExist(String requestPathInfo) {
        File file = new File(pathname + requestPathInfo);
        return file.exists();
    }

    public String getFileContent(String requestPathInfo) {
        File file = new File(pathname + requestPathInfo);
        StringJoiner joiner = new StringJoiner(", ");

        try (Scanner output = new Scanner(file)) {
            while (output.hasNextLine()) {
                joiner.add(output.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "Data in " + file.getName() + ": " + joiner;
    }

    public boolean createFile(String requestPathInfo) {
        File file = new File(pathname + requestPathInfo);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateFile(JsonObject jsonData, String requestPathInfo) {
        String stringData = jsonData.get("content").getAsString();
        try (FileWriter fileWriter = new FileWriter(pathname + requestPathInfo)) {
            fileWriter.write(stringData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFile(String requestPathInfo) {
        File file = new File(pathname + requestPathInfo);
        return file.delete();
    }
}
