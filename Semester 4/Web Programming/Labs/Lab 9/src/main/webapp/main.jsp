<%@ page import="domain.Url" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bookmarks</title>
    <link rel="stylesheet" href="main.css">
    <script src="jquery-3.6.0.min.js"></script>
    <script src="main.js"></script>
</head>
<body>
<div class="wrapper">
    <div class="topWrapper">
        <%
            out.println("<h3>Hi, " + ((User) request.getSession().getAttribute("user")).getUsername() + "!</h3>");
        %>
        <button onclick="logout()">Logout</button>
    </div>
    <label>
        <input type="number" id="cntText" placeholder="Count:">
        <%
            out.println("<button onclick=\"nav()\">URLs</button>");
        %>
    </label>
    <label>
        <input type="text" id="urlText" placeholder="New url:">
        <%
            out.println("<button onclick=\"add(" + ((User) request.getSession().getAttribute("user")).getId() + ")\">Add</button>");
        %>
    </label>
    <p id="errorMsg">
        Invalid url! Try another one
    </p>
    <ul>
        <%
            //noinspection unchecked
            List<Url> urls = (List<Url>) request.getSession().getAttribute("urls");
            for (Url url : urls)
                out.println("<li> <a href=" + url.getUrl() + ">" + url.getUrl() + "</a> <button onclick=\"del(" + url.getId() + ")\"> Delete </button> </li>");
        %>
    </ul>
</div>
</body>
</html>
