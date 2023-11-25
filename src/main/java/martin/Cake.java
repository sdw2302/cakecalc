package martin;

import java.util.ArrayList;

public class Cake {
    String cakeName;
    ArrayList<Product> products;

    public Cake(String cakeName, ArrayList<Product> products) {
        this.cakeName = cakeName;
        this.products = products;
    }

    public String getCakeName() {
        return this.cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return cakeName;
    }
}
