package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.ArrayList;

public class Controller {
    private ArrayList<Trader> traders;
    private ArrayList<String> tradersNames;



    public TextField TraderIdTextField;
    public TextField TraderNameTextField;
    public TextField TraderPhoneNumberTextField;
    public TextField TraderAddressTextField;

    public TextArea TraderNotesTextArea;
    public TextField[] addTraderTextFields = {TraderIdTextField , TraderNameTextField , TraderPhoneNumberTextField , TraderAddressTextField };


    public TextField searchForTraderByNameTextField;
    public TextField searchForTraderByIdTextField;

    public ListView<String> tradersListView;

    public Button addNewTraderButton;

    private Logger log = new Logger() ;

    public Controller() {
        traders = new ArrayList<>();

        try {
            tradersNames = (ArrayList<String>) log.readData(ArrayList.class , "names.json");
            System.out.println(tradersNames.get(0));
            TraderIdTextField.setText((tradersNames.size() + 1) + "");
        } catch (Exception e) {
            tradersNames = new ArrayList<>();
        }

        showTradersOnList();

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
        for(TextField field : addTraderTextFields) {
            field.setText("");
        }
        TraderIdTextField.setText((tradersNames.size() + 1) + "");
    }

    public void addNewTrader() {
        try {
            TextField nesscaryTextFields[] = {TraderIdTextField, TraderNameTextField, TraderPhoneNumberTextField};
             for(TextField field : nesscaryTextFields) {
                if(field.getText().isBlank()) {
                    throw new IncompleteInputData();
                }
            }


            traders.add(new Trader( Integer.parseInt(TraderIdTextField.getText()) , Integer.parseInt(TraderPhoneNumberTextField.getText()), TraderNameTextField.getText() ,  TraderAddressTextField.getText()  , TraderNotesTextArea.getText()));
            tradersNames.add(TraderNameTextField.getText());

            showConfirmationAlert();
            resetAddTraderFields();

        } catch (Exception e) {
            // Update
            showErrorAlert();
        }

    }

    public void searchForTrader() {
        if(!searchForTraderByNameTextField.getText().isBlank()){

        }
    }

    public void showTradersOnList() {
        ObservableList<String> tradersNamesList =  FXCollections.observableArrayList("adfsadf " ,"afdsdadfs");
        System.out.println(tradersNamesList.get(0));
        this.tradersListView.getItems().addAll(tradersNamesList);

    }

}
