package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class BoughtItemDescriptionPane implements Initializable  {

    private final Item currentItem;

    public TextField ItemNameTextField;
    public TextField ItemIdTextField;
    public TextField ItemPriceTextField;
    public TextField ItemQtyTextField;

    public int qty ;

    public Button RemoveButton;

    private Controller currentController;

    public BoughtItemDescriptionPane(Item currnetItem , Controller currnetController) {
        super();
        this.currentItem = currnetItem;
        this.currentController = currnetController;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ItemIdTextField.setText(currentItem.getId() + "");
        ItemNameTextField.setText(currentItem.getName());
        ItemPriceTextField.setText(currentItem.getPrice() + "");


    }

    public Item getCurrentItem() {
        if(ItemQtyTextField.getText().isBlank()) {
            qty = 0 ;
        }
        else {
            try {
                qty = Integer.parseInt(ItemQtyTextField.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        this.currentItem.setQty(qty);

        return currentItem;
    }





    public void removeItem() {
        currentController.removeItem(this);
    }
}
