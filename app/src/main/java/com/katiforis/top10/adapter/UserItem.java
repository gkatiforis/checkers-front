package com.katiforis.top10.adapter;

public class UserItem {
    public int productImage;
    public String productName;

    public UserItem(String productName, int productImage) {
        this.productImage = productImage;
        this.productName = productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}