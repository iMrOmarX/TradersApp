package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller  implements Initializable {

    private ArrayList<Trader> traders;
    private ArrayList<String> tradersNames;



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

    public Controller() {

    }

    @Override
    public void initialize(URL url , ResourceBundle rb) {
        traders = new ArrayList<>();

        try {
            tradersNames = (ArrayList<String>) log.readData(ArrayList.class , "names.json");

        } catch (Exception e) {
            tradersNames = new ArrayList<>();
        }
        TraderIdTextField.setText((tradersNames.size() + 1) + "");
        showTradersOnList();

    }



    public void saveNewTrader(Trader newTrader) {
        try {
            log.writeData(tradersNames , "names.json");
            log.writeData(newTrader,  newTrader.getId() + ".json");

            System.out.println("Success Saving");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

            Trader newTrader = new Trader( Integer.parseInt(TraderIdTextField.getText()) , Integer.parseInt(TraderPhoneNumberTextField.getText()), TraderNameTextField.getText() ,  TraderAddressTextField.getText()  , TraderNotesTextArea.getText());
            traders.add(newTrader);
            tradersNames.add(TraderNameTextField.getText());


            saveNewTrader(newTrader);
            resetAddTraderFields();

            showTradersOnList();
            showConfirmationAlert();

        } catch (Exception e) {
            // Update
            System.out.println(e.getMessage());
            showErrorAlert();
        }

    }


    public void searchForTraderByName(ObservableList<String> tradersNamesList) {
        int i = 1 ;
        for(String name: tradersNames) {
            if(name.contains(searchForTraderByNameTextField.getText())) {
                tradersNamesList.add(i +"  " + name);

            }
            i++;
        }
    }
    public void searchForTrader() {
        ObservableList<String> tradersNamesList =  FXCollections.observableArrayList();

        if(!searchForTraderByNameTextField.getText().isBlank()){
            searchForTraderByName(tradersNamesList);
        }
        else if (!searchForTraderByIdTextField.getText().isBlank()){
            try {
                if(Integer.parseInt(searchForTraderByIdTextField.getText()) > tradersNames.size()){

                }
                else {
                    tradersNamesList.add(searchForTraderByIdTextField.getText() + "  "  +tradersNames.get(Integer.parseInt(searchForTraderByIdTextField.getText()) - 1));
                }

            } catch (Exception e) {
                searchForTraderByName(tradersNamesList);
            }
        }
        else {
            showTradersOnList();
            return ;
        }
        tradersListView.setItems(tradersNamesList);
    }

    public void showTradersOnList() {
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
