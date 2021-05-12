package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

import java.util.Objects;


public class NotificationModel {
    private SimpleStringProperty ownerPIN, senderName, senderCardNumber, receiverName, receiverCardNumber, type,
            currency, status;
    private SimpleDoubleProperty amount;
    private HBox hbox;
    private Button accept, decline;

    public NotificationModel(String ownerPIN, String senderName, String senderCardNumber, String receiverName,
                             String receiverCardNumber, String type, String currency, String status, double amount) {
        this.ownerPIN = new SimpleStringProperty(ownerPIN);
        this.senderName = new SimpleStringProperty(senderName);
        this.senderCardNumber = new SimpleStringProperty(senderCardNumber);
        this.receiverName = new SimpleStringProperty(receiverName);
        this.receiverCardNumber = new SimpleStringProperty(receiverCardNumber);
        this.type = new SimpleStringProperty(type);
        this.currency = new SimpleStringProperty(currency);
        this.status = new SimpleStringProperty(status);
        this.amount = new SimpleDoubleProperty(amount);
        this.hbox = new HBox();
        this.accept = new Button("Accept");
        this.decline = new Button("Decline");
        accept.setStyle("-fx-background-color: rgb(55, 225, 70); -fx-text-fill: WHITE;");
        decline.setStyle("-fx-background-color: rgb(225, 55, 45); -fx-text-fill: WHITE;");

        accept.setOnAction(event -> {
            System.out.println("Accept clicked!");
            int index = 0;
            for(User.AccountInformation i : Objects.requireNonNull(Database.getAllAccountInformation()))
                if(i.getCardNumber().equals(senderCardNumber))
                    break;
                else
                    index ++;

            String senderPIN = Objects.requireNonNull(Database.getAllBalanceInformation()).get(index).getOwnerPIN();

            if(type.equals("Send")) {
                System.out.println("Accept clicked - SEND");
                if (!Database.sendAcceptTransfer(senderPIN, ControllerDashboard.getLoggedInUser().getPersonalInformation().getPin(),
                        amount, currency)) {
                    System.out.println("Error1");
                } else {
                    setResolved();
                    accept.setVisible(false);
                    decline.setVisible(false);
                    if(!Database.setNotificationResolved(this))
                        System.out.println("Error setting notification resolved");
                }
            }
            else {
                if (!Database.requestAcceptTransfer(senderPIN, ControllerDashboard.getLoggedInUser().getPersonalInformation().getPin(),
                        amount, currency)) {
                    System.out.println("Error2");
                } else {
                    setResolved();
                    accept.setVisible(false);
                    decline.setVisible(false);
                    if(!Database.setNotificationResolved(this))
                        System.out.println("Error setting notification resolved");
                }
            }
        });

        decline.setOnAction(event -> {
            System.out.println("Decline clicked!");
            int index = 0;
            for(User.AccountInformation i : Objects.requireNonNull(Database.getAllAccountInformation()))
                if(i.getCardNumber().equals(senderCardNumber))
                    break;
                else
                    index ++;

            String senderPIN = Objects.requireNonNull(Database.getAllBalanceInformation()).get(index).getOwnerPIN();

            if(type.equals("Send")) {
                   if(!Database.sendDeclineTransfer(senderPIN, ControllerDashboard.getLoggedInUser().getPersonalInformation().getPin(),
                           amount, currency)) {
                       System.out.println("Error3");
                   }
                   else {
                       setResolved();
                       accept.setVisible(false);
                       decline.setVisible(false);
                       if(!Database.setNotificationResolved(this))
                           System.out.println("Error setting notification resolved");
                   }
            }
            else {
                setResolved();
                accept.setVisible(false);
                decline.setVisible(false);
                if(!Database.setNotificationResolved(this))
                    System.out.println("Error setting notification resolved");
            }
        });

        hbox.setSpacing(5.0);
        hbox.setStyle("-fx-alignment: CENTER");
        hbox.getChildren().addAll(accept, decline);
    }

    public void setResolved() { this.status.set("Resolved"); }

    public void setOwnerPIN(String ownerPIN) { this.ownerPIN.set(ownerPIN); }

    public void setSenderName(String senderName) { this.senderName.set(senderName); }

    public void setSenderCardNumber(String senderCardNumber) { this.senderCardNumber.set(senderCardNumber); }

    public void setReceiverName(String receiverName) { this.receiverName.set(receiverName); }

    public void setReceiverCardNumber(String receiverCardNumber) { this.receiverCardNumber.set(receiverCardNumber); }

    public void setType(String type) { this.type.set(type); }

    public void setCurrency(String currency) { this.currency.set(currency); }

    public void setStatus(String status) { this.status.set(status); }

    public void setAmount(double amount) { this.amount.set(amount); }

    public void setHbox(HBox hbox) { this.hbox = hbox; }

    public void setAccept(Button accept) { this.accept = accept; }

    public void setDecline(Button decline) { this.decline = decline; }

    public String getOwnerPIN() { return ownerPIN.get(); }

    public SimpleStringProperty ownerPINProperty() { return ownerPIN; }

    public String getSenderName() { return senderName.get(); }

    public SimpleStringProperty senderNameProperty() { return senderName; }

    public String getSenderCardNumber() { return senderCardNumber.get(); }

    public SimpleStringProperty senderCardNumberProperty() { return senderCardNumber; }

    public String getReceiverName() { return receiverName.get(); }

    public SimpleStringProperty receiverNameProperty() { return receiverName; }

    public String getReceiverCardNumber() { return receiverCardNumber.get(); }

    public SimpleStringProperty receiverCardNumberProperty() { return receiverCardNumber; }

    public String getType() { return type.get(); }

    public SimpleStringProperty typeProperty() { return type; }

    public String getCurrency() { return currency.get(); }

    public SimpleStringProperty currencyProperty() { return currency; }

    public String getStatus() { return status.get(); }

    public SimpleStringProperty statusProperty() { return status; }

    public double getAmount() { return amount.get(); }

    public SimpleDoubleProperty amountProperty() { return amount; }

    public HBox getHbox() { return hbox; }

    public Button getAccept() { return accept; }

    public Button getDecline() { return decline; }
}