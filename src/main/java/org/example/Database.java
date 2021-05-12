package org.example;

import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class Database {
    private final static String url = "jdbc:sqlite:src\\main\\resources\\database.db";
    private static Connection connection;
    private static Statement statement;

    public static void initialize() {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection to database has failed");
            e.printStackTrace();
        }
    }

    public static void terminate() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Disconnection to database has failed");
            e.printStackTrace();
        }
    }

    /*
        Returns all the transaction in reverse order compared to the one in the database
        If a user is specified as param, only his/her transactions will be provided
    */
    public static ArrayList<TransactionModel> getAllTransactions(User user) {
        ArrayList<TransactionModel> transactionModels = new ArrayList<>();
        try {
            if(user != null)
                statement.execute(String.format("SELECT * FROM transactions WHERE ownerPIN='%s'",
                        user.getPersonalInformation().getPin()));
            else
                statement.execute("SELECT * FROM transactions");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next())
                transactionModels.add(0, new TransactionModel(resultSet.getString("ownerPIN"),
                        resultSet.getString("senderName"), resultSet.getString("receiverName"),
                        resultSet.getString("type"), resultSet.getString("currency"),
                        resultSet.getString("date"), resultSet.getDouble("amount")));
            resultSet.close();
            return transactionModels.size() == 0 ? null : transactionModels;
        } catch (SQLException e) {
            System.out.println("Error loading transactions from database");
            e.printStackTrace();
            return null;
        }
    }

    /*
        TODO add comment
    */
    public static ArrayList<NotificationModel> getAllNotifications(User user) {
        ArrayList<NotificationModel> notificationModels = new ArrayList<>();
        try {
            if(user != null)
                statement.execute(String.format("SELECT * FROM notifications WHERE ownerPIN='%s' AND status='Unresolved'",
                        user.getPersonalInformation().getPin()));
            else
                statement.execute("SELECT * FROM notifications WHERE status='Unresolved'");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next())
                notificationModels.add(0, new NotificationModel(resultSet.getString("ownerPIN"),
                        resultSet.getString("senderName"), resultSet.getString("senderCardNumber"),
                        resultSet.getString("receiverName"),
                        resultSet.getString("receiverCardNumber"), resultSet.getString("type"),
                        resultSet.getString("currency"), resultSet.getString("status"),
                        resultSet.getDouble("amount")));
            resultSet.close();
            return notificationModels.size() == 0 ? null : notificationModels;
        } catch (SQLException e) {
            System.out.println("Error loading notifications from database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User.PersonalInformation> getAllPersonalInformation() {
        ArrayList<User.PersonalInformation> personalInformations = new ArrayList<>();
        try {
            statement.execute("SELECT * FROM personalInformation");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next())
                personalInformations.add(0, new User.PersonalInformation(resultSet.getString("fname"),
                        resultSet.getString("lname"), resultSet.getString("pin"),
                        resultSet.getString("birthdate"), resultSet.getString("sex"),
                        resultSet.getString("address"), resultSet.getString("zip")));
            resultSet.close();
            return personalInformations.size() == 0 ? null : personalInformations;
        } catch (SQLException e) {
            System.out.println("Error loading personal information from database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User.AccountInformation> getAllAccountInformation() {
        ArrayList<User.AccountInformation> accountInformations = new ArrayList<>();
        try {
            statement.execute("SELECT * FROM accountInformation");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next())
                accountInformations.add(0, new User.AccountInformation(resultSet.getString("cardnumber"),
                        resultSet.getString("email"), resultSet.getString("username"),
                        resultSet.getString("password")));
            resultSet.close();
            return accountInformations.size() == 0 ? null : accountInformations;
        } catch (SQLException e) {
            System.out.println("Error loading account information from database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User.BalanceInformation> getAllBalanceInformation() {
        ArrayList<User.BalanceInformation> balanceInformations = new ArrayList<>();
        try {
            statement.execute("SELECT * FROM balanceInformation");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next())
                balanceInformations.add(0, new User.BalanceInformation(resultSet.getString("ownerPIN"),
                        resultSet.getDouble("availableEUR"),
                        resultSet.getDouble("availableUSD"), resultSet.getDouble("availableRON"),
                        resultSet.getDouble("awaitingEUR"), resultSet.getDouble("awaitingUSD"),
                        resultSet.getDouble("awaitingRON"), resultSet.getDouble("totalWithdrawnEUR"),
                        resultSet.getDouble("totalWithdrawnUSD"), resultSet.getDouble("totalWithdrawnRON"),
                        resultSet.getDouble("totalDepositedEUR"), resultSet.getDouble("totalDepositedUSD"),
                        resultSet.getDouble("totalDepositedRON"), resultSet.getDouble("totalTransfersEUR"),
                        resultSet.getDouble("totalTransfersUSD"), resultSet.getDouble("totalTransfersRON"),
                        resultSet.getDouble("totalWithdrawsEUR"), resultSet.getDouble("totalWithdrawsUSD"),
                        resultSet.getDouble("totalWithdrawsRON"), resultSet.getDouble("totalDepositsEUR"),
                        resultSet.getDouble("totalDepositsUSD"), resultSet.getDouble("totalDepositsRON")));
            resultSet.close();
            return balanceInformations.size() == 0 ? null : balanceInformations;
        } catch (SQLException e) {
            System.out.println("Error loading balance information from database");
            e.printStackTrace();
            return null;
        }
    }

    public static String getURL() { return url; }

    public static Connection getConnection() { return connection; }

    /*
        Returns TRUE if user is successfully uploaded to the internal database
    */
    public static boolean registerUser(User.PersonalInformation personalInformation, User.AccountInformation accountInformation) {
        ArrayList<User.PersonalInformation> personalInformations = getAllPersonalInformation();
        ArrayList<User.AccountInformation> accountInformations = getAllAccountInformation();

        // check if user already exists in the database
        if(personalInformations != null)
            for(User.PersonalInformation i : personalInformations)
                if(i.equals(personalInformation))
                    return false;

        if(accountInformations != null)
            for(User.AccountInformation i : accountInformations)
                if(i.equals(accountInformation))
                    return false;

        // upload user to database
        try {
            statement.execute(String.format("INSERT INTO personalInformation VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    personalInformation.getFname(), personalInformation.getLname(),
                    personalInformation.getPin(), personalInformation.getBirthdate(), personalInformation.getGender(),
                    personalInformation.getAddress(), personalInformation.getZip()));

            statement.execute(String.format("INSERT INTO accountInformation VALUES('%s', '%s', '%s', '%s')",
                    accountInformation.getCardNumber(), accountInformation.getEmail(), accountInformation.getUsername(),
                    accountInformation.getPassword()));

            statement.execute(String.format("INSERT INTO balanceInformation VALUES('%s', 0.0, 0.0, 0.0, 0.0, 0.0, 0.0," +
                    " 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)",
                            " 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)",
                    personalInformation.getPin()));

            return true;
        } catch(SQLException e) {
            System.out.println("Error uploading new user to database");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendNotification(NotificationModel notificationModel, User.BalanceInformation balanceInformation) {
        try {
            // Add notification to database
            statement.execute(String.format("INSERT INTO notifications VALUES('%s', '%s', '%s', '%s', '%s', '%s', %f, '%s', '%s')",
                    notificationModel.getOwnerPIN(), notificationModel.getSenderName(), notificationModel.getSenderCardNumber(),
                    notificationModel.getReceiverName(), notificationModel.getReceiverCardNumber(), notificationModel.getType(),
                    notificationModel.getAmount(), notificationModel.getCurrency(), notificationModel.getStatus()));

            // Add funds to waiting if type is send
            if(notificationModel.getType().equals("Send"))
                switch (notificationModel.getCurrency()) {
                    case "EUR":
                        balanceInformation.setAvailableEUR(balanceInformation.getAvailableEUR() - notificationModel.getAmount());
                        balanceInformation.setAwaitingEUR(balanceInformation.getAwaitingEUR() + notificationModel.getAmount());
                        statement.execute(String.format("UPDATE balanceInformation SET availableEUR=%f, awaitingEUR=%f WHERE ownerPIN='%s'",
                                balanceInformation.getAvailableEUR(), balanceInformation.getAwaitingEUR(), balanceInformation.getOwnerPIN()));
                        break;
                    case "USD":
                        balanceInformation.setAvailableUSD(balanceInformation.getAvailableUSD() - notificationModel.getAmount());
                        balanceInformation.setAwaitingUSD(balanceInformation.getAwaitingUSD() + notificationModel.getAmount());
                        statement.execute(String.format("UPDATE balanceInformation SET availableUSD=%f, awaitingUSD=%f WHERE ownerPIN='%s'",
                                balanceInformation.getAvailableUSD(), balanceInformation.getAwaitingUSD(), balanceInformation.getOwnerPIN()));
                        break;
                    case "RON":
                        balanceInformation.setAvailableRON(balanceInformation.getAvailableRON() - notificationModel.getAmount());
                        balanceInformation.setAwaitingRON(balanceInformation.getAwaitingRON() + notificationModel.getAmount());
                        statement.execute(String.format("UPDATE balanceInformation SET availableRON=%f, awaitingRON=%f WHERE ownerPIN='%s'",
                                balanceInformation.getAvailableRON(), balanceInformation.getAwaitingRON(), balanceInformation.getOwnerPIN()));
                        break;
                }
            return true;
        } catch (SQLException e) {
            System.out.println("Error uploading notification to database");
            e.printStackTrace();
            return false;
        }
    }

    /*
        TODO
    */
    public static boolean sendAcceptTransfer(String senderPIN, String receiverPIN, double amount, String currency) {
        try {
            // Find balanceInformation and personalInformation
            User.BalanceInformation senderBalance = null, receiverBalance = null;
            User.PersonalInformation senderPInfo = null, receiverPInfo = null;

            for(User.BalanceInformation i : Objects.requireNonNull(Database.getAllBalanceInformation()))
                if(i.getOwnerPIN().equals(senderPIN))
                    senderBalance = i;
                else if(i.getOwnerPIN().equals(receiverPIN))
                    receiverBalance = i;

            for(User.PersonalInformation i : Objects.requireNonNull(Database.getAllPersonalInformation()))
                if(i.getPin().equals(senderPIN))
                    senderPInfo = i;
                else if(i.getPin().equals(receiverPIN))
                    receiverPInfo = i;

            // Perform transaction specific operations
            assert senderBalance != null;
            assert receiverBalance != null;
            switch(currency) {
                case "EUR":
                    senderBalance.setAwaitingEUR(senderBalance.getAwaitingEUR() - amount);
                    senderBalance.setTotalTransfersEUR(senderBalance.getTotalTransfersEUR() + 1);
                    receiverBalance.setAvailableEUR(receiverBalance.getAvailableEUR() + amount);
                    receiverBalance.setTotalTransfersEUR(receiverBalance.getTotalTransfersEUR() + 1);
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setAvailableEUR(receiverBalance.getAvailableEUR());
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setTotalTransfersEUR(receiverBalance.getTotalTransfersEUR());
                    break;
                case "USD":
                    senderBalance.setAwaitingUSD(senderBalance.getAwaitingUSD() - amount);
                    senderBalance.setTotalTransfersUSD(senderBalance.getTotalTransfersUSD() + 1);
                    receiverBalance.setAvailableUSD(receiverBalance.getAvailableUSD() + amount);
                    receiverBalance.setTotalTransfersUSD(receiverBalance.getTotalTransfersUSD() + 1);
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setAvailableUSD(receiverBalance.getAvailableUSD());
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setTotalTransfersUSD(receiverBalance.getTotalTransfersUSD());
                    break;
                case "RON":
                    senderBalance.setAwaitingRON(senderBalance.getAwaitingRON() - amount);
                    senderBalance.setTotalTransfersRON(senderBalance.getTotalTransfersRON() + 1);
                    receiverBalance.setAvailableRON(receiverBalance.getAvailableRON() + amount);
                    receiverBalance.setTotalTransfersRON(receiverBalance.getTotalTransfersRON() + 1);
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setAvailableRON(receiverBalance.getAvailableRON());
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setTotalTransfersRON(receiverBalance.getTotalTransfersRON());
                    break;
            }

            // Update information to database
            assert senderPInfo != null;
            assert receiverPInfo != null;

            switch(currency) {
                case "EUR":
                    statement.execute(String.format("UPDATE balanceInformation SET awaitingEUR=%f, totalTransfersEUR=%f WHERE ownerPIN='%s'",
                            senderBalance.getAwaitingEUR(), senderBalance.getTotalTransfersEUR(), senderBalance.getOwnerPIN()));
                    statement.execute(String.format("UPDATE balanceInformation SET availableEUR=%f, totalTransfersEUR=%f WHERE ownerPIN='%s'",
                            receiverBalance.getAvailableEUR(), receiverBalance.getTotalTransfersEUR(), receiverBalance.getOwnerPIN()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'EUR', '%s')",
                            senderPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'EUR', '%s')",
                            receiverPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    break;
                case "USD":
                    statement.execute(String.format("UPDATE balanceInformation SET awaitingUSD=%f, totalTransfersUSD=%f WHERE ownerPIN='%s'",
                            senderBalance.getAwaitingUSD(), senderBalance.getTotalTransfersUSD(), senderBalance.getOwnerPIN()));
                    statement.execute(String.format("UPDATE balanceInformation SET availableUSD=%f, totalTransfersUSD=%f WHERE ownerPIN='%s'",
                            receiverBalance.getAvailableUSD(), receiverBalance.getTotalTransfersUSD(), receiverBalance.getOwnerPIN()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'USD', '%s')",
                            senderPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'USD', '%s')",
                            receiverPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    break;
                case "RON":
                    statement.execute(String.format("UPDATE balanceInformation SET awaitingRON=%f, totalTransfersRON=%f WHERE ownerPIN='%s'",
                            senderBalance.getAwaitingRON(), senderBalance.getTotalTransfersRON(), senderBalance.getOwnerPIN()));
                    statement.execute(String.format("UPDATE balanceInformation SET availableRON=%f, totalTransfersRON=%f WHERE ownerPIN='%s'",
                            receiverBalance.getAvailableRON(), receiverBalance.getTotalTransfersRON(), receiverBalance.getOwnerPIN()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'RON', '%s')",
                            senderPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'RON', '%s')",
                            receiverPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    break;
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setNotificationResolved(NotificationModel notification) {
        try {
            statement.execute(String.format("UPDATE notifications SET status='Resolved' WHERE ownerPIN='%s' AND" +
                    " senderName='%s' AND senderCardNumber='%s' AND receiverName='%s' AND receiverCardNumber='%s' AND " +
                    "type='%s' AND amount='%f' AND currency='%s'", notification.getOwnerPIN(), notification.getSenderName(),
                            " senderName='%s' AND senderCardNumber='%s' AND receiverName='%s' AND receiverCardNumber='%s' AND " +
                            "type='%s' AND amount='%f' AND currency='%s'", notification.getOwnerPIN(), notification.getSenderName(),
                    notification.getSenderCardNumber(), notification.getReceiverName(), notification.getReceiverCardNumber(),
                    notification.getType(), notification.getAmount(), notification.getCurrency()));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
        TODO
    */
    public static boolean sendDeclineTransfer(String senderPIN, String receiverPIN, double amount, String currency) {
        try {
            // Find balanceInformation and personalInformation
            User.BalanceInformation senderBalance = null, receiverBalance = null;
            User.PersonalInformation senderPInfo = null, receiverPInfo = null;

            for (User.BalanceInformation i : Objects.requireNonNull(Database.getAllBalanceInformation()))
                if (i.getOwnerPIN().equals(senderPIN))
                    senderBalance = i;
                else if (i.getOwnerPIN().equals(receiverPIN))
                    receiverBalance = i;

            for (User.PersonalInformation i : Objects.requireNonNull(Database.getAllPersonalInformation()))
                if (i.getPin().equals(senderPIN))
                    senderPInfo = i;
                else if (i.getPin().equals(receiverPIN))
                    receiverPInfo = i;

            // Perform transaction specific operations
            assert senderBalance != null;
            assert receiverBalance != null;
            switch (currency) {
                case "EUR":
                    senderBalance.setAwaitingEUR(senderBalance.getAwaitingEUR() - amount);
                    senderBalance.setAvailableEUR(senderBalance.getAvailableEUR() + amount);
                    break;
                case "USD":
                    senderBalance.setAwaitingUSD(senderBalance.getAwaitingUSD() - amount);
                    senderBalance.setAvailableUSD(senderBalance.getAvailableUSD() + amount);
                    break;
                case "RON":
                    senderBalance.setAwaitingRON(senderBalance.getAwaitingRON() - amount);
                    senderBalance.setAvailableRON(senderBalance.getAvailableRON() + amount);
                    break;
            }

            // Update information to database
            assert senderPInfo != null;
            assert receiverPInfo != null;
            switch (currency) {
                case "EUR":
                    statement.execute(String.format("UPDATE balanceInformation SET availableEUR=%f, awaitingEUR=%f WHERE ownerPin='%s'",
                            senderBalance.getAvailableEUR(), senderBalance.getAwaitingEUR(), senderBalance.getOwnerPIN()));
                    break;
                case "USD":
                    statement.execute(String.format("UPDATE balanceInformation SET availableUSD=%f, awaitingUSD=%f WHERE ownerPin='%s'",
                            senderBalance.getAvailableUSD(), senderBalance.getAwaitingUSD(), senderBalance.getOwnerPIN()));
                    break;
                case "RON":
                    statement.execute(String.format("UPDATE balanceInformation SET availableRON=%f, awaitingRON=%f WHERE ownerPin='%s'",
                            senderBalance.getAvailableRON(), senderBalance.getAwaitingRON(), senderBalance.getOwnerPIN()));
                    break;
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
        TODO
    */
    public static boolean requestAcceptTransfer(String senderPIN, String receiverPIN, double amount, String currency) {
        try {
            // Find balanceInformation and personalInformation
            User.BalanceInformation senderBalance = null, receiverBalance = null;
            User.PersonalInformation senderPInfo = null, receiverPInfo = null;

            for (User.BalanceInformation i : Objects.requireNonNull(Database.getAllBalanceInformation()))
                if (i.getOwnerPIN().equals(senderPIN))
                    senderBalance = i;
                else if (i.getOwnerPIN().equals(receiverPIN))
                    receiverBalance = i;

            for (User.PersonalInformation i : Objects.requireNonNull(Database.getAllPersonalInformation()))
                if (i.getPin().equals(senderPIN))
                    senderPInfo = i;
                else if (i.getPin().equals(receiverPIN))
                    receiverPInfo = i;

            // Check if user has the sum to effectuate the transfer
            assert senderBalance != null;
            assert receiverBalance != null;
            switch (currency) {
                case "EUR":
                    if(receiverBalance.getAvailableEUR() < amount) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Insufficient funds");
                        alert.showAndWait();
                        return false;
                    }
                    break;
                case "USD":
                    if(receiverBalance.getAvailableUSD() < amount) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Insufficient funds");
                        alert.showAndWait();
                        return false;
                    }
                    break;
                case "RON":
                    if(receiverBalance.getAvailableRON() < amount) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Insufficient funds");
                        alert.showAndWait();
                        return false;
                    }
                    break;
            }

            // Perform transaction specific operations
            switch (currency) {
                case "EUR":
                    senderBalance.setAvailableEUR(senderBalance.getAvailableEUR() + amount);
                    senderBalance.setTotalTransfersEUR(senderBalance.getTotalTransfersEUR() + 1);
                    receiverBalance.setAvailableEUR(receiverBalance.getAvailableEUR() - amount);
                    receiverBalance.setTotalTransfersEUR(receiverBalance.getTotalTransfersEUR() + 1);
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setAvailableEUR(receiverBalance.getAvailableEUR());
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setTotalTransfersEUR(receiverBalance.getTotalTransfersEUR());
                    break;
                case "USD":
                    senderBalance.setAvailableUSD(senderBalance.getAvailableUSD() + amount);
                    senderBalance.setTotalTransfersUSD(senderBalance.getTotalTransfersUSD() + 1);
                    receiverBalance.setAvailableUSD(receiverBalance.getAvailableUSD() - amount);
                    receiverBalance.setTotalTransfersUSD(receiverBalance.getTotalTransfersUSD() + 1);
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setAvailableUSD(receiverBalance.getAvailableUSD());
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setTotalTransfersUSD(receiverBalance.getTotalTransfersUSD());
                    break;
                case "RON":
                    senderBalance.setAvailableRON(senderBalance.getAvailableRON() + amount);
                    senderBalance.setTotalTransfersRON(senderBalance.getTotalTransfersRON() + 1);
                    receiverBalance.setAvailableRON(receiverBalance.getAvailableRON() - amount);
                    receiverBalance.setTotalTransfersRON(receiverBalance.getTotalTransfersRON() + 1);
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setAvailableRON(receiverBalance.getAvailableRON());
                    ControllerDashboard.getLoggedInUser().getBalanceInformation().setTotalTransfersRON(receiverBalance.getTotalTransfersRON());
                    break;
            }

            // Update information to database
            assert senderPInfo != null;
            assert receiverPInfo != null;
            switch (currency) {
                case "EUR":
                    statement.execute(String.format("UPDATE balanceInformation SET availableEUR=%f, totalTransfersEUR=%f WHERE ownerPIN='%s'",
                            receiverBalance.getAvailableEUR(), receiverBalance.getTotalTransfersEUR(), receiverPIN));
                    statement.execute(String.format("UPDATE balanceInformation SET availableEUR=%f, totalTransfersEUR=%f WHERE ownerPIN='%s'",
                            senderBalance.getAvailableEUR(), senderBalance.getTotalTransfersEUR(), senderPIN));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'EUR', '%s')",
                            senderPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'EUR', '%s')",
                            receiverPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    break;
                case "USD":
                    statement.execute(String.format("UPDATE balanceInformation SET availableUSD=%f, totalTransfersUSD=%f WHERE ownerPIN='%s'",
                            receiverBalance.getAvailableUSD(), receiverBalance.getTotalTransfersUSD(), receiverPIN));
                    statement.execute(String.format("UPDATE balanceInformation SET availableUSD=%f, totalTransfersUSD=%f WHERE ownerPIN='%s'",
                            senderBalance.getAvailableUSD(), senderBalance.getTotalTransfersUSD(), senderPIN));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'USD', '%s')",
                            senderPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'USD', '%s')",
                            receiverPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    break;
                case "RON":
                    statement.execute(String.format("UPDATE balanceInformation SET availableRON=%f, totalTransfersRON=%f WHERE ownerPIN='%s'",
                            receiverBalance.getAvailableRON(), receiverBalance.getTotalTransfersRON(), receiverPIN));
                    statement.execute(String.format("UPDATE balanceInformation SET availableRON=%f, totalTransfersRON=%f WHERE ownerPIN='%s'",
                            senderBalance.getAvailableRON(), senderBalance.getTotalTransfersRON(), senderPIN));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'RON', '%s')",
                            senderPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Transfer', '%s', '%s', %f, 'RON', '%s')",
                            receiverPIN, senderPInfo.getFullName(), receiverPInfo.getFullName(), amount, LocalDate.now().toString()));
                    break;
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deposit(User user, double sum, String currency) {
        try {
            double tSum = sum;
            switch (currency) {
                case "EUR":
                    sum += user.getBalanceInformation().getAvailableEUR();
                    user.getBalanceInformation().setAvailableEUR(sum);
                    user.getBalanceInformation().setTotalDepositedEUR(user.getBalanceInformation().getTotalDepositedEUR() + tSum);
                    user.getBalanceInformation().setTotalDepositsEUR(user.getBalanceInformation().getTotalDepositsEUR() + 1);
                    statement.execute(String.format("UPDATE balanceInformation SET totalDepositedEUR = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalDepositedEUR(), user.getPersonalInformation().getPin()));
                    statement.execute(String.format("UPDATE balanceInformation SET totalDepositsEUR = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalDepositsEUR(), user.getPersonalInformation().getPin()));
                    break;
                case "USD":
                    sum += user.getBalanceInformation().getAvailableUSD();
                    user.getBalanceInformation().setAvailableUSD(sum);
                    user.getBalanceInformation().setTotalDepositedUSD(user.getBalanceInformation().getTotalDepositedUSD() + tSum);
                    user.getBalanceInformation().setTotalDepositsUSD(user.getBalanceInformation().getTotalDepositsUSD() + 1);
                    statement.execute(String.format("UPDATE balanceInformation SET totalDepositedUSD = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalDepositedUSD(), user.getPersonalInformation().getPin()));
                    statement.execute(String.format("UPDATE balanceInformation SET totalDepositsUSD = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalDepositsUSD(), user.getPersonalInformation().getPin()));
                    break;
                case "RON":
                    sum += user.getBalanceInformation().getAvailableRON();
                    user.getBalanceInformation().setAvailableRON(sum);
                    user.getBalanceInformation().setTotalDepositedRON(user.getBalanceInformation().getTotalDepositedRON() + tSum);
                    user.getBalanceInformation().setTotalDepositsRON(user.getBalanceInformation().getTotalDepositsRON() + 1);
                    statement.execute(String.format("UPDATE balanceInformation SET totalDepositedRON = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalDepositedRON(), user.getPersonalInformation().getPin()));
                    statement.execute(String.format("UPDATE balanceInformation SET totalDepositsRON = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalDepositsRON(), user.getPersonalInformation().getPin()));
                    break;
            }

            statement.execute(String.format("UPDATE balanceInformation SET available%s = %f WHERE ownerPIN='%s'",
                    currency, sum, user.getBalanceInformation().getOwnerPIN()));
            statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Deposit', '%s', 'Intellij Banking', %f, '%s', '%s')",
                    user.getPersonalInformation().getPin(), user.getPersonalInformation().getFullName(), tSum, currency, LocalDate.now().toString()));

            return true;
        } catch(SQLException e) {
            System.out.println("Error when trying to modify balance information");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean withdraw(User user, double sum, String currency) {
        try {
            double tSum = sum;
            switch (currency) {
                case "EUR":
                    sum = user.getBalanceInformation().getAvailableEUR() - sum;
                    user.getBalanceInformation().setAvailableEUR(sum);
                    user.getBalanceInformation().setTotalWithdrawnEUR(user.getBalanceInformation().getTotalWithdrawnEUR() + tSum);
                    user.getBalanceInformation().setTotalWithdrawsEUR(user.getBalanceInformation().getTotalWithdrawsEUR() + 1);
                    statement.execute(String.format("UPDATE balanceInformation SET totalWithdrawnEUR = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalWithdrawnEUR(), user.getPersonalInformation().getPin()));
                    statement.execute(String.format("UPDATE balanceInformation SET totalWithdrawsEUR = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalWithdrawsEUR(), user.getPersonalInformation().getPin()));
                    break;
                case "USD":
                    sum = user.getBalanceInformation().getAvailableUSD() - sum;
                    user.getBalanceInformation().setAvailableUSD(sum);
                    user.getBalanceInformation().setTotalWithdrawnUSD(user.getBalanceInformation().getTotalWithdrawnUSD() + tSum);
                    user.getBalanceInformation().setTotalWithdrawsUSD(user.getBalanceInformation().getTotalWithdrawsUSD() + 1);
                    statement.execute(String.format("UPDATE balanceInformation SET totalWithdrawnUSD = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalWithdrawnUSD(), user.getPersonalInformation().getPin()));
                    statement.execute(String.format("UPDATE balanceInformation SET totalWithdrawsUSD = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalWithdrawsUSD(), user.getPersonalInformation().getPin()));
                    break;
                case "RON":
                    sum = user.getBalanceInformation().getAvailableRON() - sum;
                    user.getBalanceInformation().setAvailableRON(sum);
                    user.getBalanceInformation().setTotalWithdrawnRON(user.getBalanceInformation().getTotalWithdrawnRON() + tSum);
                    user.getBalanceInformation().setTotalWithdrawsRON(user.getBalanceInformation().getTotalWithdrawsRON() + 1);
                    statement.execute(String.format("UPDATE balanceInformation SET totalWithdrawnRON = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalWithdrawnRON(), user.getPersonalInformation().getPin()));
                    statement.execute(String.format("UPDATE balanceInformation SET totalWithdrawsRON = %f WHERE ownerPIN = '%s'",
                            user.getBalanceInformation().getTotalWithdrawsRON(), user.getPersonalInformation().getPin()));
                    break;
            }

            statement.execute(String.format("UPDATE balanceInformation SET available%s = %f WHERE ownerPIN='%s'",
                    currency, sum, user.getBalanceInformation().getOwnerPIN()));
            statement.execute(String.format("INSERT INTO transactions VALUES('%s', 'Withdraw', 'Intellij Banking', '%s', %f, '%s', '%s')",
                    user.getPersonalInformation().getPin(), user.getPersonalInformation().getFullName(), tSum, currency, LocalDate.now().toString()));

            return true;
        } catch(SQLException e) {
            System.out.println("Error when trying to modify balance information");
            e.printStackTrace();
            return false;
        }
    }

    /*
        Encode password using MD5
    */
    public static String encodePasswordMD5(String password) {
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return generatedPassword;
    }

    /*
        Validates email address
    */
    public static boolean checkIfEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
