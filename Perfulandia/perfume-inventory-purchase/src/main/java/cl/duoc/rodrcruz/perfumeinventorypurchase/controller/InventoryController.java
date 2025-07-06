package cl.duoc.rodrcruz.perfumeinventorypurchase.controller;

import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.InventoryDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.InventoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response.InventoryResponse;
import cl.duoc.rodrcruz.perfumeinventorypurchase.service.InventoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody InventoryRequest request) {
        InventoryDB saved = inventoryService.registerInventory(request);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Integer id) {
        InventoryDB found = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(mapToResponse(found));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventories() {
        List<InventoryDB> inventories = inventoryService.findAllInventories();
        List<InventoryResponse> responseList = inventories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Integer id) {
        boolean deleted = inventoryService.deleteInventory(id);
        if (deleted) {
            return ResponseEntity.ok("Inventario eliminado con éxito.");
        } else {
            return ResponseEntity.status(404).body("Inventario no encontrado.");
        }
    }

    private InventoryResponse mapToResponse(InventoryDB db) {
        return new InventoryResponse(
                db.getId(),
                db.getPerfume().getName(),
                db.getQuantity(),
                db.getPrice(),
                db.getLocation(),
                db.getDate()
        );
    }
}
