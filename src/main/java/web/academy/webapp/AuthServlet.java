package web.academy.webapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 1. Make POST request with Body in JSON format to log in the app: http://localhost:8080/login { "login" : "admin", "password" : "0000" }
 * 2. Make GET request to see files in Database: http://localhost:8080/tables
 * 3. Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database
 * 4. Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database
 * 5. Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database
 * 6. Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: { "content" : "Alex, John" }
 */

@WebServlet(name = "authServlet", value = "/login")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");

        final JsonObject jsonData = new Gson().fromJson(request.getReader(), JsonObject.class);
        final HttpSession session = request.getSession();
        session.setAttribute("login", jsonData.get("login").getAsString());
        session.setAttribute("password", jsonData.get("password").getAsString());

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.print("<h2>You are successfully logged in...<h2>");
        out.println("<h3>Make GET request to see files in Database:</h3>");
        out.println("<h3>http://localhost:8080/tables</h3>");
        out.println("</body></html>");
    }
}

