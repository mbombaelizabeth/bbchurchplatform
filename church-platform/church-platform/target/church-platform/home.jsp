<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.church.model.User" %>
<%
    User user = (session == null) ? null : (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Home - Church Platform</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f4f4; margin:0; }
        header { background:#2c3e50; color:#fff; padding:16px 24px; display:flex; justify-content:space-between; align-items:center; }
        header a { color:#fff; text-decoration:none; margin-left:16px; }
        .container { max-width:600px; margin:30px auto; background:#fff; padding:24px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,.1); }
        #notifications { list-style:none; padding:0; }
        #notifications li { background:#ecf0f1; padding:10px 14px; border-radius:4px; margin-bottom:8px; }
        .status { font-size:13px; color:#888; margin-bottom:12px; }
        .error { color:#c0392b; margin-bottom:12px; }
    </style>
</head>
<body>
<header>
    <div>Welcome, <strong><%= user.getUsername() %></strong> (<%= user.getRole() %>)</div>
    <div>
        <% if (User.ROLE_LEADER.equals(user.getRole())) { %>
            <a href="notification.jsp">Manage Notifications</a>
        <% } %>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
</header>

<div class="container">
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %><div class="error"><%= error %></div><% } %>

    <h2>Live Notifications</h2>
    <div class="status" id="status">Connecting...</div>
    <ul id="notifications"></ul>
</div>

<script>
    // Question 9: connect to the WebSocket endpoint (Question 3) so any
    // notification broadcast by the server (via Question 5's JMS ->
    // WebSocket bridge) appears here instantly, with no page refresh.
    var protocol = (window.location.protocol === "https:") ? "wss://" : "ws://";
    var wsUrl = protocol + window.location.host + "<%= request.getContextPath() %>/notifications";
    var socket = new WebSocket(wsUrl);
    var statusEl = document.getElementById("status");
    var list = document.getElementById("notifications");

    socket.onopen = function () {
        statusEl.textContent = "Connected - listening for notifications...";
    };

    socket.onmessage = function (event) {
        var li = document.createElement("li");
        li.textContent = event.data;
        list.prepend(li);
    };

    socket.onclose = function () {
        statusEl.textContent = "Disconnected.";
    };

    socket.onerror = function () {
        statusEl.textContent = "Connection error.";
    };
</script>
</body>
</html>
