package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class BoughtItemDescriptionPane implements Initializable  {

    private final Item currentItem;

    public TextField ItemNameTextField;
    public TextField ItemIdTextField;
    public TextField ItemPriceTextField;
    public TextField ItemQtyTextField;


    public BoughtItemDescriptionPane(Item currnetItem) {
        super();
        this.currentItem = currnetItem;


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ItemIdTextField.setText(currentItem.getId() + "");
        ItemNameTextField.setText(currentItem.getName());
        ItemPriceTextField.setText(currentItem.getPrice() + "");

    }
}
