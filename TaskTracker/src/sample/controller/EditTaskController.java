package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTaskController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField editTaskField;

    @FXML
    private JFXTextField editDescriptionField;

    @FXML
    private JFXTextField editUrgencyField;

    @FXML
    public JFXButton editTaskButton;

    @FXML
    void initialize() {

    }
    public void setTaskField(String task) {
        this.editTaskField.setText(task);
    }

    public String getTask() {
        return this.editTaskField.getText().trim();
    }

    public void setUpdateDescriptionField(String description) {
        this.editDescriptionField.setText(description);
    }

    public String getDescription() {
        return this.editDescriptionField.getText().trim();
    }

    public void setUrgencyField(String urgency){
        this.editUrgencyField.setText(urgency);
    }
    public String getUrgency(){
        return this.editUrgencyField.getText().trim();
    }



}
