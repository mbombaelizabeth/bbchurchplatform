package com.church.filter;

import com.church.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Question 10: Role-based access control for notification.jsp.
 *
 * Mapped directly onto "/notification.jsp" via @WebFilter, so it runs
 * in front of every request for that page - including direct URL entry,
 * not just clicks from within the app.
 *
 * Rules:
 *   - No session / not logged in           -> redirect to login.jsp
 *   - Logged in, but role != "Church Leader" -> redirect to home.jsp
 *     with an explanatory error message
 *   - Logged in as a Church Leader          -> allowed through
 */
@WebFilter("/notification.jsp")
public class AccessControlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            // Not authenticated at all
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (!User.ROLE_LEADER.equals(user.getRole())) {
            // Authenticated, but not authorised for this page
            request.setAttribute("error",
                    "Access denied: notifications are only visible to Church Leaders.");
            request.getRequestDispatcher("/home.jsp").forward(request, response);
            return;
        }

        // Authorised - continue on to notification.jsp
        chain.doFilter(request, response);
    }
}
