package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
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


    // Add Item Tab
    public TextField AddProductIdTextField;
    public TextField AddProductNameTextField;
    public TextField AddProductPriceTextField;
    public TextArea AddProductNotesTextField;
    public Button AddProductButton;

    private TextField[] TraderTextFields = {TraderNameTextField , TraderPhoneNumberTextField , TraderAddressTextField , TraderIdTextField};

    private DatabaseConnecter db;

    public TraderDataPanelController () {

    }


    public void setCurrentTrader(Trader currentTrader) {
        this.currentTrader = currentTrader;
    }

    public void setDatabase(DatabaseConnecter db) {
        this.db =db;
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

            try {
                AddProductIdTextField.setText(db.getNumberOfItems()+ 1 + "");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } );

    }


    public void resetItemsFields() throws SQLException{
        try {
            TextField[] AddProductTextFields = {AddProductIdTextField ,AddProductNameTextField ,AddProductPriceTextField};
            for(TextField field: AddProductTextFields) {
                field.setText("");
            }
            AddProductNotesTextField.setText("");
            AddProductIdTextField.setText("" + db.getNumberOfItems());
        } catch (Exception e){
            throw e;
        }

    }
    public void addNewItem(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(AddProductIdTextField.getText());
            String name = AddProductNameTextField.getText();
            float price = Float.parseFloat(AddProductPriceTextField.getText());
            String notes = AddProductNotesTextField.getText();
            int traderId = this.currentTrader.getId();

            db.saveNewItem(new Item(id,price,traderId,name , notes));
            resetItemsFields();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
