package web.academy.webapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * 1. Make POST request with Body in JSON format to log in the app: http://localhost:8080/login { "login" : "admin", "password" : "0000" }
 * 2. Make GET request to see files in Database: http://localhost:8080/tables
 * 3. Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database
 * 4. Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database
 * 5. Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database
 * 6. Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: { "content" : "Alex, John" }
 */

@WebServlet(name = "tableServlet", value = "/table/*")
public class TableServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        String result = ""; // make as instance variable?
        final File file = new File(Objects.requireNonNull(TableServlet.class.getClassLoader()
                .getResource("db")).getPath() + request.getPathInfo());
        StringJoiner joiner = new StringJoiner(", ");

        if (file.exists()) {
            try (Scanner output = new Scanner(file)) {
                while (output.hasNextLine()) {
                    joiner.add(output.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            result = "Data in " + file.getName() + ": " + joiner;
        } else {
            try {
                System.err.println("ERROR 404 from TableServlet class, doGet method: There is no such file in directory!");
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // test it
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>" + result + "</h2>\n");
            out.println("<h3>Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database </h3>");
            out.println("<h3>Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database </h3>");
            out.println("<h3>Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database </h3>");
            out.println("<h3>Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: </h3>");
            out.println("<h3>{\n" +
                    "  \"content\" : \"Alex, John\"\n" +
                    "} </h3>");
            out.println("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        String result = "";
        final File file = new File(Objects.requireNonNull(TableServlet.class.getClassLoader()
                .getResource("db")).getPath() + request.getPathInfo());
        if (file.exists()) {
            try {
                System.err.println("ERROR 409 from TableServlet class, doPost method: Such file is already exist!");
                response.sendError(HttpServletResponse.SC_CONFLICT); // test it
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            result = file.getName() + " file is created";
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>" + result + "</h2>");
            out.println("<h3>Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database </h3>");
            out.println("<h3>Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database </h3>");
            out.println("<h3>Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database </h3>");
            out.println("<h3>Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: </h3>");
            out.println("<h3>{" +
                    "  \"content\" : \"Alex, John\"" +
                    "} </h3>");
            out.println("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        final JsonObject jsonData;
        try {
            jsonData = new Gson().fromJson(request.getReader(), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String stringData = jsonData.get("content").getAsString();
        String result;
        final File file = new File(Objects.requireNonNull(TableServlet.class.getClassLoader()
                .getResource("db")).getPath() + request.getPathInfo());
        if (file.exists()) {
            result = file.getName() + " is updated";
        } else {
            result = file.getName() + " is created";
        }

        try (FileWriter fileWriter = new FileWriter(Objects.requireNonNull(TableServlet.class.getClassLoader()
                .getResource("db")).getPath() + request.getPathInfo())) {
            fileWriter.write(stringData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>" + result + "</h2>");
            out.println("<h3>Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database </h3>");
            out.println("<h3>Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database </h3>");
            out.println("<h3>Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database </h3>");
            out.println("<h3>Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: </h3>");
            out.println("<h3>{" +
                    "  \"content\" : \"Alex, John\"" +
                    "} </h3>");
            out.println("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        String result = "";
        final File file = new File(Objects.requireNonNull(TableServlet.class.getClassLoader()
                .getResource("db")).getPath() + request.getPathInfo());
        if (file.exists()) {
            result = file.getName() + " is deleted";
            file.delete();
        } else {
            try {
                System.err.println("ERROR 404 from TableServlet class, doDelete method: There is no such file in directory!");
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // test it
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>" + result + "</h2>");
            out.println("<h3>Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database </h3>");
            out.println("<h3>Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database </h3>");
            out.println("<h3>Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database </h3>");
            out.println("<h3>Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: </h3>");
            out.println("<h3>{" +
                    "  \"content\" : \"Alex, John\"" +
                    "} </h3>");
            out.println("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
