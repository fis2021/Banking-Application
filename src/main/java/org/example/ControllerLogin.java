package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ControllerLogin {


    private static User loggedInUser = null;
    @FXML
    private Button login, register;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void initialize() { addListeners(); }

    /*
        Adds listeners to the text fields to restrict input accordingly
            username: restricted to only letters, numbers and special characters, max 25 characters
            password: restricted to only letters, numbers and special characters, max 25 characters
    */
    public void addListeners() {
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if(username.getText().length() > 30)
                username.setText(oldValue);
            else if(!newValue.matches("\\sa-zA-Z\\d\\W!#\\$%&\\(\\)*+,-./\\^:;<=>?@~"))
                username.setText(newValue.replaceAll("[^a-zA-Z\\d!#$%^&()*+,-./:;<=>?@~]", ""));
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if(password.getText().length() > 30)
                password.setText(oldValue);
            else if(!newValue.matches("\\sa-zA-Z\\d\\W!#\\$%&\\(\\)*+,-./\\^:;<=>?@~"))
                password.setText(newValue.replaceAll("[^a-zA-Z\\d!#$%^&()*+,-./:;<=>?@~]", ""));
        });
    }

    public void handleLogin() throws IOException {
        if(username.getText().equals("admin") && password.getText().equals("admin")) {
            App.changeScene(login, "adminScreen");
            return;
        }

        String errors = "";

        if(username.getText().isEmpty())
            errors += "Username field cannot be empty\n";
        if(password.getText().isEmpty())
            errors += "Password field cannot be empty\n";

        if(!errors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(errors);
            alert.showAndWait();
        }
        else {
            ArrayList<User.AccountInformation> accountInformations = Database.getAllAccountInformation();
            if(accountInformations == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("No users have registered yet");
                alert.showAndWait();
            }
            else {
                boolean userFound = false;
                int pos = 0;
                for(User.AccountInformation i : accountInformations) {
                    if (i.getUsername().equals(username.getText()) &&
                            i.getPassword().equals(Database.encodePasswordMD5(password.getText()))) {
                        userFound = true;
                        break;
                    }
                    pos ++;
                }

                if(userFound) {
                    loggedInUser = new User(Objects.requireNonNull(Database.getAllPersonalInformation()).get(pos),
                            accountInformations.get(pos),
                            Objects.requireNonNull(Database.getAllBalanceInformation()).get(pos));
                    App.changeScene(login, "dashboardScreen");
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Username or password incorrect");
                    alert.showAndWait();
                }
            }
        }


    }

    public void handleRegister() throws IOException { App.changeScene(register, "registerScreen"); }

    public static User getLoggedInUser() { return loggedInUser; }
}
