package controller;

import javafx.fxml.FXML;
import main.App;

import java.io.IOException;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("/fxml/primary");
    }
}