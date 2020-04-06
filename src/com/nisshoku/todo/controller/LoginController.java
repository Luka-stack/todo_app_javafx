package com.nisshoku.todo.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.nisshoku.todo.database.DatabaseHandler;
import com.nisshoku.todo.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXTextField usernameTxt;

    @FXML
    private JFXPasswordField passwordTxt;

    @FXML
    private JFXButton loginBtn;

    @FXML
    private JFXButton signUpBtn;

    @FXML
    private Label infoLabel;

    private DatabaseHandler dbCon = null;

    private int userId = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbCon = new DatabaseHandler();
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == signUpBtn) {

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/nisshoku/todo/view/signup.fxml"));
                Parent root = null;
                root = loader.load();
                Scene scene = signUpBtn.getScene();

                root.translateYProperty().set(scene.getHeight());

                parentContainer.getChildren().remove(mainPane);
                parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_OUT);
                KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(timelineEvent -> {
                    parentContainer.getChildren().remove(mainPane);
                });
                timeline.play();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
        else if (event.getSource() == loginBtn) {
            if (logIn()) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/com/nisshoku/todo/view/home.fxml"));

                    Scene newScene = new Scene(loader.load());
                    stage.setScene(newScene);

                    ((HomeController)loader.getController()).setController(userId);

                    stage.show();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void setInfoLabel(Color color, String text) {
        infoLabel.setText(text);
        infoLabel.setTextFill(color);
    }

    private boolean logIn() {

        boolean success = true;
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setInfoLabel(Color.CRIMSON, "Empty Credentials");
            success = false;
        }
        else {
            User user = new User(username, password);
            ResultSet resultSet = dbCon.getUser(user);

            try {
                if (!resultSet.next()) {
                    setInfoLabel(Color.CRIMSON, "Incorrect Username or Password");
                    success = false;
                }
                else {
                    userId = (int) resultSet.getObject("userId");
                    setInfoLabel(Color.GREEN, "Login Successful... Redirecting...");
                }
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }

        return success;
    }

}
