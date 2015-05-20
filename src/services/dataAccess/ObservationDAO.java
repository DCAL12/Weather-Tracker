package services.dataAccess;

import models.Observation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObservationDAO extends DAO {

    private static ObservationDAO instance;
    private ObservationDAO() {
        super();
    }
    public static ObservationDAO getInstance() {
        if (instance == null) {
            instance = new ObservationDAO();
        }
        return instance;
    }

    public List<Observation> getObservations(int sensorID) {

        String sql = "SELECT * from Observation WHERE Observation.sensor_ID = ?";
        List<Observation> observations = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
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
            System.out.println("services.dataAccess.ObservationDAO.getObservations error: " + e.getMessage());
        }
        return observations;
    }

    public void addObservation(int sensorID, Observation observation) {

        String sql = "INSERT INTO Observation (" +
                "sensor_ID, " +
                "timestamp, " +
                "value" +
                ") VALUES (?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, sensorID);
            statement.setTimestamp(2, observation.getTimeStamp());
            statement.setFloat(3, observation.getValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.ObservationDAO.addObservation error: " + e.getMessage());
        }
    }

    public void clearObservations(int sensorID) {

        String sql = "DELETE FROM Observation WHERE sensor_ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, sensorID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.ObservationDAO.clearObservations error: " + e.getMessage());
        }
    }
}
