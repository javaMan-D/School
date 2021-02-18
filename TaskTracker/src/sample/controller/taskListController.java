package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Databases.DBHandler;
import sample.model.Task;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;


public class taskListController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXListView<Task> tasksList;

    @FXML
    private JFXTextField listTaskField;

    @FXML
    private JFXTextField listDescriptionField;

    @FXML
    private JFXTextField listUrgencyField;

    @FXML
    private JFXButton listAddTaskButton;

    private ObservableList<Task> tasks;
    private ObservableList<Task> refreshTasks;

    @FXML
    private ImageView listRefreshImg;

    @FXML
    private JFXButton listLogOutButton;

    @FXML
    private Label taskOwner;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        tasks = FXCollections.observableArrayList();

        DBHandler dbHandler = new DBHandler();
        ResultSet resultSet = dbHandler.getTasksByUser(addTaskController.userId);
        ResultSet userRow = dbHandler.readUser(addTaskController.userId);
        while (userRow.next()){
            String name = userRow.getString("firstname");
            String gender = userRow.getString("gender");
            taskOwner.setText((gender.equals("Male"))? "Mr. " + name + "'s Tasks" : "Ms. " + name + "'s Tasks");
        }

        while (resultSet.next()){
            Task task = new Task();
            task.setTaskId(resultSet.getInt("tasksid"));
            task.setTask(resultSet.getString("task"));
            task.setDescription(resultSet.getString("description"));
            task.setUrgency(resultSet.getString("urgency"));
            task.setDateModified(resultSet.getTimestamp("datemodified"));
            tasks.addAll(task);
        }

        tasksList.setItems(tasks);
        tasksList.setCellFactory(cellController -> new cellController());

        listRefreshImg.setOnMouseClicked(event -> {
            try {
                refreshList();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        listAddTaskButton.setOnAction(event -> {
            addNewTask();
        });

    }
    public void refreshList() throws SQLException, ClassNotFoundException {

        refreshTasks =FXCollections.observableArrayList();

        DBHandler dbHandler = new DBHandler();
        ResultSet resultSet =dbHandler.getTasksByUser(addTaskController.userId);

        while (resultSet.next()){
            Task task = new Task();
            task.setTaskId(resultSet.getInt("tasksid"));
            task.setTask(resultSet.getString("task"));
            task.setDateModified(resultSet.getTimestamp("datemodified"));
            task.setDescription(resultSet.getString("description"));
            task.setUrgency(resultSet.getString("urgency"));

            refreshTasks.addAll(task);
        }

        tasksList.setItems(refreshTasks);
        tasksList.setCellFactory(cellController -> new cellController());
    }
    public void addNewTask(){
        DBHandler dbHandler = new DBHandler();
            if (!listTaskField.getText().isEmpty() || !listDescriptionField.getText().isEmpty() ||
                    !listUrgencyField.getText().isEmpty()){
                Task task = new Task();
                Calendar calendar = Calendar.getInstance();
                java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

                task.setUserId(addTaskController.userId);
                task.setTask(listTaskField.getText().trim());
                task.setDescription(listDescriptionField.getText().trim());
                task.setUrgency(listUrgencyField.getText().trim());
                task.setDateModified(timestamp);
                dbHandler.addTask(task);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Tasks Added Successfully! Add another one!");
                alert.setHeaderText(null);
                alert.setResizable(false);
                alert.showAndWait();

                listTaskField.setText("");
                listDescriptionField.setText("");
                listUrgencyField.setText("");

                try {
                    initialize();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
    }

}

