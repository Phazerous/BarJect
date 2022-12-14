package com.barteam.barject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class BarJectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BarJectApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),900, 600);
        stage.setTitle("BarJect");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ApplicationLogo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}