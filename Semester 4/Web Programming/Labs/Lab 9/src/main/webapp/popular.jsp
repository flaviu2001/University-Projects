<%@ page import="java.util.List" %>
<%@ page import="domain.Pair" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bookmarks</title>
    <link rel="stylesheet" href="popular.css">
    <script src="jquery-3.6.0.min.js"></script>
    <script src="main.js"></script>
</head>
<body>
<div class="wrapper">
    <table>
        <tr>
            <th>
                URL
            </th>
            <th>
                COUNTS
            </th>
        </tr>
        <%
            //noinspection unchecked
            List<Pair<String, Integer>> urls = (List<Pair<String, Integer>>) request.getSession().getAttribute("urls");
            for (Pair<String, Integer> pair : urls) {
                out.println("<tr>");
                out.println("<td class=\"first_column\">");
                out.println("<a href = " + pair.first + ">");
                out.println(pair.first);
                out.println("</a>");
                out.println("</td>");
                out.println("<td class=\"second_column\">");
                out.println(pair.second);
                out.println("</td>");
                out.println("</tr>");
            }
        %>
    </table>
</div>
</body>
</html>
