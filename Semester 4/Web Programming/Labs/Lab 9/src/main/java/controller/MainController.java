package controller;

import domain.DBManager;
import domain.Url;
import domain.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MainController extends HttpServlet {
    public MainController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        for (Cookie c : request.getCookies())
            if (c.getName().equals("user")) {
                List<Url> urls = (new DBManager()).UrlsOfUser(c.getValue());
                User user = (new DBManager()).getUserFromName(c.getValue());
                request.getSession().setAttribute("urls", urls);
                request.getSession().setAttribute("user", user);
                request.getRequestDispatcher("main.jsp").include(request, response);
                return;
            }
        response.getWriter().println("Invalid request");
    }
}
