package services.dataAccess;

import models.Notification;
import models.Threshold;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DAO {

    private static NotificationDAO instance;
    private NotificationDAO() {
        super();
    }
    public static NotificationDAO getInstance() {
        if (instance == null) {
            instance = new NotificationDAO();
        }
        return instance;
    }

    public List<Notification> getNotifications(int sensorID) {

        String outerSql =
                "SELECT * from Notification WHERE Notification.sensor_ID = ?";
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement outerStatement = connection.prepareStatement(outerSql)
        ){
            outerStatement.setInt(1, sensorID);

            ResultSet outerQueryResults = outerStatement.executeQuery();

            while (outerQueryResults.next()) {

                int notificationID = outerQueryResults.getInt("id");
                Notification newNotification = new Notification(notificationID,
                        new Threshold(Threshold.Operator.values()[outerQueryResults.getInt("operator")],
                                outerQueryResults.getFloat("threshold")));


                // Populate recipients for each notification
                String innerSql = "SELECT email from Recipient WHERE notification_ID = ?";
                try (PreparedStatement innerStatement = connection.prepareStatement(innerSql)){

                    innerStatement.setInt(1, notificationID);

                    ResultSet innerQueryResults = innerStatement.executeQuery();

                    while (innerQueryResults.next()) {
                        newNotification.addRecipient(innerQueryResults.getString("email"));
                    }

                } catch (SQLException e) {
                    System.out.println("services.dataAccess.NotificationDAO.getNotifications error: " + e.getMessage());
                }
                notifications.add(newNotification);
            }

        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.getNotifications error: " + e.getMessage());
        }
        return notifications;
    }

    public void addNotification(int sensorID, Notification notification, String email) {

        String sql =
                "INSERT INTO Notification (" +
                        "sensor_ID, " +
                        "operator," +
                        "threshold" +
                        ") VALUES (?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ){
            statement.setInt(1, sensorID);
            statement.setInt(2, notification.getThreshold().getOperator().ordinal());
            statement.setFloat(3, notification.getThreshold().getValue());

            statement.executeUpdate();

            // Add recipient
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                addRecipient(result.getInt(1), email);
            }
        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.addNotification error: " + e.getMessage());
        }
    }

    public void addRecipient(int notificationID, String email) {

        String sql =
                "INSERT INTO Recipient (" +
                        "notification_ID, " +
                        "email" +
                        ") VALUES (?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, notificationID);
            statement.setString(2, email);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.addRecipient error: " + e.getMessage());
        }
    }

    public void updateNotification(int notificationID, Notification notification) {

        String sql =
                "UPDATE Notification SET " +
                        "operator = ?, " +
                        "threshold = ?" +
                        "WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setString(1, notification.getThreshold().getOperator().toString());
            statement.setFloat(2, notification.getThreshold().getValue());
            statement.setInt(3, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.updateNotification error: " + e.getMessage());
        }
    }

    public void deleteNotification(int notificationID) {

        String sql = "DELETE FROM Notification WHERE Notification.ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.deleteNotification error: " + e.getMessage());
        }

        // Clean up corresponding recipients
        deleteRecipients(notificationID);
    }

    public void deleteRecipient(int notificationID, String email) {

        String sql =
                "DELETE FROM Recipient " +
                        "WHERE notification_ID = ? " +
                        "AND email = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, notificationID);
            statement.setString(2, email);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.deleteRecipient error: " + e.getMessage());
        }
    }

    private void deleteRecipients(int notificationID) {

        String sql = "DELETE FROM Recipient WHERE notification_ID = ? ";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.dataAccess.NotificationDAO.deleteRecipient error: " + e.getMessage());
        }
    }
}
