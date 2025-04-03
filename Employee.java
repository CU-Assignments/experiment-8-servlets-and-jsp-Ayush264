import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String empId = request.getParameter("empId");
        try (Connection conn = DBConnection.getConnection()) {
            String query = (empId == null || empId.isEmpty()) ? 
                            "SELECT * FROM Employee" : 
                            "SELECT * FROM Employee WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            if (empId != null && !empId.isEmpty()) {
                stmt.setInt(1, Integer.parseInt(empId));
            }
            
            ResultSet rs = stmt.executeQuery();
            out.println("<h2>Employee List</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Department</th></tr>");
            
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("department") + "</td></tr>");
            }
            
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error retrieving employees.</p>");
        }
    }
}
