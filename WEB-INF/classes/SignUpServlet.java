import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/userinfo?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "myuser", "xxxx");
             Statement stmt = conn.createStatement()
        ) {
            String sql = "INSERT INTO userpass (username, password, firstname, lastname, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, phone);
            pstmt.executeUpdate();

            out.println("<html><head>");
            out.println("<title>Registration Successful</title>");
            out.println("<style>");
            out.println("body{");
            out.println("background-image: url('profilesuccessful.png');");
            out.println("background-size: cover; /* Cover the entire background */");
            out.println("}");
            out.println("h1, p{");
            out.println("    font-size: 24px; /* Set font size for headings and paragraphs */");
            out.println("}");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h1>Registration Successful</h1>");
            out.println("<p>Your account has been created successfully.</p>");
            out.println("<a href='userlogin.html'>Login</a>");
            out.println("</body></html>");
            
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
