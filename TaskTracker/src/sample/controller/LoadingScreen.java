package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoadingScreen {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane awd;

    @FXML
    void initialize() {
        new LoadScreen().start();
    }
    class LoadScreen extends Thread{

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/sample/view/log-in.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(root, 600,400);
                        Stage stage = new Stage();
                        stage.getIcons().add(new Image("sample/assets/document-icon.png"));
                        stage.setScene(scene);
                        stage.setTitle("Task Tracker");
                        stage.setResizable(false);
                        stage.show();
                        awd.getScene().getWindow().hide();
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
