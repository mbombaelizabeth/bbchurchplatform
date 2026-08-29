package com.church.servlet;

import com.church.model.User;

import jakarta.annotation.Resource;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.Queue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Supporting servlet that publishes a notification onto the JMS queue
 * declared in JMSConfig (Question 4). It is the "producer" side of the
 * pipeline described in Question 5: this servlet -> JMS Queue ->
 * NotificationMessageListener (MDB) -> NotificationEndpoint (WebSocket)
 * -> every browser listening on home.jsp / notification.jsp.
 *
 * Only reachable from notification.jsp, which is itself restricted to
 * Church Leaders by AccessControlFilter (Question 10). The role check
 * is repeated here server-side as a defensive measure.
 */
@WebServlet("/sendNotification")
public class SendNotificationServlet extends HttpServlet {

    @Resource(lookup = "java:app/jms/NotificationConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:app/jms/NotificationQueue")
    private Queue notificationQueue;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null || !User.ROLE_LEADER.equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Church Leaders may send notifications.");
            return;
        }

        String text = request.getParameter("message");
        if (text == null || text.trim().isEmpty()) {
            request.setAttribute("error", "Notification message cannot be empty.");
            request.getRequestDispatcher("/notification.jsp").forward(request, response);
            return;
        }

        String payload = user.getUsername() + ": " + text.trim();

        // Publish the message onto the JMS queue (Question 4/5)
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(notificationQueue, payload);
        } catch (JMSRuntimeException e) {
            throw new ServletException("Failed to publish notification to JMS", e);
        }

        request.setAttribute("success", "Notification sent.");
        request.getRequestDispatcher("/notification.jsp").forward(request, response);
    }
}
