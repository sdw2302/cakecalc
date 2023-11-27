package com.martin;

public class Product {
    String productName;
    Double boughtQuantity;
    Double usedQuantity;
    Double priceBought;
    Boolean priceFor100g;
    Boolean totalPrice;
    Double priceUp;

    public Product(String productName, Double boughtQuantity, Double usedQuantity, Double priceBought,
            Boolean priceFor100g, Boolean totalPrice, Double priceUp) {
        this.productName = productName;
        this.boughtQuantity = boughtQuantity;
        this.usedQuantity = usedQuantity;
        this.priceBought = priceBought;
        if (priceFor100g) {
            this.priceFor100g = true;
            this.totalPrice = false;
        } else {
            this.priceFor100g = false;
            this.totalPrice = true;
        }
        this.priceUp = priceUp;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBoughtQuantity() {
        return this.boughtQuantity;
    }

    public void setBoughtQuantity(Double boughtQuantity) {
        this.boughtQuantity = boughtQuantity;
    }

    public Double getUsedQuantity() {
        return this.usedQuantity;
    }

    public void setUsedQuantity(Double usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public Double getPriceBought() {
        return this.priceBought;
    }

    public void setPriceBought(Double priceBought) {
        this.priceBought = priceBought;
    }

    public Boolean isPriceFor100g() {
        return this.priceFor100g;
    }

    public void setPriceFor100g(Boolean priceFor100g) {
        this.priceFor100g = priceFor100g;
        this.totalPrice = !priceFor100g;
    }

    public Boolean isTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(Boolean totalPrice) {
        this.totalPrice = totalPrice;
        this.priceFor100g = !totalPrice;
    }

    public Double getPriceUp() {
        return this.priceUp;
    }

    public void setPriceUp(Double priceUp) {
        this.priceUp = priceUp;
    }

    @Override
    public String toString() {
        String generatedString = "";
        generatedString += productName + ": Использовано " + usedQuantity + " из " + boughtQuantity + " за "
                + priceBought;
        if (isPriceFor100g()) {
            generatedString += " грн/100г + " + priceUp + "%";
        } else {
            generatedString += " грн + " + priceUp + "%";
        }
        return generatedString;
    }

}
