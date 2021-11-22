package com.chinyangatl.github.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Contains methods that I saw myself using multiple times during front end development.
 * Decided to refactor the code to have them used in one place and just make calls to this class when i need them.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.0
 */

public class HelperMethods {
    public static void alterTextFieldToNumbersOnly(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public static Alert showAlert(String type, String header, String msg) {
        Alert alert = new Alert(Alert.AlertType.valueOf(type));
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.show();
        return alert;
    }

    public void switchScenes(String scene, String title, int width, int height, Node node) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(scene)));
        Stage window = (Stage) node.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(root, width, height));
        window.setResizable(false);
        window.show();
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;
            try {
                root = FXMLLoader.load(HelperMethods.class.getResource(fxmlFile));

            }catch (IOException error) {
                error.printStackTrace();
            }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
