package web.academy.webapp.servlets;

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

        final File file = new File(fileRepository.getPathname() + request.getPathInfo());
        if (file.exists()) {
            resultPrinter(response, fileRepository.getFileContent(file));
        } else {
            System.err.println("ERROR 404 from TableServlet class, doGet method: There is no such file in directory!");
            errorSender(response, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        final File file = new File(fileRepository.getPathname() + request.getPathInfo());
        if (file.exists()) {
            System.err.println("ERROR 409 from TableServlet class, doPost method: Such file is already exist!");
            errorSender(response, HttpServletResponse.SC_CONFLICT);
        } else {
            resultPrinter(response, fileRepository.createFile(file));
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        resultPrinter(response, fileRepository.updateFile(request));
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);

        final File file = new File(fileRepository.getPathname() + request.getPathInfo());
        if (file.exists()) {
            resultPrinter(response, fileRepository.deleteFile(file));
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
