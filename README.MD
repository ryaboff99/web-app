1. Make POST request with Body in JSON format to log in the app: http://localhost:8080/login { "login" : "admin", "password" : "0000" }
2. Make GET request to see files in Database: http://localhost:8080/tables
3. Make GET request to http://localhost:8080/table/{tableName} to see content of {tableName} file in Database
4. Make POST request to http://localhost:8080/table/{tableName} to create {tableName} file in Database
5. Make DELETE request to http://localhost:8080/table/{tableName} to delete {tableName} file from Database
6. Make PUT request to http://localhost:8080/table/{tableName} with Body in JSON format to update content in {tableName} file of Database: { "content" : "Alex, John" }
```
