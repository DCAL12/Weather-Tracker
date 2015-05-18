package controllers.setup;

import util.HTMLPageBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ViewSetup", urlPatterns = "/setup")
public class ViewSetup extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HTMLPageBuilder html = new HTMLPageBuilder(getServletContext(), "/views/setup.html");
        html.setContent("{{title}}", "Setup");

        response.setContentType("text/html");
        response.getWriter().println(html);
        response.getWriter().close();
    }
}
