package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerDashboard {
    private static User loggedInUser = null;
    @FXML
    private GridPane dashboardGrid, notificationsGrid, depositGrid, withdrawGrid, transferGrid, privacyPolicyGrid;
    @FXML
    private TextField amountDeposit, amountWithdraw, amountTransfer, nameReceiverTransfer, pinReceiverTransfer,
            emailReceiverTransfer, cardnumReceiverTransfer;
    @FXML
    private Button depositRequest, withdrawRequest, transferRequest;

    @FXML
    private RadioButton sendTransfer, requestTransfer;

    @FXML
    private ComboBox<String> currencyDeposit, currencyWithdraw, currencyTransfer;

    @FXML
    private Label nameDeposit, pinDeposit, addressDeposit, cardNumberDeposit,
          nameWithdraw, pinWithdraw, addressWithdraw, cardNumberWithdraw,
          nameTransfer, pinTransfer, emailTransfer, cardNumberTransfer;

    @FXML
    private Label eurAmountProfile, totalTransfersEUR, amountAwaitingTransferEUR, totalWithdrawsEUR, amountTotalWithdrawnEUR,
          totalDepositsEUR, amountTotalDepositedEUR,
          usdAmountProfile, totalTransfersUSD, amountAwaitingTransferUSD, totalWithdrawsUSD, amountTotalWithdrawnUSD,
          totalDepositsUSD, amountTotalDepositedUSD,
          ronAmountProfile, totalTransfersRON, amountAwaitingTransferRON, totalWithdrawsRON, amountTotalWithdrawnRON,
          totalDepositsRON, amountTotalDepositedRON;

    @FXML
    private TableColumn<TransactionModel, String> transactionType, transactionEmitor, transactionReceiver, transactionCurrency,
            transactionDate;
    @FXML
    private TableColumn<TransactionModel, Double> transactionAmount;
    @FXML
    private TableView<TransactionModel> transactionsTableView;

    @FXML
    private TableColumn<NotificationModel, String> notificationSenderName, notificationType, notificationCurrency, notificationStatus;
    @FXML
    private TableColumn<NotificationModel, Double> notificationAmount;
    @FXML
    private TableColumn<NotificationModel, HBox> notificationAction;
    @FXML
    private TableView<NotificationModel> notificationsTableView;

    private final ArrayList<GridPane> grids = new ArrayList<>();
    private final ObservableList<TransactionModel> transactionModels = FXCollections.observableArrayList();
    private final ObservableList<NotificationModel> notificationModels = FXCollections.observableArrayList();

    @FXML
    private Button logout;

    /*
        //TODO add comment
    */
    public void initialize() {
        addAllGridsToArrayList();           // adds all submenus to a grid so they can e displayed at user's preference
        addListeners();                     // adds listeners to all field (eg. cant type letters in "Amount" field)
        loggedInUser = ControllerLogin.getLoggedInUser();   // gets logged in user's data from database
        setVolatileTextOnUI(loggedInUser.getBalanceInformation());  // TODO
        setStaticTextOnUI();                // sets user's data on UI
        setCellFactories();                 // sets tables to automatically update data
        addAllTransactions();               // adds all transactions historic locally
    }

    /*
        //TODO add comment
    */
    public void handleDashboard() {
        setVolatileTextOnUI(loggedInUser.getBalanceInformation());
        addAllTransactions();
        changeSubMenu(dashboardGrid);
    }
    public void handleNotifications() {
        addAllNotifications();
        changeSubMenu(notificationsGrid);
    }
    public void handleDeposit() { changeSubMenu(depositGrid); }
    public void handleWithdraw() { changeSubMenu(withdrawGrid) ;}
    public void handleTransfer() { changeSubMenu(transferGrid); }
    public void handlePrivacyPolicy() { changeSubMenu(privacyPolicyGrid); }
    public void handleLogout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait();
        if(alert.getResult().getText().equals("Yes"))
            App.changeScene(logout, "loginScreen");
    }

    /*

    */
    public void handleDepositRequest() {
        double sumToDeposit;
        try { sumToDeposit = Double.parseDouble(amountDeposit.getText()); }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid amount");
            alert.showAndWait();
            return;
        }
        sumToDeposit = ((double) ((int) (sumToDeposit * 100)) / 100);
        if(!Database.deposit(loggedInUser, sumToDeposit, currencyDeposit.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error uploading deposit data to database");
            alert.showAndWait();
        }
        amountDeposit.setText("");
    }

    /*

    */
    public void handleWithdrawRequest() {
        double sumToWithdraw;
        try { sumToWithdraw = Double.parseDouble(amountWithdraw.getText()); }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid amount");
            alert.showAndWait();
            return;
        }
        sumToWithdraw = ((double) ((int) (sumToWithdraw * 100)) / 100);
        // check if funds are sufficient to effectuate a withdraw
        switch (currencyWithdraw.getValue()) {
            case "EUR":
                if(loggedInUser.getBalanceInformation().getAvailableEUR() < sumToWithdraw) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Insufficient funds");
                    alert.showAndWait();
                    return;
                }
                break;
            case "USD":
                if(loggedInUser.getBalanceInformation().getAvailableUSD() < sumToWithdraw) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Insufficient funds");
                    alert.showAndWait();
                    return;
                }
                break;
            case "RON":
                if(loggedInUser.getBalanceInformation().getAvailableRON() < sumToWithdraw) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Insufficient funds");
                    alert.showAndWait();
                    return;
                }
                break;
        }

        if(!Database.withdraw(loggedInUser, sumToWithdraw, currencyWithdraw.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error uploading withdraw data to database");
            alert.showAndWait();
        }

        amountWithdraw.setText("");
    }

    /*

    */
    public void handleTransferRequest() {
        String errors = "";
        // Find user
        ArrayList<User.PersonalInformation> pInfo = Database.getAllPersonalInformation();
        boolean userFound = false;
        int index = 0;
        if(pInfo != null)
            for(User.PersonalInformation i : pInfo)
                if(i.getPin().equals(pinReceiverTransfer.getText()) && i.getFullName().equals(nameReceiverTransfer.getText())) {
                    userFound = true;
                    break;
                }
                else
                    index ++;

        if(!userFound)
            errors += "User not found1\n";
        if(pinReceiverTransfer.getText().equals(loggedInUser.getPersonalInformation().getPin()))
            errors += "Cannot send money to yourself\n";
        if(pinReceiverTransfer.getText().length() != 13)
            errors += "Invalid PIN\n";
        if(!sendTransfer.isSelected() && !requestTransfer.isSelected())
            errors += "Type of transaction must be selected\n";

        // Throw errors if they exist
        if(!errors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(errors);
            alert.showAndWait();
            return;
        }

        // Check if personal information matches account information
        ArrayList<User.AccountInformation> aInfo = Database.getAllAccountInformation();
        if(aInfo != null)
            if(!aInfo.get(index).getCardNumber().equals(cardnumReceiverTransfer.getText()) ||
               !aInfo.get(index).getEmail().equals(emailReceiverTransfer.getText()))
                errors += "User not found2\n";

        if(!errors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(errors);
            alert.showAndWait();
            return;
        }

        // Check if amount to transfer is valid data
        double amountToTransfer;
        try { amountToTransfer = Double.parseDouble(amountTransfer.getText()); }
        catch (NumberFormatException e) {
            errors += "Invalid amount\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(errors);
            alert.showAndWait();
            return;
        }
        amountToTransfer = ((double) ((int) (amountToTransfer * 100)) / 100);

        // Check if user has enough available funds to make a send transfer
        if(sendTransfer.isSelected())
            switch (currencyTransfer.getValue()) {
                case "EUR":
                    if(amountToTransfer > loggedInUser.getBalanceInformation().getAvailableEUR())
                        errors += "Not enough funds\n";
                    break;
                case "USD":
                    if(amountToTransfer > loggedInUser.getBalanceInformation().getAvailableUSD())
                        errors += "Not enough funds\n";
                    break;
                case "RON":
                    if(amountToTransfer > loggedInUser.getBalanceInformation().getAvailableRON())
                        errors += "Not enough funds\n";
                    break;
            }

        // Throw error if user doesn't have enough funds
        if(!errors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(errors);
            alert.showAndWait();
            return;
        }

        NotificationModel notificationModel = new NotificationModel(pinReceiverTransfer.getText(),
                loggedInUser.getPersonalInformation().getFullName(), loggedInUser.getAccountInformation().getCardNumber(),
                nameReceiverTransfer.getText(), cardnumReceiverTransfer.getText(), requestTransfer.isSelected() ?
                "Request" : "Send", currencyTransfer.getValue(), "Unresolved", amountToTransfer);

        if(!Database.sendNotification(notificationModel, loggedInUser.getBalanceInformation()))
            System.out.println("Error sending notification to database");
        else {
            amountTransfer.setText("");
            nameReceiverTransfer.setText("");
            pinReceiverTransfer.setText("");
            emailReceiverTransfer.setText("");
            cardnumReceiverTransfer.setText("");
            requestTransfer.setSelected(false);
            sendTransfer.setSelected(false);
        }
    }

    public void setCellFactories() {
        // transactions table
        transactionType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        transactionEmitor.setCellValueFactory(new PropertyValueFactory<>("SenderName"));
        transactionReceiver.setCellValueFactory(new PropertyValueFactory<>("ReceiverName"));
        transactionCurrency.setCellValueFactory(new PropertyValueFactory<>("Currency"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        transactionAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        transactionsTableView.setItems(transactionModels);
        // notifications table
        notificationSenderName.setCellValueFactory(new PropertyValueFactory<>("SenderName"));
        notificationType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        notificationCurrency.setCellValueFactory(new PropertyValueFactory<>("Currency"));
        notificationStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        notificationAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        notificationAction.setCellValueFactory(new PropertyValueFactory<>("Hbox"));
        notificationsTableView.setItems(notificationModels);
    }

    public void addAllTransactions() {
        transactionModels.clear();
        ArrayList<TransactionModel> DBtransactionModels = Database.getAllTransactions(loggedInUser);
        if(DBtransactionModels != null)
            transactionModels.addAll(DBtransactionModels);
    }

    public void addAllNotifications() {
        notificationModels.clear();
        ArrayList<NotificationModel> DBnotificationModels = Database.getAllNotifications(loggedInUser);
        if(DBnotificationModels != null)
            notificationModels.addAll(DBnotificationModels);
    }

    public void addListeners() {
        amountDeposit.textProperty().addListener((observable, oldValue, newValue) -> {
            if(amountDeposit.getText().length() > 20)
                amountDeposit.setText(oldValue);
            else if(!newValue.matches("\\d."))
                amountDeposit.setText(newValue.replaceAll("[^\\d.]", ""));
        });

        amountWithdraw.textProperty().addListener((observable, oldValue, newValue) -> {
            if(amountWithdraw.getText().length() > 20)
                amountWithdraw.setText(oldValue);
            else if(!newValue.matches("\\d."))
                amountWithdraw.setText(newValue.replaceAll("[^\\d.]", ""));
        });

        amountTransfer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(amountTransfer.getText().length() > 20)
                amountTransfer.setText(oldValue);
            else if(!newValue.matches("\\d."))
                amountTransfer.setText(newValue.replaceAll("[^\\d.]", ""));
        });

        nameReceiverTransfer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(nameReceiverTransfer.getText().length() > 40)
                nameReceiverTransfer.setText(oldValue);
            else if(!newValue.matches("-\\sa-zA-Z*"))
                nameReceiverTransfer.setText(newValue.replaceAll("[^-\\sa-zA-Z]", ""));
        });

        pinReceiverTransfer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(pinReceiverTransfer.getText().length() > 13)
                pinReceiverTransfer.setText(oldValue);
            else if(!newValue.matches("\\d"))
                pinReceiverTransfer.setText(newValue.replaceAll("[^\\d]", ""));
        });

        emailReceiverTransfer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(emailReceiverTransfer.getText().length() > 40)
                emailReceiverTransfer.setText(oldValue);
            else if(!newValue.matches("\\sa-zA-Z\\d\\W!#\\$%&\\(\\)*+,-./\\^:;<=>?@~"))
                emailReceiverTransfer.setText(newValue.replaceAll("[^a-zA-Z\\d!#$%^&()*+,-./:;<=>?@~]", ""));
        });

        cardNumberTransfer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(cardNumberTransfer.getText().length() > 16)
                cardNumberTransfer.setText(oldValue);
            else if(!newValue.matches("\\d"))
                cardNumberTransfer.setText(newValue.replaceAll("[^\\d]", ""));
        });

        cardnumReceiverTransfer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(cardnumReceiverTransfer.getText().length() > 16)
                cardnumReceiverTransfer.setText(oldValue);
            else if(!newValue.matches("\\d"))
                cardnumReceiverTransfer.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }

    /*
        TODO
    */
    public static User getLoggedInUser() { return loggedInUser; }

    public void setVolatileTextOnUI(User.BalanceInformation balanceInformation) {
        eurAmountProfile.setText(balanceInformation.getAvailableEUR() + " €");
        totalTransfersEUR.setText("Total transfers: " + (int) balanceInformation.getTotalTransfersEUR());
        amountAwaitingTransferEUR.setText(balanceInformation.getAwaitingEUR() + " €");
        totalWithdrawsEUR.setText("Total withdraws: " + (int) balanceInformation.getTotalWithdrawsEUR());
        amountTotalWithdrawnEUR.setText(balanceInformation.getTotalWithdrawnEUR() + " €");
        totalDepositsEUR.setText("Total deposits: " + (int) balanceInformation.getTotalDepositsEUR());
        amountTotalDepositedEUR.setText(balanceInformation.getTotalDepositedEUR() + " €");

        usdAmountProfile.setText(balanceInformation.getAvailableUSD() + " $");
        totalTransfersUSD.setText("Total transfers: " + (int) balanceInformation.getTotalTransfersUSD());
        amountAwaitingTransferUSD.setText(balanceInformation.getAwaitingUSD() + " $");
        totalWithdrawsUSD.setText("Total withdraws: " + (int)  balanceInformation.getTotalWithdrawsUSD());
        amountTotalWithdrawnUSD.setText(balanceInformation.getTotalWithdrawnUSD() + " $");
        totalDepositsUSD.setText("Total deposits: " + (int) balanceInformation.getTotalDepositsUSD());
        amountTotalDepositedUSD.setText(balanceInformation.getTotalDepositedUSD() + " $");

        ronAmountProfile.setText(balanceInformation.getAvailableRON() + " L");
        totalTransfersRON.setText("Total transfers: " + (int) balanceInformation.getTotalTransfersRON());
        amountAwaitingTransferRON.setText(balanceInformation.getAwaitingRON() + " L");
        totalWithdrawsRON.setText("Total withdraws: " + (int) balanceInformation.getTotalWithdrawsRON());
        amountTotalWithdrawnRON.setText(balanceInformation.getTotalWithdrawnRON() + " L");
        totalDepositsRON.setText("Total deposits: " + (int) balanceInformation.getTotalDepositsRON());
        amountTotalDepositedRON.setText(balanceInformation.getTotalDepositedRON() + " L");
    }

    public void setStaticTextOnUI() {
        nameDeposit.setText(loggedInUser.getPersonalInformation().getFullName());
        nameWithdraw.setText(loggedInUser.getPersonalInformation().getFullName());
        nameTransfer.setText(loggedInUser.getPersonalInformation().getFullName());
        pinDeposit.setText(loggedInUser.getPersonalInformation().getPin());
        pinWithdraw.setText(loggedInUser.getPersonalInformation().getPin());
        pinTransfer.setText(loggedInUser.getPersonalInformation().getPin());
        addressDeposit.setText(loggedInUser.getPersonalInformation().getAddress());
        addressWithdraw.setText(loggedInUser.getPersonalInformation().getAddress());
        emailTransfer.setText(loggedInUser.getAccountInformation().getEmail());
        cardNumberDeposit.setText(loggedInUser.getAccountInformation().getCardNumber());
        cardNumberWithdraw.setText(loggedInUser.getAccountInformation().getCardNumber());
        cardNumberTransfer.setText(loggedInUser.getAccountInformation().getCardNumber());
    }


    /*
        //TODO add comment
    */
    private void addAllGridsToArrayList() {
        grids.add(dashboardGrid);
        grids.add(notificationsGrid);
        grids.add(depositGrid);
        grids.add(withdrawGrid);
        grids.add(transferGrid);
        grids.add(privacyPolicyGrid);
    }

    /*
        //TODO add comment
    */
    private void changeSubMenu(GridPane grid) {
        for(GridPane i : grids)
            i.setVisible(false);
        grid.setVisible(true);
    }

}
