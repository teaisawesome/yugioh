package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.io.IOException;


@Slf4j
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
    private Pagination player1Image;

    @FXML
    private Pagination player2Image;


    @FXML
    public void initialize()
    {
        player1Image.setPageFactory(pageIndex -> {
            Pane box = new Pane();

            box.setStyle(
                    "-fx-background-image: url('" + getClass().getResource("/pictures/playerpictures/playerPicture" + (pageIndex + 1) + ".png").toExternalForm() + "');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );

            return box;
        });

        player2Image.setPageFactory(pageIndex -> {
            Pane box = new Pane();

            box.setStyle(
                    "-fx-background-image: url('" + getClass().getResource("/pictures/playerpictures/playerPicture" + (pageIndex + 1) + ".png").toExternalForm() + "');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );

            return box;
        });
    }


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

    public void rules(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/rules.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Rules");
        stage.show();
    }
}