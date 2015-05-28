package controllers.triggers;

import util.HTMLPageBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ManageTriggers", urlPatterns = "/triggers")
public class ViewTriggers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HTMLPageBuilder html = new HTMLPageBuilder(getServletContext(), "/views/triggers.html");
        html.setContent("{{title}}", "Manage Triggers");

        response.setContentType("text/html");
        response.getWriter().println(html);
        response.getWriter().close();
    }
}
