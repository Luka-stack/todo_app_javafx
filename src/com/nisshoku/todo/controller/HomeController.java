package com.nisshoku.todo.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.nisshoku.todo.database.DatabaseHandler;
import com.nisshoku.todo.model.Task;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private StackPane parentStackPane;

    @FXML
    private VBox tasksPane;

    @FXML
    private JFXTextField nameTxt;

    @FXML
    private JFXTextArea descTxt;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton logoutBtn;

    @FXML
    private Label infoLbl;

    @FXML
    private ImageView refreshImg;

    private DatabaseHandler dbConn = null;
    private int userId = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { dbConn = new DatabaseHandler();}

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == logoutBtn) {

            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/com/nisshoku/todo/view/login.fxml")));
                stage.setScene(newScene);
                stage.show();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        else if (event.getSource() == addBtn) {
            if (createTask()) {
                loadTasks();
            }
        }
        else if (event.getSource() == refreshImg) {
            loadTasks();
        }
    }

    public void setController(int userId) {
        this.userId = userId;
        loadTasks();
    }

    private boolean createTask() {

        boolean success = true;
        String name = nameTxt.getText().trim();
        String description = descTxt.getText().trim();

        if (name.isEmpty() || description.isEmpty()) {
            setInfoLabel(Color.CRIMSON, "Empty Credentials");
            success = false;
        }
        else {
            Task task = new Task(name, description);
            int result = dbConn.createTask(task, userId);

            if (result == -1) {
                setInfoLabel(Color.CRIMSON, "Task: " + name + " already exists!");
                success = false;
            }
            else if (result == 1) {
                setInfoLabel(Color.GREEN,"Task has been Created");
            }
            else {
                setInfoLabel(Color.CRIMSON,"Something went wrong!");
                success = false;
            }
        }

        return success;
    }

    private void loadTasks() {

        ResultSet resultSet = dbConn.getTasks(userId);
        tasksPane.getChildren().clear();

        while (true) {
            try {
                if (resultSet.next()) {
                    try {

                        String task = resultSet.getString("task");
                        String desc = resultSet.getString("description");
                        String date = resultSet.getString("dateCreated").substring(5);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nisshoku/todo/view/taskThumb.fxml"));
                        Node node = loader.load();
                        ((TaskThumbController) loader.getController()).setTask(parentStackPane, infoLbl, task, desc, date);
                        tasksPane.getChildren().add(node);
                    }
                    catch (IOException e) { e.getStackTrace(); }
                }
                else { break; }
            }
            catch (SQLException e) { e.getStackTrace(); }
        }
    }

    private void setInfoLabel(Color color, String text) {
        infoLbl.setText(text);
        infoLbl.setTextFill(color);
    }
}
