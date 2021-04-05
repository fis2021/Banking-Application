package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.*;

import java.io.IOException;

public class ControllerRegister {
    @FXML
    private TextField fname, lname, pin, address, zip, cardnumber, email, username;
    @FXML
    private PasswordField password, repassword;
    @FXML
    private RadioButton male, female;
    @FXML
    private DatePicker birthdate;
    @FXML
    private Button register, back;

    public void initialize() { addListeners(); }

    /*
        Adds listeners to the text fields to restrict input accordingly
            first name: restrict to only letters and spaces, max 40 characters
            last name: restrict to only letters and spaces, max 40 characters
            pin: restrict to only numbers, max 13 characters
            address: max 80 characters
            zip: restrict to only numbers, max 10 characters
            card number: restrict to only numbers, max 16 characters
            email: restricted to only letters, numbers and special characters, max 40 characters
            username: restricted to only letters, numbers and special characters, max 30 characters
            password: restricted to only letters, numbers and special characters, max 30 characters
            repassword: restricted to only letters, numbers and special characters, max 30 characters
    */
    public void addListeners() {
        fname.textProperty().addListener((observable, oldValue, newValue) -> {
            if(fname.getText().length() > 40)
                fname.setText(oldValue);
            else if(!newValue.matches("[a-zA-Z\\-]"))
                fname.setText(newValue.replaceAll("[^a-zA-Z\\-]", ""));
         });

        lname.textProperty().addListener((observable, oldValue, newValue) -> {
            if(lname.getText().length() > 40)
                lname.setText(oldValue);
            else if(!newValue.matches("[a-zA-Z\\-]"))
                lname.setText(newValue.replaceAll("[^a-zA-Z\\-]", ""));
        });

        pin.textProperty().addListener((observable, oldValue, newValue) -> {
            if(pin.getText().length() > 13)
                pin.setText(oldValue);
            else if(!newValue.matches("\\d"))
                pin.setText(newValue.replaceAll("[^\\d]", ""));
        });

        address.textProperty().addListener((observable, oldValue, newValue) -> {
            if(address.getText().length() > 80)
                address.setText(oldValue);
        });

        zip.textProperty().addListener((observable, oldValue, newValue) -> {
            if(zip.getText().length() > 10)
                zip.setText(oldValue);
            else if(!newValue.matches("\\d"))
                zip.setText(newValue.replaceAll("[^\\d]", ""));
        });

        cardnumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if(cardnumber.getText().length() > 16)
                cardnumber.setText(oldValue);
            else if(!newValue.matches("\\d"))
                cardnumber.setText(newValue.replaceAll("[^\\d]", ""));
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if(email.getText().length() > 40)
                email.setText(oldValue);
            else if(!newValue.matches("\\sa-zA-Z\\d\\W!#\\$%&\\(\\)*+,-./\\^:;<=>?@~"))
                email.setText(newValue.replaceAll("[^a-zA-Z\\d!#$%^&()*+,-./:;<=>?@~]", ""));
        });

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

        repassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if(repassword.getText().length() > 30)
                repassword.setText(oldValue);
            else if(!newValue.matches("\\sa-zA-Z\\d\\W!#\\$%&\\(\\)*+,-./\\^:;<=>?@~"))
                repassword.setText(newValue.replaceAll("[^a-zA-Z\\d!#$%^&()*+,-./:;<=>?@~]", ""));
        });
    }

    public Alert verifyData() {
        //TODO
        return null;
    }

    public void handleRegister() {
        //TODO
    }

    public void handleBack() throws IOException { App.changeScene(back, "loginScreen"); }


}
