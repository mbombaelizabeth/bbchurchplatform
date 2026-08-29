<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Church Platform</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f4f4; display:flex; justify-content:center; margin-top:80px; }
        .card { background:#fff; padding:30px 40px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,.1); width:320px; }
        h2 { text-align:center; color:#2c3e50; }
        label { display:block; margin-top:12px; font-size:14px; color:#555; }
        input[type=text], input[type=password] { width:100%; padding:8px; margin-top:4px; box-sizing:border-box; border:1px solid #ccc; border-radius:4px; }
        button { width:100%; margin-top:20px; padding:10px; background:#2c3e50; color:#fff; border:none; border-radius:4px; cursor:pointer; }
        button:hover { background:#1a242f; }
        .error { color:#c0392b; text-align:center; margin-top:10px; }
        .success { color:#27ae60; text-align:center; margin-top:10px; }
        .link { text-align:center; margin-top:16px; font-size:14px; }
    </style>
</head>
<body>
<div class="card">
    <h2>Church Platform Login</h2>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <% String success = (String) request.getAttribute("success"); %>
    <% if (success != null) { %>
        <div class="success"><%= success %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Log In</button>
    </form>

    <div class="link">
        Don't have an account? <a href="registration.jsp">Register here</a>
    </div>
</div>
</body>
</html>
