package sample;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TraderDataPanelController implements Initializable {
    Trader currentTrader ;

    public TextField TraderIdTextField ;
    public TextField TraderNameTextField;
    public TextField TraderPhoneNumberTextField;
    public TextField TraderAddressTextField;
    public TextArea TraderNotesTextField;

    public Button ChangeTraderDataButton;
    public Button SaveChangedTraderData;

    private TextField[] TraderTextFields = {TraderNameTextField , TraderPhoneNumberTextField , TraderAddressTextField , TraderIdTextField};

    public TraderDataPanelController () {

    }


    public void setCurrentTrader(Trader currentTrader) {
        this.currentTrader = currentTrader;
    }

    public void initializeTextFields() {
        TraderIdTextField.setText(currentTrader.getId() + "");
        TraderNameTextField.setText(currentTrader.getName());
        TraderPhoneNumberTextField.setText(currentTrader.getPhoneNumber());
        TraderAddressTextField.setText(currentTrader.getAddress());
        TraderNotesTextField.setText(currentTrader.getNotes());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            initializeTextFields();
        } );

    }
}
