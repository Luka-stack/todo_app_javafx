package com.nisshoku.todo.controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.nisshoku.todo.database.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskThumbController implements Initializable {

    @FXML
    private Label nameLbl;

    @FXML
    private Label dateLbl;

    @FXML
    private ImageView deleteImg;

    @FXML
    private ImageView editImg;

    private StackPane parentPane;
    private Label infoLbl;
    private String description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void setTask(StackPane parentPane, Label infoLbl, String name, String description, String date) {
        nameLbl.setText(name);
        dateLbl.setText(date);
        this.parentPane = parentPane;
        this.infoLbl = infoLbl;
        this.description = description;
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == nameLbl) {

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(nameLbl.getText() + "\t\t\t\t\t\tCreated At: " + dateLbl.getText()));
            content.setBody(new Text(description));

            JFXDialog dialog = new JFXDialog(parentPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton button = new JFXButton("Done");
            button.setOnAction(eventBtn -> { dialog.close(); });
            content.setActions(button);
            dialog.show();
        }
        else if (event.getSource() == deleteImg) {

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Delete -> " + nameLbl.getText()));
            content.setBody(new Text("You are sure you want to delete " + nameLbl.getText() + " task?"));

            JFXDialog dialog = new JFXDialog(parentPane, content, JFXDialog.DialogTransition.RIGHT);

            JFXButton positiveBtn = new JFXButton("Delete");
            positiveBtn.setTextFill(Color.CRIMSON);
            positiveBtn.setOnAction(eventBtn -> {

                DatabaseHandler db = new DatabaseHandler();
                db.deleteTask(nameLbl.getText());
                setInfoLabel(Color.CRIMSON, "Successfully deleted, just Refresh!");
                dialog.close();
            });
            JFXButton negativeBtn = new JFXButton("Cancel");
            negativeBtn.setOnAction(eventBtn -> { dialog.close(); });

            content.setActions(positiveBtn, negativeBtn);
            dialog.show();
        }
        else if (event.getSource() == editImg) {

            JFXTextField newName = new JFXTextField(nameLbl.getText());
            JFXTextArea newDesc = new JFXTextArea(description);

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(newName);
            content.setBody(newDesc);

            JFXDialog dialog = new JFXDialog(parentPane, content, JFXDialog.DialogTransition.LEFT);

            JFXButton updateBtn = new JFXButton("Update");
            updateBtn.setTextFill(Color.GREEN);
            updateBtn.setOnAction(eventBtn -> {

                DatabaseHandler db = new DatabaseHandler();
                db.updateTask(nameLbl.getText(), newName.getText(), newDesc.getText());
                setInfoLabel(Color.GREEN, "Successfully updated, just Refresh!");
                dialog.close();
            });
            JFXButton negativeBtn = new JFXButton("Cancel");
            negativeBtn.setOnAction(eventBtn -> { dialog.close(); });

            content.setActions(updateBtn, negativeBtn);
            dialog.show();
        }
    }

    private void setInfoLabel(Color color, String text) {
        infoLbl.setText(text);
        infoLbl.setTextFill(color);
    }
}
