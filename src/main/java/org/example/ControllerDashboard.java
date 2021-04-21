package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ControllerDashboard {
    @FXML
    GridPane dashboardGrid, notificationsGrid, depositGrid, withdrawGrid, transferGrid, privacyPolicyGrid;
    @FXML
    TextField amountDeposit, amountWithdraw, amountTransfer;
    private final ArrayList<GridPane> grids = new ArrayList<>();

    @FXML
    private Button logout;

    /*
        //TODO add comment
    */
    public void initialize() {
        addAllGridsToArrayList();
        addListeners();
    }

    /*
        //TODO add comment
    */
    public void handleDashboard() { changeSubMenu(dashboardGrid); }
    public void handleNotifications() throws SQLException {
        changeSubMenu(notificationsGrid);
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:src\\main\\resources\\database.db");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
