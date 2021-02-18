package sample.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Databases.DBHandler;
import javafx.fxml.FXML;
import sample.animations.Error;
import sample.model.User;
import sample.model.UserDao;

/**
 * @author Mandy Llagas
 */

public class newAccountController {
    @FXML
    private AnchorPane logPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private TextField newAccountFirstName;

    @FXML
    private TextField newAccountLastName;

    @FXML
    private TextField newAccountUsername;

    @FXML
    private PasswordField newAccountPassword;

    @FXML
    private Button signupButton;
    @FXML
    private RadioButton radioMale;

    @FXML
    private RadioButton radioFemale;

    @FXML
    private JFXButton loginButton;

    @FXML
    void initialize() {

        signupButton.setOnAction(event -> {

            if (newAccountFirstName.getText().isEmpty())
                errorShake(newAccountFirstName);
            else if (newAccountLastName.getText().isEmpty())
                errorShake(newAccountLastName);
            else if (newAccountUsername.getText().isEmpty())
                errorShake(newAccountUsername);
            else if (newAccountPassword.getText().isEmpty())
                errorShake(newAccountPassword);
            else if (!radioMale.isSelected() && !radioFemale.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Fields");
                alert.setContentText("Please select Gender");
                alert.setHeaderText(null);
                alert.setResizable(false);
                alert.showAndWait();
            } else {
                UserDao userDao = new UserDao();
                try {
                    if (!userDao.userExists(newAccountUsername.getText())) {
                        createUser();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setContentText("Account created successfully!");
                        alert.setHeaderText(null);
                        alert.showAndWait();

                        newAccountFirstName.setText("");
                        newAccountLastName.setText("");
                        newAccountUsername.setText("");
                        newAccountPassword.setText("");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incomplete Fields");
                        alert.setContentText("Username Already Exists");
                        alert.setHeaderText(null);
                        alert.setResizable(false);
                        alert.showAndWait();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        loginButton.setOnAction(event -> {
            //loginButton.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                AnchorPane formPane = FXMLLoader.load(getClass().getResource("/sample/view/log-in.fxml"));
                logPane.getChildren().setAll(formPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void errorShake(TextField textField) {
        Error error = new Error(textField);
        error.err();
    }

    private void errorShake(PasswordField passwordField) {
        Error error = new Error(passwordField);
        error.err();
    }

    private void createUser() {
        DBHandler dbHandler = new DBHandler();
        String firstName = newAccountFirstName.getText();
        String lastName = newAccountLastName.getText();
        String userName = newAccountUsername.getText();
        String password = newAccountPassword.getText();
        String gender;

        if (radioMale.isSelected()) {
            gender = "Male";

        } else{
            gender = "Female";
        }

        User user = new User(firstName, lastName, userName, password, gender);
        try {
            dbHandler.newAccount(user);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

}
