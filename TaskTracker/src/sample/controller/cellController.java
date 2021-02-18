package sample.controller;

import com.jfoenix.controls.JFXListCell;
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
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class cellController extends JFXListCell<Task> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView iconImg;

    @FXML
    private Label taskLbl;

    @FXML
    private Label descriptionLbl;

    @FXML
    private Label urgencyLbl;

    @FXML
    private Label dateCreatedLbl;

    @FXML
    private ImageView deleteImg;

    @FXML
    private ImageView taskEditButton;

    private FXMLLoader fxmlLoader;
    private DBHandler dbHandler;

    @FXML
    void initialize() {

    }

    @Override
    public void updateItem(Task task, boolean empty) {
        dbHandler = new DBHandler();
        super.updateItem(task, empty);

        if (empty || task == null){
            setText(null);
            setGraphic(null);
        }
        else {
            if (fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            taskLbl.setText(task.getTask());
            descriptionLbl.setText(task.getDescription());
            urgencyLbl.setText(task.getUrgency());
            dateCreatedLbl.setText(task.getDateModified().toString());

            int taskId = task.getTaskId();

            taskEditButton.setOnMouseClicked(event -> {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sample/view/editTask.fxml"));


                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Edit Task");
                stage.setResizable(false);
                stage.getIcons().add(new Image("sample/assets/icons8-edit-24.png"));
                stage.setScene(new Scene(root));

                EditTaskController editTaskController = loader.getController();
                editTaskController.setTaskField(task.getTask());
                editTaskController.setUpdateDescriptionField(task.getDescription());
                editTaskController.setUrgencyField(task.getUrgency());

                editTaskController.editTaskButton.setOnAction(event1 -> {
                    Calendar calendar = Calendar.getInstance();

                    java.sql.Timestamp timestamp =
                            new java.sql.Timestamp(calendar.getTimeInMillis());

                    try {
                        dbHandler.updateTask( editTaskController.getTask(), timestamp, editTaskController.getDescription(),
                                            editTaskController.getUrgency(), task.getTaskId());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Tasks edited!");
                    alert.setHeaderText(null);
                    alert.setResizable(false);
                    alert.showAndWait();
                });
                stage.show();
            });

            deleteImg.setOnMouseClicked(event ->{
                dbHandler = new DBHandler();
                try {
                    dbHandler.deleteTask(addTaskController.userId, taskId);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                getListView().getItems().remove(getItem());

            });

            setText(null);
            setGraphic(rootPane);

        }
    }
}
