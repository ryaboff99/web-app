package web.academy.webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.academy.webapp.FileRepository;

import java.io.*;
import java.util.Objects;

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
    private FileRepository fileRepository;

    @Override
    public void init() {
        fileRepository = new FileRepository(Objects.requireNonNull(
                TableServlet.class.getClassLoader().getResource("db")).getPath());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        if (fileRepository.fileExist(request.getPathInfo())) {
            resultPrinter(response, fileRepository.getFileContent(request.getPathInfo()));
        } else {
            System.err.println("ERROR 404 from TableServlet class, doGet method: There is no such file in directory!");
            errorSender(response, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        if (fileRepository.createFile(request.getPathInfo())) {
            resultPrinter(response, request.getPathInfo().replace("/","") + " file is created");
        } else {
            System.err.println("ERROR 409 from TableServlet class, doPost method: Such file is already exist!");
            errorSender(response, HttpServletResponse.SC_CONFLICT);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        final JsonObject jsonData;
        if (fileRepository.fileExist(request.getPathInfo())) {
            try {
                jsonData = new Gson().fromJson(request.getReader(), JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileRepository.updateFile(jsonData, request.getPathInfo());

            resultPrinter(response, request.getPathInfo().replace("/","") + " is updated");
        } else {
            try {
                jsonData = new Gson().fromJson(request.getReader(), JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileRepository.updateFile(jsonData, request.getPathInfo());

            resultPrinter(response, request.getPathInfo().replace("/","") + " is created");
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        if (fileRepository.deleteFile(request.getPathInfo())) {
            resultPrinter(response, request.getPathInfo().replace("/","") + " is deleted");
        } else {
            System.err.println("ERROR 404 from TableServlet class, doDelete method: There is no such file in directory!");
            errorSender(response, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void resultPrinter(HttpServletResponse response, String result) {
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

    private void errorSender(HttpServletResponse response, int error) {
        try {
            response.sendError(error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
