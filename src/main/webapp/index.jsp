<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Welcome</title>
</head>
<body>
<h2><%= "Make POST request with any login and password in Body with JSON format to login in app: \n" +
        "http://localhost:8080/login \n" +
        "{\n" +
        "  \"login\" : \"admin\",\n" +
        "  \"password\" : \"0000\"\n" +
        "}" %>
</h2>
</body>
</html>