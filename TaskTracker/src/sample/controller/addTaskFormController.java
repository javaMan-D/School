package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Databases.DBHandler;
import sample.model.Task;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class addTaskFormController {
    private DBHandler dbHandler;
    private int userID;

    @FXML
    private AnchorPane formPane;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField taskField;

    @FXML
    private JFXTextField descriptionField;

    @FXML
    private JFXButton addTaskButton;

    @FXML
    private JFXTextField urgencyField;

    @FXML
    private JFXButton counterButton;

    public addTaskFormController() {
    }

    @FXML
    void initialize() {

        dbHandler = new DBHandler();
        Task task = new Task();
        addTaskButton.setOnAction(event -> {

            Calendar calendar = Calendar.getInstance();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

            String taskText = taskField.getText().trim();
            String taskDescription = descriptionField.getText().trim();
            String taskUrgency = urgencyField.getText().trim();

            if (!taskText.equals("") && !taskDescription.equals("") && !taskUrgency.equals("")) {
                task.setUserId(addTaskController.userId);
                task.setDateModified(timestamp);
                task.setTask(taskText);
                task.setDescription(taskDescription);
                task.setUrgency(taskUrgency);
                dbHandler.addTask(task);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Tasks Added Successfully! Add another one!");
                alert.setHeaderText(null);
                alert.showAndWait();

                counterButton.setVisible(true);
                int taskCount = 0;
                try {
                     taskCount= dbHandler.getTasks(addTaskController.userId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                counterButton.setText("My Tasks: " + taskCount);
                taskField.setText("");
                descriptionField.setText("");
                urgencyField.setText("");
                counterButton.setOnAction(event1 -> {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/sample/view/taskList.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Tasks");
                    stage.getIcons().add(new Image("/sample/assets/credit-account-red.png"));
                    stage.setResizable(false);
                    stage.showAndWait();
                });

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Fields");
                alert.setContentText("Please Complete Information");
                alert.setHeaderText(null);
                alert.setResizable(false);
                alert.showAndWait();
            }
        });
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
