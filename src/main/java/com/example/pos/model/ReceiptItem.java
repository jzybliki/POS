package com.example.pos.model;

public class ReceiptItem {
    private final Product product;
    private double quantity; // Zmiana z int na double

    public ReceiptItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

    // Zamiast incrementQuantity (+1), mamy dodawanie dowolnej wagi
    public void addQuantity(double amount) {
        this.quantity += amount;
    }

    public Product getProduct() { return product; }
    public String getProductName() { return product.getName(); }
    public double getQuantity() { return quantity; }
    public double getPrice() { return product.getPrice(); }
}