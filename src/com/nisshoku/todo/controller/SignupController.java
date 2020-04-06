package com.nisshoku.todo.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import com.nisshoku.todo.database.DatabaseHandler;
import com.nisshoku.todo.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXTextField firstNameTxt;

    @FXML
    private JFXTextField lastNameTxt;

    @FXML
    private JFXTextField usernameTxt;

    @FXML
    private JFXPasswordField passwordTxt;

    @FXML
    private JFXCheckBox femaleBox;

    @FXML
    private JFXCheckBox maleBox;

    @FXML
    private JFXButton signUpBtn;

    @FXML
    private JFXButton backBtn;

    @FXML
    private Label infoLabel;

    private DatabaseHandler dbCon = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { dbCon = new DatabaseHandler(); }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == signUpBtn) {
            if (createUser()) {
                backToLogin();
            }
        }
        else if (event.getSource() == backBtn) {
            backToLogin();
        }
    }

    private boolean createUser() {

        boolean success = true;
        String firstName = firstNameTxt.getText().trim();
        String lastName  = lastNameTxt.getText().trim();
        String username  = usernameTxt.getText().trim();
        String password  = passwordTxt.getText().trim();
        String gender = "";

        if (femaleBox.isSelected()) { gender = "Female"; }
        else if (maleBox.isSelected()){ gender = "Male"; }

        if (firstName.isEmpty() || lastName.isEmpty() ||
            username.isEmpty() || password.isEmpty() || gender.isEmpty())
        {
            setInfoLabel(Color.CRIMSON, "Empty Credentials");
            success = false;
        }
        else {
            User user = new User(firstName, lastName, username, password, gender);
            int result = dbCon.createUser(user);

            if (result == -1) {
                setInfoLabel(Color.CRIMSON, "Username " + username + " exists.");
                success = false;
            }
            else if (result == 1){
                setInfoLabel(Color.GREEN, "Account has been created... Redirecting...");
            }
            else {
                setInfoLabel(Color.CRIMSON, "Something went wrong!");
            }
        }

        return success;
    }

    private void setInfoLabel(Color color, String text) {
        infoLabel.setText(text);
        infoLabel.setTextFill(color);
    }

    private void backToLogin() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/nisshoku/todo/view/login.fxml"));
            Parent root = null;
            root = loader.load();
            Scene scene = signUpBtn.getScene();

            ((LoginController)loader.getController()).setInfoLabel(Color.GREEN, "You can log in now.");

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
}
