package org.example;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class TransactionModel {
    private SimpleStringProperty senderName, receiverName, type, currency, date;
    private SimpleDoubleProperty amount;

    public TransactionModel (String senderName, String receiverName, String type, String currency, String date,
                             double amount) {
        this.senderName = new SimpleStringProperty(senderName);
        this.receiverName = new SimpleStringProperty(receiverName);
        this.type = new SimpleStringProperty(type);
        this.currency = new SimpleStringProperty(currency);
        this.date = new SimpleStringProperty(date);
        this.amount = new SimpleDoubleProperty(amount);
    }

    public void setSenderName(String senderName) {
        this.senderName.set(senderName);
    }

    public void setReceiverName(String receiverName) {
        this.receiverName.set(receiverName);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getSenderName() {
        return senderName.get();
    }

    public SimpleStringProperty senderNameProperty() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName.get();
    }

    public SimpleStringProperty receiverNameProperty() {
        return receiverName;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getCurrency() {
        return currency.get();
    }

    public SimpleStringProperty currencyProperty() {
        return currency;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }
}