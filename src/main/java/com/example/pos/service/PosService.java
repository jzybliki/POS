package com.example.pos.service;

import com.example.pos.model.Product;
import com.example.pos.model.ReceiptItem;
import com.example.pos.repository.ProductRepository;
import com.example.pos.strategy.DiscountStrategy;
import com.example.pos.utils.ReceiptPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PosService {
    private final ProductRepository repository;
    private final DiscountStrategy discountStrategy;

    // Stan koszyka (ArrayList)
    private final List<ReceiptItem> cart = new ArrayList<>();

    // Konstruktor przyjmuje zależności (Dependency Injection)
    public PosService(ProductRepository repository, DiscountStrategy discountStrategy) {
        this.repository = repository;
        this.discountStrategy = discountStrategy;
    }

    public ReceiptItem scanProduct(String barcode) {
        Optional<Product> productOpt = repository.findByBarcode(barcode);

        if (productOpt.isPresent()) {
            Product p = productOpt.get();
            // Sprawdź czy produkt już jest w koszyku
            for (ReceiptItem item : cart) {
                if (item.getProduct().getBarcode().equals(p.getBarcode())) {
                    item.incrementQuantity();
                    return item;
                }
            }
            // Jeśli nie, dodaj nowy
            ReceiptItem newItem = new ReceiptItem(p, 1);
            cart.add(newItem);
            return newItem;
        }
        return null;
    }

    public List<ReceiptItem> getCart() {
        return cart;
    }

    public String checkout() {
        if (cart.isEmpty()) return "Koszyk jest pusty!";

        double total = cart.stream().mapToDouble(ReceiptItem::getTotal).sum();
        double discount = discountStrategy.calculateDiscount(total);
        double toPay = total - discount;

        StringBuilder sb = new StringBuilder();
        sb.append("--- SKLEP SPOŻYWCZY ---\n");
        sb.append("--- PARAGON FISKALNY ---\n\n");

        for (ReceiptItem item : cart) {
            sb.append(String.format("%-15s x%d  %.2f PLN\n",
                    item.getProductName(), item.getQuantity(), item.getTotal()));
        }

        sb.append("\n------------------------\n");
        sb.append(String.format("SUMA:          %.2f PLN\n", total));
        sb.append(String.format("RABAT:        -%.2f PLN\n", discount));
        sb.append(String.format("DO ZAPŁATY:    %.2f PLN\n", toPay));
        sb.append("------------------------\n");
        sb.append("Dziękujemy za zakupy!");

        String receiptContent = sb.toString();

        // Uruchomienie wątku drukującego (Thread)
        Thread printerThread = new Thread(new ReceiptPrinter(receiptContent));
        printerThread.start();

        cart.clear(); // Wyczyść koszyk
        return receiptContent;
    }
}