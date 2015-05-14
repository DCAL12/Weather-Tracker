package services;

import models.Measurement;
import models.Sensor;
import models.Notification;
import models.Threshold;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO {

    private Connection connection;

    public SensorDAO() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather",
                    "user", "RLXREL4Z3VfWZV54");
        } catch (SQLException e) {
            System.out.println("services.SensorDAO SQL error: " + e.getMessage());
            connection = null;
        } catch (ClassNotFoundException c) {
            System.out.println("services.SensorDAO Driver error: " + c.getMessage());
            connection = null;
        }
    }

    public List<Sensor> getAllSensors() {

        List<Sensor> results = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from Sensor";
            ResultSet queryResults = statement.executeQuery(sql);

            while (queryResults.next()) {
                results.add(createSensor(queryResults));
            }

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.getAllSensors error: " + e.getMessage());
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
            sensor = createSensor(queryResult);

            sensor.setMeasurements(populateMeasurements(sensor.getId()));
            sensor.setNotifications(populateNotifications(sensor.getId()));

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.getSensor error: " + e.getMessage());
        }

        return sensor;
    }

    private Sensor createSensor(ResultSet queryResult) {

        Sensor sensor = null;

        try {
            queryResult.next();
            sensor = new Sensor(
                    queryResult.getInt("id"),
                    queryResult.getString("port"),
                    queryResult.getString("label"),
                    queryResult.getBoolean("enabled"),
                    queryResult.getInt("sampleRate")
            );
        } catch (SQLException e) {
            System.out.println("services.SensorDAO.createSensor error: " + e.getMessage());
        }
        return sensor;
    }

    public Sensor getSensorDetails(int sensorID) {

        Sensor sensor = getSensor(sensorID);
        sensor.setMeasurements(populateMeasurements(sensor.getId()));
        sensor.setNotifications(populateNotifications(sensor.getId()));

        return sensor;
    }

    private List<Notification> populateNotifications(int sensorID) {
        List<Notification> notifications = new ArrayList<>();

        try {
            String sql = "SELECT * from Notification WHERE Notification.sensor_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResults = statement.executeQuery();

            while (queryResults.next()) {
                notifications.add(new Notification(
                        queryResults.getInt("id"),
                        queryResults.getString("email"),
                        new Threshold(
                                Threshold.Operator.values()[queryResults.getInt("operator")],
                                queryResults.getFloat("threshold")
                        )
                ));
            }

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.populateNotifications error: " + e.getMessage());
        }
        return notifications;
    }

    private List<Measurement> populateMeasurements(int sensorID) {
        List<Measurement> measurements = new ArrayList<>();

        try {
            String sql = "SELECT * from Measurement WHERE Measurement.sensor_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResults = statement.executeQuery();

            while (queryResults.next()) {
                measurements.add(new Measurement(
                        queryResults.getTimestamp("timeStamp"),
                        queryResults.getFloat("value")
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.populateMeasurements error: " + e.getMessage());
        }
        return measurements;
    }

    public void toggleSensor(int sensorID) {
        Sensor sensor = getSensor(sensorID);

        if (sensor.isEnabled()) {
            TaskDispatcher.stopRecording(sensorID);
            toggleSensorInDB(sensorID, false);
        }
        else {
            TaskDispatcher.addSensorRecorder(sensor, this);
            toggleSensorInDB(sensorID, true);
        }
    }

    private void toggleSensorInDB(int sensorID, boolean setEnabled) {
        try {
            String sql = "UPDATE Sensor SET Sensor.enabled = ? WHERE Sensor.ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, setEnabled);
            statement.setInt(2, sensorID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.toggleSensorInDB error: " + e.getMessage());
        }
    }

    void recordData(int sensorID, Measurement measurement) {
        clearData(sensorID);
        try {
            String sql = "INSERT INTO Measurement (" +
                    "sensor_ID, " +
                    "timestamp, " +
                    "value" +
                    ") VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);
            statement.setTimestamp(2, measurement.getTimeStamp());
            statement.setDouble(3, measurement.getValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.recordData error: " + e.getMessage());
        }
    }

    private void clearData(int sensorID) {
        try {
            String sql = "DELETE FROM Measurement WHERE sensor_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.recordData error: " + e.getMessage());
        }
    }

    public void addNotification(int sensorID, Notification notification) {
        try {
            String sql = "INSERT INTO Notification (" +
                    "sensor_ID, " +
                    "email, " +
                    "operator," +
                    "threshold" +
                    ") VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);
            statement.setString(2, notification.getEmail());
            statement.setInt(3, notification.getThreshold().getOperator().ordinal());
            statement.setFloat(4, notification.getThreshold().getValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.addNotification error: " + e.getMessage());
        }
    }

    public void deleteNotification(int notificationID) {
        try {
            String sql = "DELETE FROM Notification WHERE Notification.ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.deleteNotification error: " + e.getMessage());
        }
    }
}
