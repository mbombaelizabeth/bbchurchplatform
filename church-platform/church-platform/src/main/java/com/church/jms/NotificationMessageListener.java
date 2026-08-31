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
