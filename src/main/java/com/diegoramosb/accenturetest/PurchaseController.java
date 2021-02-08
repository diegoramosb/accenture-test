package com.diegoramosb.accenturetest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {

    private final PurchaseRepository repository;

    public PurchaseController(PurchaseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/purchases")
    public List<Purchase> getAll() {
        return repository.findAll();
    }

    @PostMapping("/purchases")
    public ResponseEntity<Purchase> newPurchase(@RequestBody Purchase newPurchase) {
        newPurchase.setProductCost(PurchaseLogic.calculateProductCost(newPurchase.getProducts()));
        if (PurchaseLogic.validateCost(newPurchase.getProductCost())) {
            newPurchase.setTimestamp(PurchaseLogic.getTimestamp());
            newPurchase.setIva(PurchaseLogic.calculateIva(newPurchase.getProducts()));
            newPurchase.setDeliveryCost(PurchaseLogic.calculateDeliveryCost(newPurchase.getProducts()));
            newPurchase.setTotal(PurchaseLogic.calculateTotalCost(newPurchase));
            return new ResponseEntity<>(repository.save(newPurchase), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/purchases/{id}")
    public ResponseEntity<Purchase> editPurchase(@RequestBody Purchase newPurchase, @PathVariable Long id) {
        Optional<Purchase> oldPurchase = repository.findById(id);
        if (oldPurchase.isPresent()) {
            newPurchase.setId(id);
            newPurchase.setProductCost(PurchaseLogic.calculateProductCost(newPurchase.getProducts()));
            newPurchase.setTimestamp(PurchaseLogic.getTimestamp());
            newPurchase.setIva(PurchaseLogic.calculateIva(newPurchase.getProducts()));
            newPurchase.setDeliveryCost(PurchaseLogic.calculateDeliveryCost(newPurchase.getProducts()));
            newPurchase.setTotal(PurchaseLogic.calculateTotalCost(newPurchase));
            if (PurchaseLogic.validateEdit(oldPurchase.get(), newPurchase)) {
                return new ResponseEntity<>(repository.save(newPurchase), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<String> letePurchase(@PathVariable Long id) {
        Optional<Purchase> purchase = repository.findById(id);
        if (PurchaseLogic.validateDelete(purchase.get())) {
            repository.deleteById(id);
            return new ResponseEntity<>("Purchase cancelled", HttpStatus.OK);
        } else {
            repository.deleteById(id);
            return new ResponseEntity<>(
                    "Purchase cancelled after 12 hours. You will be charged " + purchase.get().getTotal() * 0.1,
                    HttpStatus.OK);
        }
    }
}
