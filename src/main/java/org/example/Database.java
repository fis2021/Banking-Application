package org.example;

import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Database {
    private final static String url = "jdbc:sqlite:src\\main\\resources\\database.db";
    private static Connection connection;
    private static Statement statement;

    public void initialize() {
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
                statement.execute(String.format("SELECT * FROM notifications WHERE ownerPIN='%s'",
                        user.getPersonalInformation().getPin()));
            else
                statement.execute("SELECT * FROM notifications");
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
                        resultSet.getString("birthdate"), resultSet.getString("gender"),
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
                balanceInformations.add(0, new User.BalanceInformation(resultSet.getDouble("availableEUR"),
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
    public boolean registerUser(User.PersonalInformation personalInformation, User.AccountInformation accountInformation) {
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
                    personalInformation.getPin(), personalInformation.getBirthday(), personalInformation.getGender(),
                    personalInformation.getAddress(), personalInformation.getZip()));

            statement.execute(String.format("INSERT INTO accountInformation VALUES('%s', '%s', '%s', '%s')",
                    accountInformation.getCardNumber(), accountInformation.getEmail(), accountInformation.getUsername(),
                    accountInformation.getPassword()));

            statement.execute(String.format("INSERT INTO balanceInformation VALUES('%s', 0.0, 0.0, 0.0, 0.0, 0.0, 0.0," +
                    " 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,)",
                    personalInformation.getPin()));

            return true;
        } catch(SQLException e) {
            System.out.println("Error uploading new user to database");
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
