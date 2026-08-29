package com.church.servlet;

import com.church.model.User;
import com.church.util.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Question 2: Login Servlet.
 *
 * 2.1 Accepts username/password posted from login.jsp.
 * 2.2 Validates the input (non-empty fields) and checks the credentials
 *     against UserStore, the same in-memory store used at registration
 *     (this is also where a "does this user/role exist" check happens).
 * 2.3 On success, creates an HttpSession and stores the authenticated
 *     User object in it.
 * 2.4 Returns an appropriate success (redirect to home.jsp) or error
 *     (forward back to login.jsp with a message) response.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserStore userStore = UserStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));

        // 2.2 Validate the input data
        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userStore.getUser(username);

        // 2.2 Validate credentials - reject unknown username or wrong password
        if (user == null || !user.getPassword().equals(password)) {
            request.setAttribute("error", "Invalid username or password.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 2.3 On successful login, create an HTTP Session and store the authenticated user
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        // 2.4 Return an appropriate success response
        response.sendRedirect(request.getContextPath() + "/home.jsp");
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
