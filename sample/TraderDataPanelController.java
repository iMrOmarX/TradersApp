package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private final TextField[] TraderTextFields = {TraderNameTextField , TraderPhoneNumberTextField , TraderAddressTextField , TraderIdTextField};

    private DatabaseConnecter db;



    public TextField SearchForItemByNameTextField;
    public TableView<Item> ItemsTable ;


    public TableColumn ItemIdColumn;
    public TableColumn ItemNameColumn;
    public TableColumn ItemPriceColumn;
    public TableColumn ItemNotesColumn;



    public void setCurrentTrader(Trader currentTrader) {
        this.currentTrader = currentTrader;
    }

    public void setDatabase(DatabaseConnecter db) {
        this.db =db;
    }

    public void updateItemsTable() throws SQLException{
        ArrayList<Item> items = db.getItemsOfTrader(currentTrader);

        ItemsTable.setItems(FXCollections.observableArrayList(items));

    }

    public void initializeTextFields() {
        TraderIdTextField.setText(currentTrader.getId() + "");
        TraderNameTextField.setText(currentTrader.getName());
        TraderPhoneNumberTextField.setText(currentTrader.getPhoneNumber());
        TraderAddressTextField.setText(currentTrader.getAddress());
        TraderNotesTextField.setText(currentTrader.getNotes());

    }


    public void serachForItemByNameAndUpdate() throws SQLException {
        String searchedItem = SearchForItemByNameTextField.getText();
        ArrayList<Item> items = db.getItemsOfTrader(currentTrader, searchedItem);

        ItemsTable.setItems(FXCollections.observableArrayList(items));
    }

    public void setupTable() {
        ItemIdColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer >("id"));
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String >("name"));
        ItemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, Float >("price"));
        ItemNotesColumn.setCellValueFactory(new PropertyValueFactory<Item, String >("notes"));



    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            initializeTextFields();

            try {
                AddProductIdTextField.setText(db.getNumberOfItems()+ 1 + "");
                setupTable();
                updateItemsTable();
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
            AddProductIdTextField.setText("" + (db.getNumberOfItems() +1));
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
            updateItemsTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
