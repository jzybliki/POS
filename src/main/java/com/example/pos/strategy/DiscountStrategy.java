package com.example.pos.strategy;

import com.example.pos.model.ReceiptItem;
import java.util.List;

public interface DiscountStrategy {
    // ZMIANA: Dodajemy argument 'List<ReceiptItem> cart'
    double calculateDiscount(double totalAmount, List<ReceiptItem> cart);
}