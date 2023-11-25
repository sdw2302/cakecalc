package martin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    TextField productName, boughtQuantity, usedQuantity, priceBought, priceUp, cakeName;

    @FXML
    RadioButton priceRadio1, priceRadio2;

    @FXML
    Button addToListBtn, modifyInListBtn, removeFromListBtn, clearBtn;

    @FXML
    ListView<Product> list;

    @FXML
    Label totalPrice;

    @FXML
    ComboBox<Cake> comboBox;

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

    public void refreshCakeList() {
        try {
            File file = new File("savedCakes");
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            RandomAccessFile rafile = new RandomAccessFile(file, "rw");

            rafile.seek(0);
            int ctr = rafile.readInt();

            ArrayList<Cake> cakesList = new ArrayList<>();

            for (int i = 0; i < ctr; i++) {
                String cakeName = rafile.readUTF();
                ArrayList<Product> productsList = new ArrayList<>();
                int productsCtr = rafile.readInt();
                for (int j = 0; j < productsCtr; j++) {
                    productsList.add(new Product(rafile.readUTF(), rafile.readDouble(), rafile.readDouble(),
                            rafile.readDouble(), rafile.readBoolean(), rafile.readBoolean(), rafile.readDouble()));
                }
                cakesList.add(new Cake(cakeName, productsList));
            }
            comboBox.getItems().clear();
            comboBox.getItems().addAll(cakesList);

            rafile.close();
        } catch (FileNotFoundException e) {
            this.createAlert("Файл с сохранёнными тортами не существует или находится в другом месте");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCakeIngredientsToList() {
        list.getItems().clear();
        list.getItems().addAll(comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex()).getProducts());
        cakeName.setText(comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex()).getCakeName());
    }

    public void saveCake() {
        ArrayList<Product> productsArray = new ArrayList<>();

        for (int i = 0; i < list.getItems().toArray().length; i++) {
            productsArray.add(list.getItems().get(i));
        }

        comboBox.getItems().add(new Cake(cakeName.getText(), productsArray));
        saveToFile();
    }

    public void modifySelectedCake() {
        comboBox.getItems().remove(comboBox.getSelectionModel().getSelectedIndex());
        saveCake();
    }

    public void removeSelectedCake() {
        comboBox.getItems().remove(comboBox.getSelectionModel().getSelectedIndex());
        cakeName.clear();
        list.getItems().clear();
        saveToFile();
    }

    private void saveToFile() {
        try {
            File file = new File("savedCakes");
            file.delete();
            file.createNewFile();
            RandomAccessFile rafile = new RandomAccessFile(file, "rw");

            rafile.seek(0);
            rafile.writeInt(comboBox.getItems().toArray().length);

            for (int i = 0; i < comboBox.getItems().toArray().length; i++) {
                Cake cake = comboBox.getItems().get(i);
                rafile.writeUTF(cake.getCakeName());
                rafile.writeInt(cake.getProducts().toArray().length);
                for (Product product : cake.getProducts()) {
                    rafile.writeUTF(product.getProductName());
                    rafile.writeDouble(product.getBoughtQuantity());
                    rafile.writeDouble(product.getUsedQuantity());
                    rafile.writeDouble(product.getPriceBought());
                    rafile.writeBoolean(product.isPriceFor100g());
                    rafile.writeBoolean(product.isTotalPrice());
                    rafile.writeDouble(product.getPriceUp());
                }
            }

            rafile.close();
        } catch (Exception e) {
        }
    }
}
