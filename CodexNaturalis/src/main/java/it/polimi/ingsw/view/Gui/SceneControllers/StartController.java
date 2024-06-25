package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.view.Gui.ScenesName;
import it.polimi.ingsw.view.Gui.Sound;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

/**
 * Controller class for the start scene where connection options are selected.
 */
public class StartController extends GenericSceneController {

    @FXML
    TextField IpTextField;

    @FXML
    CheckBox RMIcheckBox;

    @FXML
    CheckBox SocketcheckBox;

    @FXML
    Text ErrorMessage;

    /**
     * Event handler method for the "Ask Connection" button click.
     * Shows the ask connection scene and plays an opening sound.
     *
     * @param event The ActionEvent triggered by the user.
     * @throws IOException If an I/O exception occurs.
     */
    @FXML
    private void AskConnection(ActionEvent event) throws IOException {
        print("key pressed");
        getGuiApplication().showScene(ScenesName.ASKCONNECTION);
        Sound.playSound("opening.wav");
    }

    /**
     * Event handler method for the "Confirm Connection" button click.
     * Validates the selected connection type and IP address,
     * then attempts to establish the connection.
     *
     * @param event The ActionEvent triggered by the user.
     * @throws IOException       If an I/O exception occurs.
     * @throws NotBoundException If the remote object is not bound.
     */
    @FXML
    private void ConfirmConnection(ActionEvent event) throws IOException, NotBoundException {
        print("key pressed");
        if (RMIcheckBox.isSelected() && SocketcheckBox.isSelected()) {
            ErrorMessage.setText("Please Select only one type of connection");
            ErrorMessage.setFill(Color.RED);
        } else if (!RMIcheckBox.isSelected() && !SocketcheckBox.isSelected()) {
            ErrorMessage.setText("Please Select a type of connection");
            ErrorMessage.setFill(Color.RED);
        } else if (!IpTextField.getText().equals("") && !isValidIPAddress(IpTextField.getText())) {
            ErrorMessage.setText("Not valid IP");
            ErrorMessage.setFill(Color.RED);
        } else {
            ErrorMessage.setText("Connecting......");
            ErrorMessage.setFill(Color.GREEN);
            getGuiApplication().getGui().connect(RMIcheckBox.isSelected(), IpTextField.getText());
        }
    }

    /**
     * Validates if the provided IP address is in a valid format.
     *
     * @param ip The IP address string to validate.
     * @return {@code true} if the IP address is valid, {@code false} otherwise.
     */
    private boolean isValidIPAddress(String ip) {
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);

        if (ip == null) {
            return false;
        }
        if (ip.equals("localhost")) {
            return true;
        }
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    /**
     * Shows an error message with a specified string and sets the text color to red.
     *
     * @param string The error message to display.
     */
    @Override
    public void ShowErrorMessage(String string) {
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
    }

    /**
     * Event handler method for deselecting RMI checkbox.
     *
     * @param event The ActionEvent triggered by the user.
     */
    @FXML
    void onlyOne(ActionEvent event) {
        RMIcheckBox.setSelected(false);
    }

    /**
     * Event handler method for deselecting Socket checkbox.
     *
     * @param event The ActionEvent triggered by the user.
     */
    @FXML
    void onlyTwo(ActionEvent event) {
        SocketcheckBox.setSelected(false);
    }
}
