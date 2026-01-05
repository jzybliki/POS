package com.example.pos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    private String barcode; // Kod kreskowy jako ID
    private String name;
    private double price;

    public Product() {}

    public Product(String barcode, String name, double price) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
    }

    // Gettery
    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}