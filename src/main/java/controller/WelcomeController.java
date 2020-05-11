package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private TextField player1Textfield;

    @FXML
    private TextField player2Textfield;

    @FXML
    private Label player1ErrorLabel;
    @FXML
    private Label player2ErrorLabel;

    @FXML
    private void startAction(ActionEvent actionEvent) throws IOException
    {

        if(player1Textfield.getText().isEmpty())
        {
            player1ErrorLabel.setText("Kérlek adj meg egy nevet!");
        }
        else
        {
            player1ErrorLabel.setText("");
        }
        if(player2Textfield.getText().isEmpty())
        {
            player2ErrorLabel.setText("Kérlek adj meg egy nevet!");
        }
        else
        {
            player2ErrorLabel.setText("");
        }
        if(!player1Textfield.getText().isEmpty() && !player2Textfield.getText().isEmpty())
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().initPlayerNames(player1Textfield.getText(), player2Textfield.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}