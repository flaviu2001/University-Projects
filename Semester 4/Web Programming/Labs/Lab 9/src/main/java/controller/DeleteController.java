package controller;

import domain.DBManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteController extends HttpServlet {
    public DeleteController() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        new DBManager().deleteUrl(Integer.parseInt(request.getParameter("id")));
    }
}
