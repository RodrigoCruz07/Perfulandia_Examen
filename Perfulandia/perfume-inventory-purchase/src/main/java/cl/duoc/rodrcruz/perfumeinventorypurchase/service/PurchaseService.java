package cl.duoc.rodrcruz.perfumeinventorypurchase.service;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.*;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.PurchaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseJpaRepository purchaseJpaRepository;

    @Autowired
    private PerfumeJpaRepository perfumeJpaRepository;
    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;
    public PurchaseDB registerPurchase(PurchaseRequest request) {
        PerfumeDB perfume = perfumeJpaRepository.findById(request.getPerfumeid())
                .orElseThrow(() -> new RuntimeException("Perfume no encontrado con ID: " + request.getPerfumeid()));
        InventoryDB inventory = inventoryJpaRepository.findByPerfume_Id(request.getPerfumeid())
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el perfume con ID: " + request.getPerfumeid()));
        if (inventory.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente. Stock disponible: " + inventory.getQuantity());
        }
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventoryJpaRepository.save(inventory);
        PurchaseDB purchase = new PurchaseDB();
        purchase.setUserid(request.getUserid());
        purchase.setSellerid(request.getSellerid());
        purchase.setPerfume(perfume);
        purchase.setPerfumename(perfume.getName());
        purchase.setQuantity(request.getQuantity());
        purchase.setPrice(request.getPrice());
        purchase.setPurchasedate(LocalDateTime.now());



        return purchaseJpaRepository.save(purchase);
    }

    public PurchaseDB getPurchaseById(Integer id) {
        return purchaseJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
    }

    public List<PurchaseDB> findAllPurchases() {
        return purchaseJpaRepository.findAll();
    }

    public boolean deletePurchase(Integer id) {
        PurchaseDB purchase = purchaseJpaRepository.findById(id).orElse(null);
        if (purchase != null) {
            purchaseJpaRepository.delete(purchase);
            return true;
        }
        return false;
    }
}
