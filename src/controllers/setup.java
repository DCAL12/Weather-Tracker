package controllers;

import models.Sensor;
import models.TestSensor;
import services.HTMLPageBuilder;

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

    private static final String SENSOR_ROW_TEMPLATE =
            "<tr>" +    "<td>%s</td>" + "<td>%s</td>" + "<td>%s</td>" + "</tr>";
    // SENSOR ROW DATA:     enabled         ID           Sensor Type

    private List<Sensor> sensors = new ArrayList<>();
    private final int sampleRateInSeconds = 1;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        HTMLPageBuilder html = new HTMLPageBuilder(getServletContext(), "/views/setup.html");
        html.setContent("{{title}}", "Setup");

        // Available Sensors
        sensors.add(new TestSensor(TestSensor.WeatherParameter.TEMPERATURE));
        sensors.add(new TestSensor(TestSensor.WeatherParameter.HUMIDITY));
        sensors.add(new TestSensor(TestSensor.WeatherParameter.LIGHT));

        StringBuilder tableRows = new StringBuilder();

        sensors.forEach(sensor -> {
            String[] rowData = sensor.toString().split(" ");
            tableRows.append(String.format(SENSOR_ROW_TEMPLATE, rowData[0],rowData[1], rowData[2]));
        });

        html.setContent("{{sensors}}", tableRows.toString());

        response.getWriter().println(html);
        response.getWriter().close();
    }
}
