<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.church.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Church Platform</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f4f4; display:flex; justify-content:center; margin-top:60px; }
        .card { background:#fff; padding:30px 40px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,.1); width:320px; }
        h2 { text-align:center; color:#2c3e50; }
        label { display:block; margin-top:12px; font-size:14px; color:#555; }
        input[type=text], input[type=password], select { width:100%; padding:8px; margin-top:4px; box-sizing:border-box; border:1px solid #ccc; border-radius:4px; }
        button { width:100%; margin-top:20px; padding:10px; background:#27ae60; color:#fff; border:none; border-radius:4px; cursor:pointer; }
        button:hover { background:#1e8449; }
        .error { color:#c0392b; text-align:center; margin-top:10px; }
        .link { text-align:center; margin-top:16px; font-size:14px; }
    </style>
</head>
<body>
<div class="card">
    <h2>Create an Account</h2>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <label for="role">Role</label>
        <select id="role" name="role" required>
            <option value="<%= User.ROLE_MEMBER %>">Member</option>
            <option value="<%= User.ROLE_LEADER %>">Church Leader</option>
        </select>

        <button type="submit">Register</button>
    </form>

    <div class="link">
        Already have an account? <a href="login.jsp">Log in</a>
    </div>
</div>
</body>
</html>
