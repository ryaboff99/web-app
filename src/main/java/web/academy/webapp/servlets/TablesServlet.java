package web.academy.webapp.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 1. Make POST request with Body in JSON format to log in the app: http://localhost:8080/login { "login" : "admin", "password" : "0000" }
 * 2. Make GET request to see files in Database: http://localhost:8080/tables
 * 3. Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database
 * 4. Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database
 * 5. Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database
 * 6. Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: { "content" : "Alex, John" }
 */

@WebServlet(name = "tablesServlet", value = "/tables")
public class TablesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");

        final StringJoiner joiner = new StringJoiner(", ");
        final File files = new File(Objects.requireNonNull(TableServlet.class.getClassLoader()
                .getResource("db")).getPath());
        for (File file : Objects.requireNonNull(files.listFiles())) {
            joiner.add(file.getName());
        }

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Files in resources/" + files.getName() + ":</h2>");
        out.println("<h2>" + joiner + "</h2>");
        out.println("<h3>Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database </h3>");
        out.println("<h3>Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database </h3>");
        out.println("<h3>Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database </h3>");
        out.println("<h3>Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: </h3>");
        out.println("<h3>{" +
                "  \"content\" : \"Alex, John\"" +
                "} </h3>");
        out.println("</body></html>");
    }
}
