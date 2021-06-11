package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

    private Logger log = new Logger() ;
    private DatabaseConnecter db;


    private TraderDataPanelController traderDataController;

    @Override
    public void initialize(URL url , ResourceBundle rb) {


        try {

            db = new DatabaseConnecter();
            traders = db.getTradersByName("");
            currentWantedTraders = traders;
            tradersNames = db.getTradersNames();
        } catch (SQLException e)  {
            System.out.println(e.getCause());
        }

        TraderIdTextField.setText((tradersNames.size() + 1) + "");

        try {
            showTradersOnList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tradersListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    int currentItemSelected = tradersListView.getSelectionModel()
                            .getSelectedIndex();
                    //use this to do whatever you want to. Open Link etc.
                    System.out.println(currentItemSelected);
                    System.out.println(currentWantedTraders.size());
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/traderDataScene.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();

                        traderDataController =  fxmlLoader.<TraderDataPanelController>getController();

                        traderDataController.setCurrentTrader(currentWantedTraders.get(currentItemSelected));

                        stage.setTitle("Trader Info");
                        stage.setScene(new Scene(root1));


                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
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
    public void showConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("نجاح العملية");
        alert.setHeaderText("لقد تم إدخال بيانات التاجر الجديد");


        alert.showAndWait();
    }

    public void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطأ");
        alert.setHeaderText("البيانات المدخلة غير صحيحة");
        alert.setContentText("يرجى إعادة إدخالها مرة أخرى");

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
            TextField nesscaryTextFields[] = {TraderIdTextField, TraderNameTextField, TraderPhoneNumberTextField};
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
            showConfirmationAlert();

        } catch (Exception e) {
            // update
            System.out.println(e.getMessage());
            showErrorAlert();
        }

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

}
