import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/userinfo?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "myuser", "xxxx");
             Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT * FROM userpass WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                // Authentication successful
                String firstName = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                int phone = resultSet.getInt("phone");

                // Generate HTML response to display user info
               // Generate HTML response to display user info
            out.println("<html><head>");
            out.println("<title>User Information</title>");
            out.println("<style>");
            out.println("body{");
            out.println("background-image: url('profilebackground.png');");
            out.println("background-size: cover; /* Cover the entire background */");
            out.println("}");
            out.println("h1, p{");
            out.println("    font-size: 18px; /* Set font size for headings and paragraphs */");
            out.println("}");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h1>User Information</h1>");
            out.println("<p>Username: " + username + "</p>");
            out.println("<p>Password: " + password + "</p>");
            out.println("<p>First Name: " + firstName + "</p>");
            out.println("<p>Last Name: " + lastname + "</p>");
            out.println("<p>Email: " + email + "</p>");
            out.println("<p>Phone: " + phone + "</p>");
            out.println("<a href='homequerymv.html'>Return to buy page</a>");
            out.println("</body></html>");

            } else {
                // Authentication failed
                out.println("<html><head>");
                out.println("<title>Authentication Failed</title>");
                out.println("<style>");
                out.println("body{");
                out.println("    background-image: url('loginfailbackground.png');");
                out.println("    background-size: cover; /* Cover the entire background */");
                out.println("}");
                out.println("h3{");
                out.println("    font-size: 24px; /* Set font size for the error message */");
                out.println("    color: red; /* Set font color to red */");
                out.println("}");
                out.println("a{");
                out.println("    font-size: 18px; /* Set font size for the link */");
                out.println("}");
                out.println("</style>");
                out.println("</head><body>");
                out.println("<h3>Authentication failed. Invalid username or password.</h3>");
                out.println("<a href='userlogin.html'>Try again</a>");
                out.println("</body></html>");

            }
        } catch (SQLException ex) {
            out.println("<html><body>");
            out.println("<h3>Error: " + ex.getMessage() + "</h3>");
            out.println("<p>Check Tomcat console for details.</p>");
            out.println("</body></html>");
            ex.printStackTrace();
        }
        out.close();
    }
}
