package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

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

    public void handleRegister() throws IOException {
        // check if data is entered correctly
        String errors = "";
        if(fname.getText().length() < 3)
            errors += "First name should be at least 3 characters long\n";
        if(lname.getText().length() < 3)
            errors += "Last name should be at least 3 characters long\n";
        if(pin.getText().length() != 13)
            errors += "PIN must be 13 characters long\n";
        if(birthdate.getValue() == null)
            errors += "Birthdate not selected\n";
        else if(Period.between(birthdate.getValue(), LocalDate.now()).getYears() < 18)
            errors += "User is underage\n";
        if(!male.isSelected() && !female.isSelected())
            errors += "Gender not selected\n";
        if(address.getText().length() < 10)
            errors += "Address field must be at least 10 characters long\n";
        if(zip.getText().length() < 5)
            errors += "ZIP must be at least 5 characters long\n";
        if(cardnumber.getText().length() != 16)
            errors += "Card Number must be 16 characters long\n";
        if(!Database.checkIfEmailValid(email.getText()))
            errors += "Email address is not valid\n";

        if(username.getText().length() < 5)
            errors += "Length of username must be at least 5 characters\n";
        char[] usernameCheck = username.getText().toCharArray();
        boolean letterFound = false, numberFound = false, specCharFound = false;
        for(char i : usernameCheck)
            if(Character.isLetter(i))
                letterFound = true;
            else if(Character.isDigit(i))
                numberFound = true;
            else if("!#$%^&()*+,-./:;<=>?@~".contains(i + ""))
                specCharFound = true;
        if(!letterFound)
            errors += "Username must contain at least a letter\n";
        if(!numberFound)
            errors += "Username must contain at least a number\n";
        if(!specCharFound)
            errors += "Username must contain at least a special character\n";

        if(password.getText().length() < 10)
            errors += "Length of password must be at least 10 characters\n";
        char[] passwordCheck = password.getText().toCharArray();
        letterFound = false; numberFound = false; specCharFound = false;
        for(char i : passwordCheck)
            if(Character.isLetter(i))
                letterFound = true;
            else if(Character.isDigit(i))
                numberFound = true;
            else if("!#$%^&()*+,-./:;<=>?@~".contains(i + ""))
                specCharFound = true;
        if(!letterFound)
            errors += "Password must contain at least a letter\n";
        if(!numberFound)
            errors += "Password must contain at least a number\n";
        if(!specCharFound)
            errors += "Password must contain at least a special character\n";

        if(!password.getText().equals(repassword.getText()))
            errors += "Passwords do not match\n";

        if(errors.isEmpty()) {
            User.PersonalInformation personalInformation = new User.PersonalInformation(fname.getText(), lname.getText(),
                    pin.getText(), birthdate.getValue().toString(), male.isSelected() ? "M" : "F", address.getText(),
                    zip.getText());
            User.AccountInformation accountInformation = new User.AccountInformation(cardnumber.getText(), email.getText(),
                    username.getText(), Database.encodePasswordMD5(password.getText()));

            Alert alert;
            if(Database.registerUser(personalInformation, accountInformation)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Registration successful");
                alert.showAndWait();
                handleBack();
            }
            else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("User already exists in the database");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Data fields entered incorrectly");
            errors = errors.substring(0, errors.length() - 1);
            alert.setContentText(errors);
            alert.showAndWait();
        }

    }

    public void handleBack() throws IOException { App.changeScene(back, "loginScreen"); }
}
