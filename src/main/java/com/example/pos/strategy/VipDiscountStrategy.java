package com.example.pos.strategy;

import com.example.pos.model.ReceiptItem;
import java.util.List;

public class VipDiscountStrategy implements DiscountStrategy {

    private final DiscountStrategy next; // To pole jest kluczowe dla łańcucha

    // KONSTRUKTOR - Tego brakuje u Ciebie!
    public VipDiscountStrategy(DiscountStrategy next) {
        this.next = next;
    }

    @Override
    public double calculateDiscount(double totalAmount, List<ReceiptItem> cart) {
        // 1. Najpierw pobierz rabat z "głębszych" warstw
        double prevDiscount = next.calculateDiscount(totalAmount, cart);

        // 2. Oblicz kwotę, która została po odjęciu tamtych rabatów
        double baseForMe = totalAmount - prevDiscount;

        // 3. Sprawdź warunek na ORYGINALNEJ sumie, ale procent licz od POZOSTAŁEJ
        double myDiscount = 0.0;
        if (totalAmount > 50.00) {
            myDiscount = baseForMe * 0.10; // 10%
        }

        return prevDiscount + myDiscount;
    }

    @Override
    public String toString() {
        String nextDesc = next.toString();
        // Jeśli następny opis jest pusty, nie dodawaj przecinka
        return "VIP 10%" + (nextDesc.isEmpty() ? "" : ", " + nextDesc);
    }
}