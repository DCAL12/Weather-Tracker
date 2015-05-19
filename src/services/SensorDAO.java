package services;

import models.Sensor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO extends DAO {

    private static SensorDAO instance;
    private SensorDAO() {
        super();
    }
    public static SensorDAO getInstance() {
        if (instance == null) {
            instance = new SensorDAO();
        }
        return instance;
    }

    public List<Sensor> getSensors() {

        String sql = "SELECT * from Sensor";
        List<Sensor> results = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ){
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

        String sql = "SELECT * from Sensor WHERE Sensor.ID = ?";
        Sensor sensor = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
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
