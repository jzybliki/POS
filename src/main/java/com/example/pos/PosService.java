package com.example.pos;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PosService {
    private final ProductRepository repository;
    // Prosty koszyk w pamięci (lista zeskanowanych produktów)
    private List<Product> currentCart = new ArrayList<>();

    public PosService(ProductRepository repository) {
        this.repository = repository;
        // Dodajmy przykładowe produkty do bazy na start
        repository.save(new Product("111", "Mleko", 3.50));
        repository.save(new Product("222", "Chleb", 4.20));
        repository.save(new Product("333", "Masło", 7.99));
        repository.save(new Product("444", "Wódka", 39.99));
    }

    // 1. SKANOWANIE
    public String scanProduct(String barcode) {
        return repository.findById(barcode).map(product -> {
            currentCart.add(product);
            return "Zeskanowano: " + product.getName() + " (" + product.getPrice() + " PLN)";
        }).orElse("Błąd: Nie znaleziono produktu o kodzie " + barcode);
    }

    // 2. PODGLĄD KOSZYKA
    public List<Product> getCart() {
        return currentCart;
    }

    // 3. FINALIZACJA + RABATY + DRUKOWANIE PARAGONU
    public String checkout(String paymentMethod) {
        if (currentCart.isEmpty()) return "Koszyk jest pusty!";

        StringBuilder receipt = new StringBuilder();
        receipt.append("\n--- PARAGON FISKALNY ---\n");

        double total = 0;
        for (Product p : currentCart) {
            receipt.append(p.getName()).append("\t\t").append(p.getPrice()).append(" PLN\n");
            total += p.getPrice();
        }

        receipt.append("------------------------\n");
        receipt.append("SUMA CZĘŚCIOWA: ").append(String.format("%.2f", total)).append(" PLN\n");

        // Logika RABATU: Jeśli zakupy za ponad 50 zł -> 10% zniżki
        if (total > 50.00) {
            double discount = total * 0.10;
            total -= discount;
            receipt.append("RABAT (VIP > 50zł): -").append(String.format("%.2f", discount)).append(" PLN\n");
        }

        receipt.append("DO ZAPŁATY: ").append(String.format("%.2f", total)).append(" PLN\n");
        receipt.append("METODA PŁATNOŚCI: ").append(paymentMethod).append("\n");
        receipt.append("--- DZIĘKUJEMY ---\n");

        // Wyczyszczenie koszyka po zakupie
        currentCart.clear();

        return receipt.toString();
    }

    // 4. ZWROTY
    public String returnProduct(String barcode) {
        // Uproszczony zwrot - sprawdzamy czy produkt istnieje w bazie
        return repository.findById(barcode).map(product ->
                "DOKONANO ZWROTU: " + product.getName() + ". Oddaj klientowi: " + product.getPrice() + " PLN"
        ).orElse("Nie można zwrócić - brak produktu w bazie.");
    }
}