
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopsphere.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopSphere | Admin Products</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            color: #222;
        }

        header {
            background: #222;
            color: white;
            padding: 20px 5%;
        }

        header h1 {
            margin: 0;
        }

        .container {
            max-width: 1200px;
            margin: auto;
            padding: 30px 20px;
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
        }

        .panel {
            background: white;
            padding: 22px;
            border-radius: 10px;
            box-shadow: 0 2px 10px #00000012;
            margin-bottom: 25px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 15px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 6px;
        }

        input, textarea, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
        }

        textarea {
            resize: vertical;
            min-height: 80px;
        }

        button {
            background: #222;
            color: white;
            border: none;
            padding: 11px 16px;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background: #444;
        }

        .submit-button {
            margin-top: 18px;
        }

        .table-wrapper {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
            vertical-align: top;
        }

        th {
            background: #222;
            color: white;
        }

        .edit-form {
            min-width: 220px;
        }

        .edit-form input,
        .edit-form textarea,
        .edit-form select {
            margin-bottom: 8px;
        }

        .delete-button {
            background: #b42318;
        }

        .delete-button:hover {
            background: #8f1d14;
        }

        .status-active {
            color: #087443;
            font-weight: bold;
        }

        .status-inactive {
            color: #b42318;
            font-weight: bold;
        }

        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #222;
        }

        footer {
            text-align: center;
            padding: 20px;
            color: #666;
        }

        @media (max-width: 600px) {
            .container {
                padding: 20px 12px;
            }

            .panel {
                padding: 15px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>ShopSphere | Admin Panel</h1>
</header>

<div class="container">

    <a class="back-link"
       href="<%= request.getContextPath() %>/products">
        Back to Customer Products
    </a>

    <h2>Manage Products</h2>

    <div class="panel">
        <h3>Add New Product</h3>

        <form action="<%= request.getContextPath() %>/admin/products"
              method="post">

            <input type="hidden" name="action" value="add">

            <div class="form-grid">
                <div>
                    <label>Category ID</label>
                    <input type="number" name="categoryId" min="1" required>
                </div>

                <div>
                    <label>Product Name</label>
                    <input type="text" name="name" maxlength="150" required>
                </div>

                <div>
                    <label>Brand</label>
                    <input type="text" name="brand" maxlength="100">
                </div>

                <div>
                    <label>Price (₹)</label>
                    <input type="number" name="price"
                           min="0" step="0.01" required>
                </div>

                <div>
                    <label>Discount (%)</label>
                    <input type="number" name="discount"
                           min="0" max="100" step="0.01" value="0" required>
                </div>

                <div>
                    <label>Stock</label>
                    <input type="number" name="stock" min="0" value="0" required>
                </div>

                <div>
                    <label>Image URL</label>
                    <input type="url" name="imageUrl" maxlength="500">
                </div>

                <div>
                    <label>Status</label>
                    <select name="status" required>
                        <option value="1">Active</option>
                        <option value="0">Inactive</option>
                    </select>
                </div>

                <div>
                    <label>Description</label>
                    <textarea name="description"></textarea>
                </div>
            </div>

            <button class="submit-button" type="submit">
                Add Product
            </button>
        </form>
    </div>

    <div class="panel">
        <h3>Existing Products</h3>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Current Details</th>
                        <th>Update Product</th>
                        <th>Delete</th>
                    </tr>
                </thead>

                <tbody>
                <%
                    List<Product> products =
                        (List<Product>) request.getAttribute("products");

                    if (products != null && !products.isEmpty()) {
                        for (Product p : products) {
                %>
                    <tr>
                        <td><%= p.getProductId() %></td>

                        <td>
                            <strong><%= p.getName() %></strong><br>
                            Brand: <%= p.getBrand() == null ? "" : p.getBrand() %><br>
                            Price: ₹<%= p.getPrice() %><br>
                            Discount: <%= p.getDiscount() %>%<br>
                            Stock: <%= p.getStock() %><br>
                            Status:
                            <span class="<%= "1".equals(p.getStatus())
                                ? "status-active" : "status-inactive" %>">
                                <%= "1".equals(p.getStatus())
                                    ? "Active" : "Inactive" %>
                            </span>
                        </td>

                        <td>
                            <form class="edit-form"
                                  action="<%= request.getContextPath() %>/admin/products"
                                  method="post">

                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="productId"
                                       value="<%= p.getProductId() %>">

                                <label>Category ID</label>
                                <input type="number" name="categoryId" min="1"
                                       value="<%= p.getCategoryId() %>" required>

                                <label>Name</label>
                                <input type="text" name="name" maxlength="150"
                                       value="<%= p.getName() %>" required>

                                <label>Brand</label>
                                <input type="text" name="brand" maxlength="100"
                                       value="<%= p.getBrand() == null ? "" : p.getBrand() %>">

                                <label>Description</label>
                                <textarea name="description"><%= p.getDescription() == null
                                    ? "" : p.getDescription() %></textarea>

                                <label>Price (₹)</label>
                                <input type="number" name="price" min="0"
                                       step="0.01" value="<%= p.getPrice() %>" required>

                                <label>Discount (%)</label>
                                <input type="number" name="discount" min="0"
                                       max="100" step="0.01"
                                       value="<%= p.getDiscount() %>" required>

                                <label>Stock</label>
                                <input type="number" name="stock" min="0"
                                       value="<%= p.getStock() %>" required>

                                <label>Image URL</label>
                                <input type="url" name="imageUrl" maxlength="500"
                                       value="<%= p.getImageUrl() == null
                                           ? "" : p.getImageUrl() %>">

                                <label>Status</label>
                                <select name="status" required>
                                    <option value="1"
                                        <%= "1".equals(p.getStatus()) ? "selected" : "" %>>
                                        Active
                                    </option>
                                    <option value="0"
                                        <%= "0".equals(p.getStatus()) ? "selected" : "" %>>
                                        Inactive
                                    </option>
                                </select>

                                <button type="submit">Update Product</button>
                            </form>
                        </td>

                        <td>
                            <form action="<%= request.getContextPath() %>/admin/products"
                                  method="post"
                                  onsubmit="return confirm('Delete this product?');">

                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="productId"
                                       value="<%= p.getProductId() %>">

                                <button class="delete-button" type="submit">
                                    Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="4">No products found.</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>

</div>

<footer>
    ShopSphere - Admin Product Management
</footer>

</body>
</html>
