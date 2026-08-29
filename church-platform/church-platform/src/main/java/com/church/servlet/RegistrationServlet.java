package com.church.servlet;

import com.church.model.User;
import com.church.util.UserStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Question 1: User Registration Servlet.
 *
 * 1.1 Accepts username/password (and role) posted from registration.jsp.
 * 1.2 Validates the input (non-empty fields, allowed role, duplicate username).
 * 1.3 Delegates storage to UserStore, an in-memory ConcurrentHashMap.
 * 1.4 Rejects registration if the username is already taken.
 * 1.5 Forwards back to the form with an error, or to login.jsp with a
 *     success message.
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private static final List<String> VALID_ROLES =
            Arrays.asList(User.ROLE_MEMBER, User.ROLE_LEADER);

    private final UserStore userStore = UserStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/registration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));
        String role = trim(request.getParameter("role"));

        // 1.2 Validate the input data: fields must not be empty
        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            forwardWithError(request, response, "All fields (username, password, role) are required.");
            return;
        }

        // 1.2 Validate the user role against the allowed set
        if (!VALID_ROLES.contains(role)) {
            forwardWithError(request, response,
                    "Invalid role. Allowed roles are: " + VALID_ROLES);
            return;
        }

        // 1.4 Prevent registration if the username already exists (duplicate check)
        if (userStore.userExists(username)) {
            forwardWithError(request, response,
                    "Username '" + username + "' is already taken. Please choose another.");
            return;
        }

        // 1.3 Store the user credentials in the in-memory data structure
        User user = new User(username, password, role);
        userStore.addUser(user);

        // 1.5 Return an appropriate success response
        request.setAttribute("success",
                "Registration successful for '" + username + "'. You can now log in.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                   String message) throws ServletException, IOException {
        // 1.5 Return an appropriate error response
        request.setAttribute("error", message);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.getRequestDispatcher("/registration.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
