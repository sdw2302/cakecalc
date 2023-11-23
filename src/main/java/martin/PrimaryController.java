package martin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    TextField productName, boughtQuantity, usedQuantity, priceBought, priceUp;

    @FXML
    RadioButton priceRadio1, priceRadio2;

    @FXML
    Button addToListBtn, modifyInListBtn, removeFromListBtn, clearBtn;

    @FXML
    ListView<Product> list;

    @FXML
    Label totalPrice;

    public void addToList() {
        if (productName.getText().equals("") || boughtQuantity.getText().equals("") || usedQuantity.getText().equals("")
                || priceBought.getText().equals("") || priceUp.getText().equals("")) {
            this.createAlert("Все поля должны быть заполнены");
            return;
        }

        for (int i = 0; i < list.getItems().toArray().length; i++) {
            if (list.getItems().get(i).productName.trim().equals(productName.getText().trim())) {
                this.createAlert("Продукт с таким названием уже существует");
                return;
            }
        }

        try {
            if (Double.parseDouble(usedQuantity.getText()) > Double.parseDouble(boughtQuantity.getText())) {
                this.createAlert("Не может быть использовано больше чем имеется");
                return;
            }

            list.getItems().add(new Product(productName.getText(), Double.parseDouble(boughtQuantity.getText()),
                    Double.parseDouble(usedQuantity.getText()), Double.parseDouble(priceBought.getText()),
                    priceRadio1.isSelected(), priceRadio2.isSelected(), Double.parseDouble(priceUp.getText())));
        } catch (Exception e) {
            this.createAlert("Проверьте формат введённых данных");
        }
    }

    public void loadProductToFields() {
        int index = list.getSelectionModel().getSelectedIndex();

        Product product = list.getItems().get(index);

        productName.setText(product.getProductName());
        boughtQuantity.setText(String.valueOf(product.getBoughtQuantity()));
        usedQuantity.setText(String.valueOf(product.getUsedQuantity()));
        priceBought.setText(String.valueOf(product.getPriceBought()));
        priceUp.setText(String.valueOf(product.getPriceUp()));
        priceRadio1.setSelected(product.isPriceFor100g());
        priceRadio2.setSelected(product.isTotalPrice());
    }

    public void modifyInList() {
        if (list.getSelectionModel().isEmpty())
            return;
        int index = list.getSelectionModel().getSelectedIndex();

        Product product = new Product(productName.getText(), Double.parseDouble(boughtQuantity.getText()),
                Double.parseDouble(usedQuantity.getText()), Double.parseDouble(priceBought.getText()),
                priceRadio1.isSelected(), priceRadio2.isSelected(), Double.parseDouble(priceUp.getText()));

        list.getItems().remove(index);
        list.getItems().add(index, product);
    }

    public void removeFromList() {
        if (list.getSelectionModel().getSelectedIndex() < 0)
            return;
        list.getItems().remove(list.getSelectionModel().getSelectedIndex());
    }

    public void clearFields() {
        productName.clear();
        boughtQuantity.clear();
        usedQuantity.clear();
        priceBought.clear();
        priceUp.clear();
        priceRadio2.setSelected(true);
    }

    public void calcCakePrice() {
        if (list.getItems().toArray().length == 0)
            return;

        double price = 0;

        for (int i = 0; i < list.getItems().toArray().length; i++) {
            Product product = list.getItems().get(i);
            if (product.isTotalPrice())
                price += Math
                        .round((product.getPriceBought() * (product.getUsedQuantity() / product.getBoughtQuantity()))
                                * (100.0 + product.getPriceUp()))
                        / 100.0;
            else
                price += Math
                        .round((product.getPriceBought() * (product.getUsedQuantity() / 100.0))
                                * (100.0 + product.getPriceUp()))
                        / 100.0;
        }

        totalPrice.setText(String.valueOf(price) + " грн");
    }

    private void createAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Произошла ошибка");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
