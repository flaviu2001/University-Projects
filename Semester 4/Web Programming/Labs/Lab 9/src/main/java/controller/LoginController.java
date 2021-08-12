package controller;

import domain.DBManager;
import domain.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginController extends HttpServlet {
    public LoginController() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username.equals("")) {
            request.getSession().setAttribute("error", "Username must not be null!");
            request.getRequestDispatcher("login.jsp").include(request, response);
        } else if (password.isEmpty()) {
            request.getSession().setAttribute("error", "Password must not be null!");
            request.getRequestDispatcher("login.jsp").include(request, response);
        } else if (password.length() < 6) {
            request.getSession().setAttribute("error", "Password is not strong enough! It must have more than 6 characters!");
            request.getRequestDispatcher("login.jsp").include(request, response);
        } else {
            DBManager dbManager = new DBManager();
            User user = dbManager.authenticate(username, password);
            if (user != null) {
                response.addCookie(new Cookie("user", user.getUsername()));
                response.sendRedirect("main");
            } else {
                request.getSession().setAttribute("error", "Username or password invalid!");
                request.getRequestDispatcher("login.jsp").include(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        request.getRequestDispatcher("login.jsp").include(request, response);
    }
}
