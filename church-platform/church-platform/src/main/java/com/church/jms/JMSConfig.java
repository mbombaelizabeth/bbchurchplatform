package com.church.jms;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSDestinationDefinition;


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
