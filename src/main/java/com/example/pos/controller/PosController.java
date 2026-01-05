package com.example.pos.controller;

// TE IMPORTY SĄ KLUCZOWE:
import com.example.pos.model.ReceiptItem;
import com.example.pos.service.PosService; // <--- TEGO BRAKOWAŁO!
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PosController {

    private PosService posService;

    @FXML private TextField barcodeField;
    @FXML private TableView<ReceiptItem> cartTable;
    @FXML private TableColumn<ReceiptItem, String> colName;
    @FXML private TableColumn<ReceiptItem, Integer> colQty;
    @FXML private TableColumn<ReceiptItem, Double> colPrice;
    @FXML private TableColumn<ReceiptItem, Double> colTotal;
    @FXML private Label totalLabel;
    @FXML private TextArea receiptArea;

    // Metoda do "wstrzyknięcia" serwisu z Maina
    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    @FXML
    public void initialize() {
        // Powiązanie kolumn tabeli z polami klasy ReceiptItem
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    @FXML
    public void handleScan() {
        if (posService == null) return;

        String barcode = barcodeField.getText();
        ReceiptItem item = posService.scanProduct(barcode);

        if (item != null) {
            refreshView();
            barcodeField.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nie znaleziono produktu!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCheckout() {
        if (posService == null || posService.getCart().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Koszyk jest pusty!");
            alert.showAndWait();
            return;
        }

        String receipt = posService.checkout();
        receiptArea.setText(receipt);
        refreshView();
    }

    private void refreshView() {
        cartTable.getItems().setAll(posService.getCart());
        double sum = posService.getCart().stream().mapToDouble(ReceiptItem::getTotal).sum();
        totalLabel.setText(String.format("SUMA: %.2f PLN", sum));
    }
}