package com.church.jms;

import com.church.websocket.NotificationEndpoint;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Question 5: Integration with WebSockets.
 *
 * This Message-Driven Bean is the consumer for the NotificationQueue
 * defined in JMSConfig (Question 4). The application server invokes
 * onMessage() automatically whenever a message is published to the
 * queue - for example when SendNotificationServlet publishes a message
 * on behalf of a Church Leader. As soon as a message is received, it is
 * forwarded to every connected WebSocket client via
 * NotificationEndpoint.broadcastMessage(message).
 *
 * This combination gives the platform:
 *   - Reliable persistence: JMS holds the message until a consumer is
 *     available/acknowledges it, so nothing is lost if the server is
 *     briefly busy.
 *   - Real-time delivery: as soon as the MDB consumes the message it is
 *     pushed instantly to every browser connected to /notifications.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/NotificationQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class NotificationMessageListener implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(NotificationMessageListener.class.getName());

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();
                LOGGER.info("Received JMS notification, broadcasting to WebSocket clients: " + text);

                // Bridge: hand the message straight to the WebSocket endpoint
                NotificationEndpoint.broadcastMessage(text);
            } else {
                LOGGER.warning("Ignoring non-text JMS message: " + message.getClass().getName());
            }
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Failed to process JMS notification message", e);
        }
    }
}
