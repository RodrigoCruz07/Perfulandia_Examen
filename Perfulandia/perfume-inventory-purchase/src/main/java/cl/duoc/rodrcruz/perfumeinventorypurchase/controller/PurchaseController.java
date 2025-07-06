package cl.duoc.rodrcruz.perfumeinventorypurchase.controller;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PurchaseDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.PurchaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response.PurchaseResponse;
import cl.duoc.rodrcruz.perfumeinventorypurchase.service.PurchaseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;


    // Crear nueva compra
    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody PurchaseRequest request) {
        PurchaseDB saved = purchaseService.registerPurchase(request);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    // Obtener compra por ID
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Integer id) {
        PurchaseDB found = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(mapToResponse(found));
    }

    // Eliminar compra
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable Integer id) {
        boolean deleted = purchaseService.deletePurchase(id);
        if (deleted) {
            return ResponseEntity.ok("Compra eliminada con éxito.");
        } else {
            return ResponseEntity.status(404).body("Compra no encontrada.");
        }
    }

    // Listar todas las compras
    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        List<PurchaseDB> purchases = purchaseService.findAllPurchases();
        List<PurchaseResponse> responseList = purchases.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    //
    private PurchaseResponse mapToResponse(PurchaseDB db) {
        return new PurchaseResponse(
                db.getId(),
                db.getUserid(),
                db.getSellerid(),
                db.getPerfumename(),
                db.getQuantity(),
                db.getPrice(),
                db.getPurchasedate()

        );
    }
}

