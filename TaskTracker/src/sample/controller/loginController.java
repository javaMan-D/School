package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import sample.Databases.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.animations.Error;
import sample.model.User;

public class loginController {
    private int userId;
    private DBHandler dbHandler;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public TextField loginUsername;

    @FXML
    public PasswordField loginPassword;

    @FXML
    private Button createNewAccountButton;

    @FXML
    private Button loginButton;

    @FXML
    void initialize() {
        dbHandler = new DBHandler();

        loginButton.setOnAction(event -> {
            login();
        });
        createNewAccountButton.setOnAction(event -> {
            createNewAccountButton.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/view/newAccount.fxml"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image("sample/assets/credit-account-red.png"));
            stage.setTitle("TaskTracker");
            stage.show();
        });
    }
    private void showAddTask() throws SQLException {
        createNewAccountButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/view/addTask.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getIcons().add(new Image("sample/assets/credit-account-red.png"));
        stage.setTitle("Add Task");

        addTaskController addTaskController = fxmlLoader.getController();
        addTaskController.setUserId(userId);
        stage.showAndWait();
    }
    public void login(){
        loginButton.setOnAction(event -> {
            String strUser = loginUsername.getText();
            String strUserPass = loginPassword.getText();

            User user = new User();
            user.setUserName(strUser);
            user.setPassword(strUserPass);
            ResultSet resultSet = dbHandler.readUser(user);

            int counter = 0;
            try {
                while (resultSet.next()) {
                    counter++;
                    userId = resultSet.getInt("userid");

                }
                if (counter == 1) {
                    showAddTask();
                } else {
                    Error userError = new Error(loginUsername);
                    Error passError = new Error(loginPassword);
                    userError.err();
                    passError.err();

                }
            }catch (SQLException throwables){
                throwables.printStackTrace();
            }

        });
    }

}
