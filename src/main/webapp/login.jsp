
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>ShopSphere | Login</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-container {
            background: white;
            width: 90%;
            max-width: 400px;
            padding: 32px;
            border-radius: 12px;
            box-shadow: 0 4px 18px #00000015;
        }

        h1 {
            text-align: center;
            margin-top: 0;
            color: #222;
        }

        .subtitle {
            text-align: center;
            color: #666;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin: 15px 0 7px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }

        input:focus {
            outline: 2px solid #777;
            border-color: transparent;
        }

        button {
            width: 100%;
            padding: 12px;
            margin-top: 22px;
            background: #222;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background: #444;
        }

        .error {
            padding: 10px;
            background: #ffe0e0;
            color: #9b2226;
            border-radius: 6px;
            margin-bottom: 15px;
        }

        .back-link {
            text-align: center;
            margin-top: 20px;
        }

        a {
            color: #222;
        }
    </style>
</head>

<body>

<div class="login-container">

    <h1>ShopSphere</h1>
    <p class="subtitle">Login to your account</p>

    <%
        String error = (String) request.getAttribute("error");

        if (error != null) {
    %>
        <div class="error">
            <%= error %>
        </div>
    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/login"
          method="post">

        <label for="email">Email</label>
        <input
            type="email"
            id="email"
            name="email"
            placeholder="Enter your email"
            autocomplete="email"
            required>

        <label for="password">Password</label>
        <input
            type="password"
            id="password"
            name="password"
            placeholder="Enter your password"
            autocomplete="current-password"
            required>

        <button type="submit">Login</button>

    </form>

    <div class="back-link">
        <a href="<%= request.getContextPath() %>/products">
            Back to Products
        </a>
    </div>

</div>

</body>
</html>
