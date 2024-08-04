import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Game Query Response</title>");
        out.println("<style>");
        out.println("body {");
        out.println("background-image: url('gamequerybackground.png');");
        out.println("background-size: cover; /* Cover the entire background */");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gamingeshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "myuser", "xxxx");

             Statement stmt = conn.createStatement()) {

            String[] selectedGames = request.getParameterValues("id");
            if (selectedGames == null || selectedGames.length == 0) {
                out.println("<h2>No games selected. Please go back to select game(s)</h2></body></html>");
                return;
            }

            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM gamesindex WHERE id IN (");
            for (int i = 0; i < selectedGames.length; i++) {
                sqlQuery.append("'").append(selectedGames[i]).append("'");
                if (i < selectedGames.length - 1) {
                    sqlQuery.append(", ");
                }
            }
            sqlQuery.append(")");

            out.println("<h3>Thank you for your query.</h3>");
            ResultSet resultSet = stmt.executeQuery(sqlQuery.toString());

            int count = 0;
            out.println("<form id='confirmForm' action='confirm' method='post'>");
            while (resultSet.next()) {
                out.println("<p><input type='checkbox' name='id' value='" + resultSet.getString("id") + "' />" +
                        resultSet.getString("title") + " by " + resultSet.getString("developer") +
                        ", $" + resultSet.getDouble("price") +
                        ", Quantity remaining: " + resultSet.getInt("qty") + "</p>");
                count++;
            }
            out.println("<p>==== " + count + " records found =====</p>");

            out.println("<p>Enter your Name: <input type='text' name='cust_name' required /></p>");
            out.println("<p>Enter your Email: <input type='email' name='cust_email' required /></p>");
            out.println("<p>Enter your Phone Number: <input type='text' name='cust_phone' required /></p>");
            out.println("<input type='submit' value='Confirm Order'>");
            out.println("</form>");

        } catch (SQLException ex) {
            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();
        }

        out.println("</body></html>");
        out.close();
    }
}
