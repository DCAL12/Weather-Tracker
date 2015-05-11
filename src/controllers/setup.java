package controllers;

import models.Sensor;
import services.HTMLPageBuilder;
import services.SensorDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/setup")
public class setup extends HttpServlet {

    private SensorDAO dao;
    private List<Sensor> sensors = new ArrayList<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HTMLPageBuilder html = new HTMLPageBuilder(getServletContext(), "/views/setup.html");
        html.setContent("{{title}}", "Setup");

        dao = new SensorDAO();
        sensors = dao.getAllSensors();

        StringBuilder tableRows = new StringBuilder();
        String sensorRowDetailTemplate = html.readTemplate("/views/sensorDetail.html");

        sensors.forEach(sensor -> {
            String tableRow = sensorRowDetailTemplate;
            tableRow = tableRow.replace("{{enabled}}", Boolean.toString(sensor.isEnabled()));
            tableRow = tableRow.replace("{{ID}}", Integer.toString(sensor.getId()));
            tableRow = tableRow.replace("{{label}}", sensor.getLabel());
            tableRows.append(tableRow);
        });

        html.setContent("{{sensors}}", tableRows.toString());

        response.setContentType("text/html");
        response.getWriter().println(html);
        response.getWriter().close();
    }
}
