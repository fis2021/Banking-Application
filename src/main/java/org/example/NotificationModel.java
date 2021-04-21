package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class NotificationModel {
    private SimpleStringProperty senderName, senderCardNumber, receiverName, receiverCardNumber, type, currency, status;
    private SimpleDoubleProperty amount;

    public NotificationModel(String senderName, String senderCardNumber, String receiverName, String receiverCardNumber,
                             String type, String currency, String status, double amount) {
        this.senderName = new SimpleStringProperty(senderName);
        this.senderCardNumber = new SimpleStringProperty(senderCardNumber);
        this.receiverName = new SimpleStringProperty(receiverName);
        this.receiverCardNumber = new SimpleStringProperty(receiverCardNumber);
        this.type = new SimpleStringProperty(currency);
        this.currency = new SimpleStringProperty(currency);
        this.status = new SimpleStringProperty(status);
        this.amount = new SimpleDoubleProperty(amount);
    }

    public void setSenderName(String senderName) { this.senderName.set(senderName); }

    public void setSenderCardNumber(String senderCardNumber) { this.senderCardNumber.set(senderCardNumber); }

    public void setReceiverName(String receiverName) { this.receiverName.set(receiverName); }

    public void setReceiverCardNumber(String receiverCardNumber) { this.receiverCardNumber.set(receiverCardNumber); }

    public void setType(String type) { this.type.set(type); }

    public void setCurrency(String currency) { this.currency.set(currency); }

    public void setStatus(String status) { this.status.set(status); }

    public void setAmount(double amount) { this.amount.set(amount); }

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
}