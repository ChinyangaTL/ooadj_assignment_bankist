package com.chinyangatl.github.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The Controller Class for the adminLogin.fxml file which represents the Admin's login screen.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.0
 */
public class AdminLoginController {
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUserID;
    @FXML
    private TextField txtPassword;

    public void switchToCustomerLogin() throws IOException {
        switchScenes("/com/chinyangatl/github/view/login.fxml", "Welcome To Bankist", 600, 375, btnLogin);
    }

    public void goToDashboard() throws IOException {
        switchScenes("/com/chinyangatl/github/view/adminDashboard.fxml", "Admin Dashboard", 715, 486, btnLogin);
    }

    public void switchScenes(String scene, String title, int width, int height, Node node ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(scene));
        Stage window = (Stage) node.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(root, width, height));
        window.setResizable(false);
        window.show();
    }

    public void login() throws NumberFormatException, IOException, SQLException {
        if(txtUserID.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            Controller.throwError("No fields can be empty");
        } else {
            btnLogin.setOnAction(event -> {
                try {
                    goToDashboard();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }



}
