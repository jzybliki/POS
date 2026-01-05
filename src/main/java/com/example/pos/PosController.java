package com.example.pos;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pos")
public class PosController {

    private final PosService posService;

    public PosController(PosService posService) {
        this.posService = posService;
    }

    // Skanowanie: np. /pos/scan/111
    @PostMapping("/scan/{barcode}")
    public String scan(@PathVariable String barcode) {
        return posService.scanProduct(barcode);
    }

    // Podgląd koszyka
    @GetMapping("/cart")
    public List<Product> viewCart() {
        return posService.getCart();
    }

    // Płatność i Paragon: np. /pos/pay?method=KARTA
    @PostMapping("/pay")
    public String pay(@RequestParam(defaultValue = "GOTÓWKA") String method) {
        return posService.checkout(method);
    }

    // Zwrot: np. /pos/return/111
    @PostMapping("/return/{barcode}")
    public String returnItem(@PathVariable String barcode) {
        return posService.returnProduct(barcode);
    }
}