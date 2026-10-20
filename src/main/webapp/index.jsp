
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopSphere | Online Shopping</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f6fa;
            color: #222;
        }
        header {
            background: #172554;
            color: white;
            padding: 20px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }
        header h1 { margin: 0; font-size: 26px; }
        nav { display: flex; gap: 18px; flex-wrap: wrap; }
        nav a {
            color: white;
            text-decoration: none;
        }
        .hero {
            padding: 75px 8%;
            background: linear-gradient(120deg, #dbeafe, #eff6ff);
        }
        .hero h2 { font-size: 42px; margin: 0 0 15px; }
        .hero p { font-size: 18px; line-height: 1.6; }
        .btn {
            display: inline-block;
            background: #1d4ed8;
            color: white;
            padding: 12px 22px;
            border-radius: 6px;
            text-decoration: none;
            margin: 8px 8px 0 0;
        }
        .features {
            padding: 35px 7%;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }
        .card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 10px #0000000d;
        }
        footer {
            text-align: center;
            background: #172554;
            color: white;
            padding: 18px;
        }
    </style>
</head>
<body>

<header>
    <h1>ShopSphere</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <a href="${pageContext.request.contextPath}/my-orders">My Orders</a>
        <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
        <a href="${pageContext.request.contextPath}/register.jsp">Register</a>
    </nav>
</header>

<section class="hero">
    <h2>Welcome to ShopSphere</h2>
    <p>Discover products, manage your shopping cart and enjoy a convenient online shopping experience.</p>
    <a class="btn" href="${pageContext.request.contextPath}/products">Explore Products</a>
    <a class="btn" href="${pageContext.request.contextPath}/login.jsp">Login</a>
</section>

<section class="features">
    <div class="card">
        <h3>Browse Products</h3>
        <p>Explore the products available in our online store.</p>
    </div>
    <div class="card">
        <h3>Shopping Cart</h3>
        <p>Add products and manage your cart before checkout.</p>
    </div>
    <div class="card">
        <h3>Secure Account</h3>
        <p>Register or log in to access account-related features.</p>
    </div>
    <div class="card">
        <h3>Order History</h3>
        <p>View your orders after signing in.</p>
    </div>
</section>

<footer>
    <p>&copy; 2026 ShopSphere. All rights reserved.</p>
</footer>

</body>
</html>
