package web.academy.webapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringJoiner;

public class FileRepository {

    private FileRepository() {
        throw new IllegalStateException("Utility class");
    }

    public static String getFileContent(File file) {
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

    public static String createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file.getName() + " file is created";
    }

    public static String updateFile(HttpServletRequest request, String pathname) {
        final JsonObject jsonData;
        try {
            jsonData = new Gson().fromJson(request.getReader(), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String result;
        final File file = new File(pathname + request.getPathInfo());
        if (file.exists()) {
            result = file.getName() + " is updated";
        } else {
            result = file.getName() + " is created";
        }

        String stringData = jsonData.get("content").getAsString();
        try (FileWriter fileWriter = new FileWriter(file.getPath())) {
            fileWriter.write(stringData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String deleteFile(File file) {
        String result = file.getName() + " is deleted";
        file.delete();
        return result;
    }
}
