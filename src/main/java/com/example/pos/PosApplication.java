package com.example.pos;

import com.example.pos.controller.PosController;
import com.example.pos.repository.ProductRepository;
import com.example.pos.service.PosService;
import com.example.pos.strategy.*; // Import wszystkich strategii
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PosApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // --- 1. Ręczne Wstrzykiwanie Zależności (Dependency Injection) ---

        ProductRepository repository = new ProductRepository();

        // Domyślna strategia na start (musi być identyczna jak "Brak dodatkowego" w kontrolerze)
        // Łańcuch: VIP -> 3za2 -> Koniec (NoDiscount)
        DiscountStrategy defaultStrategy = new VipDiscountStrategy(
                new ThreeForTwoStrategy(
                        new NoDiscountStrategy()
                )
        );

        // Tworzymy Serwis
        PosService service = new PosService(repository, defaultStrategy);

        // --- 2. Ładowanie Widoku FXML ---
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pos_view.fxml"));
        Parent root = loader.load();

        // --- 3. Konfiguracja Kontrolera ---
        PosController controller = loader.getController();
        controller.setPosService(service);

        // --- 4. Wyświetlenie Okna ---
        primaryStage.setTitle("System POS (Projekt Zaliczeniowy)");
        primaryStage.setScene(new Scene(root, 1100, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}