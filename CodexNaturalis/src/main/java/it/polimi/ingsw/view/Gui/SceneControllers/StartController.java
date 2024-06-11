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

public class StartController extends GenericSceneController {

    @FXML
    TextField IpTextField;


    @FXML
    CheckBox RMIcheckBox;

    @FXML
    CheckBox SocketcheckBox;

    @FXML
    Text ErrorMessage;

    @FXML
    private void AskConnection(ActionEvent event) throws IOException {
        print("key pressed");
        getGuiApplication().showScene(ScenesName.ASKCONNECTION);
        Sound.playSound("opening.wav");
    }

    @FXML
    private void ConfirmConnection(ActionEvent event) throws IOException, NotBoundException {
        print("key pressed");
        if (RMIcheckBox.isSelected() && SocketcheckBox.isSelected()){
            ErrorMessage.setText("Please Select only one type of connection");
            ErrorMessage.setFill(Color.RED);
        }else if (!RMIcheckBox.isSelected() && !SocketcheckBox.isSelected()){
            ErrorMessage.setText("Please Select a type of connection");
            ErrorMessage.setFill(Color.RED);

        }else if (!IpTextField.getText().equals("") && !isValidIPAddress(IpTextField.getText())){
            ErrorMessage.setText("Not valid IP");
            ErrorMessage.setFill(Color.RED);
        }else{
            ErrorMessage.setText("Connecting......");
            ErrorMessage.setFill(Color.GREEN);
            getGuiApplication().getGui().connect(RMIcheckBox.isSelected(),IpTextField.getText());

        }
    }
    private boolean isValidIPAddress(String ip)
    {
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
        if(ip.equals("localhost")){
            return true;
        }
        Matcher m = p.matcher(ip);
        return m.matches();
    }
    @Override
    public void ShowErrorMessage(String string){
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
    }

}
