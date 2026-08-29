package com.church.jms;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSDestinationDefinition;

/**
 * Question 4: JMS Configuration.
 *
 * Declares the two resources the platform needs for reliable message
 * persistence:
 *
 *   1. A JMS Connection Factory - used to open connections to the
 *      messaging provider (the application server's built-in broker,
 *      e.g. OpenMQ on GlassFish/Payara, or ActiveMQ/Artemis on WildFly).
 *      JNDI name: java:app/jms/NotificationConnectionFactory
 *
 *   2. A JMS Destination (a Queue) - where notification messages are
 *      stored until a consumer (NotificationMessageListener, Question 5)
 *      picks them up. Using a Queue means the JMS provider persists each
 *      message until it has been reliably delivered/acknowledged, even
 *      if no one is online at the moment it is sent.
 *      JNDI name: java:app/jms/NotificationQueue
 *      Physical name: NotificationQueue
 *
 * These are declared using the standard Java EE 8 resource-definition
 * annotations, so any compliant application server creates them
 * automatically at deploy time - no manual admin-console configuration
 * is required. (An equivalent, purely declarative glassfish-resources.xml
 * is included under docs/ as a reference if you would rather configure
 * these resources by hand instead of via annotations - the two should
 * not be used at the same time.)
 *
 * A Queue (point-to-point) rather than a Topic (publish/subscribe) is
 * used because there is exactly one consumer in this design -
 * NotificationMessageListener - which itself fans each message out to
 * every connected WebSocket client. Swapping @JMSDestinationDefinition's
 * interfaceName to "jakarta.jms.Topic" is all that would be required to
 * move to a publish/subscribe model instead.
 */
@JMSConnectionFactoryDefinition(
        name = "java:app/jms/NotificationConnectionFactory",
        interfaceName = "jakarta.jms.ConnectionFactory"
)
@JMSDestinationDefinition(
        name = "java:app/jms/NotificationQueue",
        interfaceName = "jakarta.jms.Queue",
        destinationName = "NotificationQueue"
)
@Singleton
@Startup
public class JMSConfig {
    // No business logic required here - this class exists purely to
    // carry the resource-definition annotations above so the container
    // processes them at application startup.
}
