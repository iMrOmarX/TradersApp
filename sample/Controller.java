package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller  implements Initializable {

    private ArrayList<Trader> traders;
    private ArrayList<String> tradersNames;
    private ArrayList<Trader> currentWantedTraders;


    public TextField TraderIdTextField;

    public TextField TraderNameTextField;

    public TextField TraderPhoneNumberTextField;

    public TextField TraderAddressTextField;


    public TextArea TraderNotesTextArea;


    //public TextField[] addTraderTextFields = {TraderIdTextField , TraderNameTextField , TraderPhoneNumberTextField , TraderAddressTextField };
    public TextField searchForTraderByNameTextField;

    public TextField searchForTraderByIdTextField;

    public ListView tradersListView;

    public Button addNewTraderButton;

    private final Logger log = new Logger() ;
    private DatabaseConnecter db;



    private TraderDataPanelController traderDataController;

    public VBox WantToBuyItemsScrollPane;

    public ScrollPane ItemsScrollPane;
    public TextField SearchForItemByNameTextField;
    public TextField SearchForItemByIdTextField;

    public TableView<Item> ItemsTable ;

    public TableColumn ItemIdColumn;
    public TableColumn ItemNameColumn;
    public TableColumn ItemPriceColumn;
    public TableColumn ItemNotesColumn;
    public TableColumn ItemTraderName;

    public ArrayList<Item> boughtItems;
    public ArrayList<BoughtItemDescriptionPane> boughtItemsPanes;
    private ArrayList<Node> itemsNodes;


    public Button BuyButton;

    @Override
    public void initialize(URL url , ResourceBundle rb) {


        try {

            db = new DatabaseConnecter();

            traders = db.getTradersByName("");

            currentWantedTraders = traders;
            tradersNames = db.getTradersNames();
        } catch (SQLException | IOException e)  {
            System.out.println(e.getCause());

        }

        TraderIdTextField.setText((tradersNames.size() + 1) + "");

        try {
            showTradersOnList();
            setupTable();
            serachForItemByNameAndUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        boughtItemsPanes = new ArrayList<>();
        boughtItems = new ArrayList<>();
        itemsNodes = new ArrayList<>();
        tradersListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    int currentItemSelected = tradersListView.getSelectionModel()
                            .getSelectedIndex();
                    //use this to do whatever you want to. Open Link etc.

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/traderDataScene.fxml"));
                        Parent root1 = fxmlLoader.load();
                        Stage stage = new Stage();

                        traderDataController =  fxmlLoader.getController();


                        traderDataController.setCurrentTrader(currentWantedTraders.get(currentItemSelected));
                        traderDataController.setDatabase(db);

                        stage.setTitle("Trader Info");
                        stage.setScene(new Scene(root1));


                        stage.show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ItemsTable.setRowFactory( tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Item currnetBoughtItem = row.getItem();
                    if(!boughtItems.contains(currnetBoughtItem)) {
                        addNewItemPane(currnetBoughtItem);
                        boughtItems.add(currnetBoughtItem);
                    }


                }
            });
            return row ;
        });
    }



    public void saveData() {
        try {
            log.writeData(tradersNames , "names.json");
            for(Trader trader : traders) {
                log.writeData(trader ,  trader.getId() + ".json");
            }
            System.out.println("Success Saving");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void showConfirmationAlert(String input) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("???????? ??????????????");
        alert.setHeaderText(input);


        alert.showAndWait();
    }

    public void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("??????");
        alert.setHeaderText("???????????????? ?????????????? ?????? ??????????");
        alert.setContentText("???????? ?????????? ?????????????? ?????? ????????");

        alert.showAndWait();
    }


    public void resetAddTraderFields() {
        TextField[] addTraderTextFields = {TraderIdTextField , TraderNameTextField , TraderPhoneNumberTextField , TraderAddressTextField };
        for(TextField field : addTraderTextFields) {
            field.setText("");
        }

        TraderIdTextField.setText((tradersNames.size() + 1) + "");
        TraderNotesTextArea.setText("");

    }

    public void addNewTrader() {
        try {
            TextField[] nesscaryTextFields = {TraderIdTextField, TraderNameTextField, TraderPhoneNumberTextField};
            for(TextField field : nesscaryTextFields) {
                if(field.getText().isBlank()) {
                    throw new IncompleteInputData();
                }
            }

            Trader newTrader = new Trader( Integer.parseInt(TraderIdTextField.getText()) , TraderPhoneNumberTextField.getText(), TraderNameTextField.getText() ,  TraderAddressTextField.getText()  , TraderNotesTextArea.getText());
            traders.add(newTrader);
            currentWantedTraders.add(newTrader);
            //tradersNames.add(TraderNameTextField.getText());


            db.saveNewTrader(newTrader);
            tradersNames = db.getTradersNames();
            resetAddTraderFields();

            showTradersOnList();
            showConfirmationAlert("?????? ???? ?????????? ???????????? ???????????? ????????????");

        } catch (Exception e) {
            // update
            System.out.println(e.getMessage());
            showErrorAlert();
        }

    }

    public void setupTable() {
        ItemIdColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer >("id"));
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String >("name"));
        ItemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, Float >("price"));
        ItemNotesColumn.setCellValueFactory(new PropertyValueFactory<Item, String >("notes"));
        ItemTraderName.setCellValueFactory(new PropertyValueFactory<Item , String>("traderName"));


    }


    public void searchForTrader() {
        ObservableList<String> tradersNamesList =  FXCollections.observableArrayList();

        currentWantedTraders = new ArrayList<>();

        if(!searchForTraderByNameTextField.getText().isBlank()){
            try {
                currentWantedTraders = db.getTradersByName(searchForTraderByNameTextField.getText());

            } catch (SQLException throwables) {
                throwables.printStackTrace(); // update
            }
        }
        else if (!searchForTraderByIdTextField.getText().isBlank()){
            try {
                Trader wantedTrader = db.getTraderById(Integer.parseInt(searchForTraderByIdTextField.getText()));
                if(wantedTrader != null)
                    currentWantedTraders.add(wantedTrader);

            } catch (Exception e) {
                e.printStackTrace(); // update
            }
        }
        else {

            try {
                showTradersOnList();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return ;
        }
        if(!currentWantedTraders.isEmpty()) {
            for(Trader s : currentWantedTraders) {
                tradersNamesList.add(s.getId() + "  "  + s.getName());
            }
        }

        tradersListView.setItems(tradersNamesList);
    }

    public void showTradersOnList() throws SQLException {
        ArrayList<String> tradersNamesWithId = new ArrayList<>();
        int i = 1 ;

        for(String name : tradersNames) {
            tradersNamesWithId.add(i + "  " + name);
            i++;
        }
        ObservableList<String> tradersNamesList =  FXCollections.observableArrayList(tradersNamesWithId);

        tradersListView.setItems(tradersNamesList);

    }

    public void addNewItemPane(Item addedItem) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/BoughtItemDescriptionPane.fxml"));

        BoughtItemDescriptionPane itemController = new BoughtItemDescriptionPane(addedItem , this);

        loader.setController(itemController);

        System.out.println("Adding item pane");
        try {
            Node newItemNode = loader.load();
            itemsNodes.add(newItemNode);
            this.WantToBuyItemsScrollPane.getChildren().add(newItemNode);
            boughtItemsPanes.add(itemController);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void serachForItemByNameAndUpdate() throws SQLException {
        String searchedItem = SearchForItemByNameTextField.getText();

        ArrayList<Item> items = db.getItems( SearchForItemByNameTextField.getText() );

        ItemsTable.setItems(FXCollections.observableArrayList(items));

    }

    public void removeItem(BoughtItemDescriptionPane boughtItemDescriptionPane) {
        int i = 0 ;
        for (BoughtItemDescriptionPane s : boughtItemsPanes){
            if (s == boughtItemDescriptionPane){
                break;
            }
            i++;
        }

        boughtItemsPanes.remove(i);
        boughtItems.remove(i);

        this.WantToBuyItemsScrollPane.getChildren().remove(itemsNodes.get(i));
        itemsNodes.remove(i);
    }


    public void resetBoughtItems(){
        boughtItemsPanes.clear();
        boughtItems.clear();
        this.WantToBuyItemsScrollPane.getChildren().clear();
        itemsNodes.clear();
    }

    public void buyItems() {
        for(BoughtItemDescriptionPane s : boughtItemsPanes) {
            s.setQty();
        }

        PDFConverter pdfConverter = new PDFConverter();
        try {
            pdfConverter.printBill(boughtItems);
            showConfirmationAlert("?????? ???? ?????????? ???????????? ??????????");

        } catch (Exception e) {
            e.printStackTrace();
        }

        resetBoughtItems();


    }
}
