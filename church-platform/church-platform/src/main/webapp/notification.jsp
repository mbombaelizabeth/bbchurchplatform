<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.church.model.User" %>
<%
    
    User user = (session == null) ? null : (User) session.getAttribute("user");
    if (user == null || !User.ROLE_LEADER.equals(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/home.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Send Notification - Church Platform</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f4f4; margin:0; }
        header { background:#2c3e50; color:#fff; padding:16px 24px; display:flex; justify-content:space-between; align-items:center; }
        header a { color:#fff; text-decoration:none; margin-left:16px; }
        .container { max-width:600px; margin:30px auto; background:#fff; padding:24px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,.1); }
        textarea { width:100%; box-sizing:border-box; padding:10px; border-radius:4px; border:1px solid #ccc; }
        button { margin-top:12px; padding:10px 18px; background:#2c3e50; color:#fff; border:none; border-radius:4px; cursor:pointer; }
        .success { color:#27ae60; margin-top:10px; }
        .error { color:#c0392b; margin-top:10px; }
    </style>
</head>
<body>
<header>
    <div>Church Leader Console - <strong><%= user.getUsername() %></strong></div>
    <div>
        <a href="home.jsp">Home</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
</header>

<div class="container">
    <h2>Send a Notification</h2>
    <p> Messages sent here are broadcast in real time, to every
       member currently viewing the home page.</p>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %><div class="error"><%= error %></div><% } %>

    <% String success = (String) request.getAttribute("success"); %>
    <% if (success != null) { %><div class="success"><%= success %></div><% } %>

    <form action="<%= request.getContextPath() %>/sendNotification" method="post">
        <textarea name="message" rows="4" placeholder="Type your announcement..." required></textarea>
        <br>
        <button type="submit">Send Notification</button>
    </form>
</div>
</body>
</html>
