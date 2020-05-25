package game.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BoardIsFullException extends Exception
{
    public BoardIsFullException()
    {
        Alert alertDialog = new Alert(Alert.AlertType.NONE);
        alertDialog.setTitle("Yu-Gi-OH!");
        alertDialog.setContentText("Your board is FULL!");
        ButtonType cancelButton = new ButtonType("Cancel");

        alertDialog.getButtonTypes().setAll(cancelButton);

        Optional<ButtonType> result = alertDialog.showAndWait();

        if(result.get() == cancelButton)
        {
            alertDialog.close();
        }
    }
}
