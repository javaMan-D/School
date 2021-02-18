package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import sample.Databases.DBHandler;
import sample.model.User;

public class addTaskController {
    public static int userId;
    private DBHandler dbHandler;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView addButton;

    @FXML
    private Label noTasklabel;

    @FXML
    private Label labelWelcome;

    @FXML
    void initialize() throws SQLException {
        dbHandler = new DBHandler();

        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            FadeTransition buttonFade = new FadeTransition(Duration.millis(1500), addButton);
            FadeTransition labelFade = new FadeTransition(Duration.millis(1500), noTasklabel);
            addButton.relocate(279,200);
            noTasklabel.relocate(210,138);

            addButton.setOpacity(0);

            buttonFade.setFromValue(1f);
            noTasklabel.setOpacity(0);
            buttonFade.setToValue(0f);
            buttonFade.setCycleCount(1);
            buttonFade.setAutoReverse(false);
            buttonFade.play();

            labelFade.setFromValue(1f);
            labelFade.setToValue(0f);
            labelFade.setCycleCount(1);
            labelFade.setAutoReverse(false);
            labelFade.play();

            try {
                AnchorPane formPane = FXMLLoader.load(getClass().getResource("/sample/view/addTaskForm.fxml"));

                addTaskController.userId = getUserId();

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500), formPane);
                fadeTransition.setFromValue(0f);
                fadeTransition.setToValue(1f);
                fadeTransition.setCycleCount(1);
                fadeTransition.setAutoReverse(false);
                fadeTransition.play();

                rootAnchorPane.getChildren().setAll(formPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setUserId(int userId) throws SQLException {
        addTaskController.userId = userId;
        System.out.println("user id is " + addTaskController.userId);
        ResultSet userRow = dbHandler.readUser(userId);
        while (userRow.next()){
            String name = userRow.getString("firstname");
            String gender = userRow.getString("gender");
            labelWelcome.setText((gender.equals("Male")) ? "Welcome, Mr. " + name +"!": "Welcome, Ms. " + name + "!");
        }
    }
    public int getUserId(){
        return userId;
    }
}
