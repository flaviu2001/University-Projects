package controller;

import domain.DBManager;
import domain.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PopularController extends HttpServlet {
    public PopularController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        int howMany = 10;
        for (Cookie c : request.getCookies())
            if (c.getName().equals("howMany"))
                howMany = Integer.parseInt(c.getValue());
        List<Pair<String, Integer>> urls = new DBManager().getPopularUrls(howMany);
        request.getSession().setAttribute("urls", urls);
        request.getRequestDispatcher("popular.jsp").include(request, response);
    }
}
