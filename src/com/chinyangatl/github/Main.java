package com.chinyangatl.github;

import com.chinyangatl.github.dataSource.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Entry point for the JavaFX application for it to run.
 * Opens the database within the init() method
 * and closes it within the stop() method
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.0
 */
public class Main extends Application {
    @Override
    public void init() throws Exception {
        super.init();
        if(!DataSource.getInstance().open()) {
            System.out.println("Fatal error. Couldn't connect to db");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/login.fxml")));
        primaryStage.setTitle("Welcome To Bankist");
        //primaryStage.getIcons().add(new Image("/com/chinyangatl/github/res/icon.png"));
        primaryStage.setScene(new Scene(root, 600, 375));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
