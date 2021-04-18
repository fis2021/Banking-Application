package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerDashboard {
    @FXML
    GridPane dashboardGrid, notificationsGrid, depositGrid, withdrawGrid, transferGrid, privacyPolicyGrid;

    private final ArrayList<GridPane> grids = new ArrayList<>();

    @FXML
    private Button logout;

    /*
        //TODO add comment
    */
    public void initialize() {
        addAllGridsToArrayList();
    }

    /*
        //TODO add comment
    */
    public void handleDashboard() { changeSubMenu(dashboardGrid); }
    public void handleNotifications() { changeSubMenu(notificationsGrid); }
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
