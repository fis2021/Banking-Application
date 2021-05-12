package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerAdmin {
    @FXML
    private TableView<TransactionModel> transactionsTableView;
    @FXML
    private TableColumn<TransactionModel, String> transactionType, transactionEmitor, transactionReceiver,
            transactionCurrency, transactionDate;
    @FXML
    private TableColumn<TransactionModel, Double> transactionAmount;
    @FXML
    private Button exit;

    private final ObservableList<TransactionModel> transactionModels = FXCollections.observableArrayList();

    public void initialize() {
        transactionType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        transactionEmitor.setCellValueFactory(new PropertyValueFactory<>("SenderName"));
        transactionReceiver.setCellValueFactory(new PropertyValueFactory<>("ReceiverName"));
        transactionCurrency.setCellValueFactory(new PropertyValueFactory<>("Currency"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        transactionAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        transactionsTableView.setItems(transactionModels);

        ArrayList<TransactionModel> tModel = Database.getAllTransactions(null);
        if(tModel != null)
            for(TransactionModel i : tModel)
                transactionModels.add(0, i);
    }

    public void handleExit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure you want to exit?");
        alert.showAndWait();
        if(alert.getResult().getText().equals("Yes"))
            App.changeScene(exit, "loginScreen");
    }

}
