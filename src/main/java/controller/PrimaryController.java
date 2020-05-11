package controller;

import javafx.fxml.FXML;
import main.App;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("/fxml/welcome");
    }
}
