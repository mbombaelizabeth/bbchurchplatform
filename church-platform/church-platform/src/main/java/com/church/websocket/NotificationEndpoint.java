package com.church.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;


@ServerEndpoint("/notifications")
public class NotificationEndpoint {

    private static final Logger LOGGER = Logger.getLogger(NotificationEndpoint.class.getName());

    // Thread-safe set of every currently connected client session.
    private static final Set<Session> CLIENTS = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        CLIENTS.add(session);
        LOGGER.info("WebSocket opened: " + session.getId() + " (total connected: " + CLIENTS.size() + ")");
    }

    @OnClose
    public void onClose(Session session) {
        CLIENTS.remove(session);
        LOGGER.info("WebSocket closed: " + session.getId() + " (total connected: " + CLIENTS.size() + ")");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.log(Level.WARNING, "WebSocket error on session " + session.getId(), throwable);
        CLIENTS.remove(session);
    }

    /**
     * Broadcasts a text message to every currently connected WebSocket
     * client. Static so it can be invoked from the MDB
     * (NotificationMessageListener), which is a completely different
     * managed component and does not hold a reference to any particular
     * endpoint instance.
     */
    public static void broadcastMessage(String message) {
        for (Session client : CLIENTS) {
            try {
                if (client.isOpen()) {
                    client.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Failed to send message to session " + client.getId(), e);
            }
        }
    }
}
