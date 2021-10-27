package controller;

import domain.DBManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddController extends HttpServlet {
    public AddController() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        new DBManager().addUrl(Integer.parseInt(req.getParameter("userId")), req.getParameter("url"));
    }
}
