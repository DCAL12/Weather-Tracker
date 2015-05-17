package services;

import models.Observation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObservationDAO extends DAO {

    public List<Observation> getObservations(int sensorID) {
        List<Observation> observations = new ArrayList<>();

        try {
            String sql = "SELECT * from Observation WHERE Observatioin.sensor_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResults = statement.executeQuery();

            while (queryResults.next()) {
                observations.add(new Observation(
                        queryResults.getTimestamp("timeStamp"),
                        queryResults.getFloat("value")
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println("services.ObservationDAO.getObservations error: " + e.getMessage());
        }
        return observations;
    }

    public void addObservation(int sensorID, Observation observation) {

        try {
            String sql = "INSERT INTO Measurement (" +
                    "sensor_ID, " +
                    "timestamp, " +
                    "value" +
                    ") VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);
            statement.setTimestamp(2, observation.getTimeStamp());
            statement.setFloat(3, observation.getValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.ObservationDAO.addObservation error: " + e.getMessage());
        }
    }

    public void clearObservations(int sensorID) {
        try {
            String sql = "DELETE FROM Measurement WHERE sensor_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.ObservationDAO.clearObservations error: " + e.getMessage());
        }
    }

}
