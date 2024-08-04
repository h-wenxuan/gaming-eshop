import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/confirm")
public class ConfirmServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Order Confirmation</title>");
        out.println("<style>");
        out.println("body {");
        out.println("background-image: url('confirmbackground.png');");
        out.println("background-size: cover; /* Cover the entire background */");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gamingeshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "myuser", "xxxx");

             Statement stmt = conn.createStatement()) {

            String[] ids = request.getParameterValues("id");
            if (ids != null) {
                String custName = request.getParameter("cust_name");
                String custEmail = request.getParameter("cust_email");
                String custPhone = request.getParameter("cust_phone");

                for (String id : ids) {
                    String sqlUpdate = "UPDATE gamesindex SET qty = qty - 1 WHERE id = " + id;
                    int rowsUpdated = stmt.executeUpdate(sqlUpdate);
                    out.println("<p>" + rowsUpdated + " record(s) updated.</p>");

                    String sqlInsert = "INSERT INTO order_records (id, qty_ordered, cust_name, cust_email, cust_phone) VALUES (" +
                            id + ", 1, '" + custName + "', '" + custEmail + "', '" + custPhone + "')";
                    int rowsInserted = stmt.executeUpdate(sqlInsert);
                    out.println("<p>" + rowsInserted + " record(s) inserted.</p>");
                    out.println("<h3>Your order for game ID=" + id + " has been confirmed.</h3>");
                }
                out.println("<h3>Thank you for your purchase!</h3>");
                out.println("<a href='homequerymv.html'>Go back to main menu</a>");
            } else {
                out.println("<h3>No game selected. Please go back and select a game.</h3>");
            }

        } catch (SQLException ex) {
            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();
        }

        out.println("</body></html>");
        out.close();
    }
}
