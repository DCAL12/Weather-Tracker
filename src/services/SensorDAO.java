package services;

import models.Sensor;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO extends DAO {

    public List<Sensor> getSensors() {

        List<Sensor> results = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from Sensor";
            ResultSet queryResults = statement.executeQuery(sql);

            while (queryResults.next()) {
                results.add(createSensor(queryResults));
            }

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.getSensors error: " + e.getMessage());
        }
        return results;
    }

    public Sensor getSensor(int sensorID) {

        Sensor sensor = null;

        try {
            String sql = "SELECT * from Sensor WHERE Sensor.ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResult = statement.executeQuery();

            if (queryResult.next()) {
                sensor = createSensor(queryResult);
            }

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.getSensor error: " + e.getMessage());
        }

        return sensor;
    }

    private Sensor createSensor(ResultSet queryResult) {

        Sensor sensor = null;

        try {
            sensor = new Sensor(
                    queryResult.getInt("id"),
                    queryResult.getString("label"),
                    queryResult.getString("port")
            );

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.createSensor error: " + e.getMessage());
        }
        return sensor;
    }
}
