# Church Notification Platform

A Jakarta EE 11 web application (Servlets + WebSocket + JMS + JSP),
targeting GlassFish 8.0.4. Every
source file has a comment naming the question it answers.

> **Namespace note:** this code uses the `jakarta.*` package namespace
> (Jakarta Servlet, Jakarta WebSocket, Jakarta Messaging, Jakarta EJB),
> matching GlassFish 8 / Jakarta EE 11.


## How the pieces fit together

1. A visitor registers on **registration.jsp** -> `RegistrationServlet`
   validates the form and stores the user in the in-memory `UserStore`
   (a `ConcurrentHashMap<String, User>`), rejecting duplicate usernames.
2. They log in on **login.jsp** -> `LoginServlet` checks the credentials
   against `UserStore` and, on success, opens an `HttpSession` holding
   the `User` object, then redirects to **home.jsp**.
3. **home.jsp** opens a browser WebSocket connection to
   `NotificationEndpoint` (`/notifications`) and appends any message it
   receives to a live list - no page refresh needed.
4. If the logged-in user's role is `"Church Leader"`, they also see a
   link to **notification.jsp**. Anyone else who tries to open that URL
   is intercepted by `AccessControlFilter` and redirected before the
   page renders.
5. From **notification.jsp**, a Church Leader submits a message to
   `SendNotificationServlet`, which publishes it onto the
   `NotificationQueue` JMS destination declared in `JMSConfig`.
6. The `NotificationMessageListener` message-driven bean is triggered by
   the container as soon as a message lands on that queue, and calls
   `NotificationEndpoint.broadcastMessage(...)`, which pushes the text
   out to every browser currently connected over WebSocket.

## Running it

This app needs a **full Java EE / Jakarta EE application server** that
supports JMS and Message-Driven Beans - e.g. **GlassFish**, **Payara**,
or **WildFly**. A bare Servlet container (plain Apache Tomcat) is *not*
enough on its own, because it has no JMS provider or EJB container to
run the MDB in.

```bash
mvn clean package
# deploy target/church-platform.war to GlassFish/Payara/WildFly,
# then open http://localhost:8080/church-platform/
```

The JMS connection factory and queue are created automatically at
deploy time from the `@JMSConnectionFactoryDefinition` /
`@JMSDestinationDefinition` annotations in `JMSConfig.java` - no manual
admin-console setup is required. `docs/glassfish-resources.xml.reference`
is included only as a reference if you'd rather configure the same
resources declaratively by hand instead.

## Notes / assumptions

- Passwords are stored in plain text in memory for simplicity, as the
  brief asks for an in-memory `HashMap`-style store rather than a real
  database with hashing - flagged here so it's clear this would need to
  change (e.g. BCrypt + a real datastore) before any real deployment.
- Two roles are supported: `Member` and `Church Leader` (see
  `User.ROLE_MEMBER` / `User.ROLE_LEADER`), matching the "duplicate
  usernames and user roles" validation called out in Question 1.2 and
  the RBAC requirement in Question 10.
- `LogoutServlet` and `SendNotificationServlet` are small supporting
  pieces (not separately marked) that make the JSPs in Deliverable 2
  fully functional end to end.
