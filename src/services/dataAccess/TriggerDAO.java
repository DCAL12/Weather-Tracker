package services.dataAccess;

import models.Trigger;
import models.Threshold;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TriggerDAO extends DAO {

    private static TriggerDAO instance;
    private TriggerDAO() {
        super();
    }
    public static TriggerDAO getInstance() {
        if (instance == null) {
            instance = new TriggerDAO();
        }
        return instance;
    }

    public List<Trigger> getTriggers(int sensorID) {

        String outerSql =
                "SELECT * from `Trigger` WHERE sensor_id = ?";
        List<Trigger> triggers = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement outerStatement = connection.prepareStatement(outerSql)
        ){
            outerStatement.setInt(1, sensorID);

            ResultSet outerQueryResults = outerStatement.executeQuery();

            while (outerQueryResults.next()) {

                int notificationID = outerQueryResults.getInt("id");
                Trigger newTrigger = new Trigger(notificationID, sensorID,
                        new Threshold(Threshold.Operator.values()[outerQueryResults.getInt("operator")],
                                outerQueryResults.getFloat("threshold")));


                // Populate recipients for each notification
                String innerSql = "SELECT email from Recipient WHERE trigger_id = ?";
                try (PreparedStatement innerStatement = connection.prepareStatement(innerSql)){

                    innerStatement.setInt(1, notificationID);

                    ResultSet innerQueryResults = innerStatement.executeQuery();

                    while (innerQueryResults.next()) {
                        newTrigger.addRecipient(innerQueryResults.getString("email"));
                    }

                } catch (SQLException e) {
                    System.out.println("services.dataAccess.TriggerDAO.getTriggers error: " + e.getMessage());
                }
                triggers.add(newTrigger);
            }

        } catch (SQLException e) {
            System.out.println("services.dataAccess.TriggerDAO.getTriggers error: " + e.getMessage());
        }
        return triggers;
    }

    public void addTrigger(int sensorID, Trigger trigger, String email) {

        String sql =
                "INSERT INTO `Trigger` (" +
                        "sensor_id, " +
                        "operator," +
                        "threshold" +
                        ") VALUES (?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ){
            statement.setInt(1, sensorID);
            statement.setInt(2, trigger.getThreshold().getOperator().ordinal());
            statement.setFloat(3, trigger.getThreshold().getValue());

            statement.executeUpdate();

            // Add recipient
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                addRecipient(result.getInt(1), email);
            }
        } catch (SQLException e) {
            System.out.println("services.dataAccess.TriggerDAO.addTrigger error: " + e.getMessage());
        }
    }

    public void addRecipient(int triggerID, String email) {

        String sql =
                "INSERT INTO Recipient (" +
                        "trigger_id, " +
                        "email" +
                        ") VALUES (?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, triggerID);
            statement.setString(2, email);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.TriggerDAO.addRecipient error: " + e.getMessage());
        }
    }

    public void deleteTrigger(int triggerID) {

        String sql = "DELETE FROM `Trigger` WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, triggerID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.TriggerDAO.deleteTrigger error: " + e.getMessage());
        }

        // Clean up corresponding recipients
        deleteRecipients(triggerID);
    }

    public void deleteRecipient(int triggerID, String email) {

        String sql =
                "DELETE FROM Recipient " +
                        "WHERE trigger_id = ? " +
                        "AND email = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, triggerID);
            statement.setString(2, email);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.TriggerDAO.deleteRecipient error: " + e.getMessage());
        }
    }

    private void deleteRecipients(int triggerID) {

        String sql = "DELETE FROM Recipient WHERE trigger_id = ? ";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, triggerID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.TriggerDAO.deleteRecipient error: " + e.getMessage());
        }
    }
}
