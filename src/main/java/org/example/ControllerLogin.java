package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControllerLogin {

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

    public void handleRegister() throws IOException { App.changeScene(register, "registerScreen"); }

}
