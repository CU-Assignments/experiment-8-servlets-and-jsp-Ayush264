<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Attendance</title>
</head>
<body>
    <h2>Enter Attendance</h2>
    <form action="AttendanceServlet" method="post">
        Student ID: <input type="text" name="studentId" required><br>
        Name: <input type="text" name="name" required><br>
        Date: <input type="date" name="date" required><br>
        Status: <select name="status">
                    <option value="Present">Present</option>
                    <option value="Absent">Absent</option>
                </select><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String name = request.getParameter("name");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO Attendance (studentId, name, date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);
            stmt.setString(2, name);
            stmt.setString(3, date);
            stmt.setString(4, status);
            stmt.executeUpdate();
            response.sendRedirect("student.jsp?msg=Attendance Recorded");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error saving attendance.</p>");
        }
    }
}


