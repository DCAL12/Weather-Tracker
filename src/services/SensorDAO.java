package services;

import models.Measurement;
import models.Sensor;
import models.Notification;
import models.Threshold;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SensorDAO {

    private Connection connection;

    public SensorDAO() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/weather",
                    "user", "123");
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
            String sql = "SELECT * from Sensors";
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
            String sql = "SELECT ALL from Sensors WHERE (id) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResult = statement.executeQuery(sql);
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
            String sql = "SELECT ALL from Notifications WHERE (sensor_ID) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResults = statement.executeQuery(sql);

            while (queryResults.next()) {
                notifications.add(new Notification(
                        queryResults.getInt("id"),
                        queryResults.getString("email"),
                        new Threshold(
                                Threshold.Operator.valueOf(queryResults.getString("operator")),
                                queryResults.getDouble("threshold")
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
            String sql = "SELECT ALL from Measurements WHERE (sensor_ID) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResults = statement.executeQuery(sql);

            while (queryResults.next()) {
                measurements.add(new Measurement(
                        queryResults.getDate("timeStamp"),
                        queryResults.getDouble("value")
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
            String sql = "UPDATE Sensors SET (enabled) VALUES (?);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, setEnabled);

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.toggleSensorInDB error: " + e.getMessage());
        }
    }

    void recordData(int sensorID, Measurement measurement) {
        clearData(sensorID);
        try {
            String sql = "INSERT INTO Measurement (" +
                    "Sensor_ID, " +
                    "Measurement_Timestamp, " +
                    "Measurement_Value," +
                    ") VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);
            statement.setDate(2, measurement.getTimeStamp());
            statement.setDouble(3, measurement.getValue());

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.recordData error: " + e.getMessage());
        }
    }

    private void clearData(int sensorID) {
        try {
            String sql = "DELETE FROM Measurement (Sensor_ID) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.recordData error: " + e.getMessage());
        }
    }

    public void addNotification(int sensorID, Notification notification) {
        try {
            String sql = "INSERT INTO notifications (" +
                    "sensorID, " +
                    "notification_email, " +
                    "notification_operator," +
                    "notification_threshold" +
                    ") VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);
            statement.setString(2, notification.getEmail());
            statement.setString(3, notification.getThreshold().getOperator().toString());
            statement.setDouble(4, notification.getThreshold().getValue());

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.addNotification error: " + e.getMessage());
        }
    }

    public void deleteNotification(int notificationID) {
        try {
            String sql = "DELETE FROM notifications WHERE (id) VALUES (?);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationID);

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("services.SensorDAO.deleteNotification error: " + e.getMessage());
        }
    }
}
