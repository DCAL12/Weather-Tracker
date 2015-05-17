package services;

import models.Notification;
import models.Threshold;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DAO {

    public List<Notification> getNotifications(int sensorID) {

        List<Notification> notifications = new ArrayList<>();

        try {
            String sql =
                    "SELECT * from Notification" +
                    "JOIN Recipient WHERE Notification.id = Recipient.notification_id " +
                    "AND Notification.sensor_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);

            ResultSet queryResults = statement.executeQuery();

            while (queryResults.next()) {

                int notificationID = queryResults.getInt("id");
                Notification newNotification = new Notification(notificationID,
                        new Threshold(Threshold.Operator.values()[queryResults.getInt("operator")],
                                queryResults.getFloat("threshold")));


                // Populate recipients for each notification
                try {
                    sql = "SELECT email from Recipient WHERE notification_ID = ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, notificationID);

                    queryResults = statement.executeQuery();

                    while (queryResults.next()) {
                        newNotification.addRecipient(queryResults.getString("email"));
                    }

                } catch (SQLException e) {
                    System.out.println("services.NotificationDAO.getNotifications error: " + e.getMessage());
                }

                notifications.add(newNotification);
            }

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.getNotifications error: " + e.getMessage());
        }
        return notifications;
    }

    public void addNotification(int sensorID, Notification notification) {
        try {
            String sql =
                    "INSERT INTO Notification (" +
                    "sensor_ID, " +
                    "operator," +
                    "threshold" +
                    ") VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sensorID);
            statement.setInt(2, notification.getThreshold().getOperator().ordinal());
            statement.setFloat(3, notification.getThreshold().getValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.addNotification error: " + e.getMessage());
        }
    }

    public void addRecipient(int notificationID, String email) {
        try {
            String sql =
                    "INSERT INTO Recipient (" +
                            "notification_ID, " +
                            "email" +
                            ") VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationID);
            statement.setString(2, email);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.addNotification error: " + e.getMessage());
        }
    }

    public void updateNotification(int notificationID, Notification notification) {

        try {
            String sql =
                    "UPDATE Notification SET " +
                    "operator = ?, " +
                    "threshold = ?" +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, notification.getThreshold().getOperator().toString());
            statement.setFloat(2, notification.getThreshold().getValue());
            statement.setInt(3, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.updateNotification error: " + e.getMessage());
        }
    }

    public void deleteNotification(int notificationID) {
        try {
            String sql = "DELETE FROM Notification WHERE Notification.ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.deleteNotification error: " + e.getMessage());
        }

        // Clean up corresponding recipients
        deleteRecipients(notificationID);
    }

    public void deleteRecipient(int notificationID, String email) {
        try {
            String sql =
                    "DELETE FROM Recipient " +
                    "WHERE notification_ID = ? " +
                    "AND email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationID);
            statement.setString(2, email);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.deleteRecipient error: " + e.getMessage());
        }
    }

    private void deleteRecipients(int notificationID) {
        try {
            String sql =
                    "DELETE FROM Recipient WHERE notification_ID = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationID);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("services.NotificationDAO.deleteRecipient error: " + e.getMessage());
        }
    }
}
