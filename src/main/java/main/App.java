package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
        stage.setTitle("Yu-Gi-OH!");
        stage.setScene(new Scene(root));
        stage.setWidth(1024);
        stage.setHeight(720);
        stage.show();
    }
}