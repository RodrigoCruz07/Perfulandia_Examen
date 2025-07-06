package cl.duoc.rodrcruz.perfumeinventorypurchase.service;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.InventoryDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.InventoryJpaRepository;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeJpaRepository;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.InventoryRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private PerfumeJpaRepository perfumeJpaRepository;

    public InventoryDB registerInventory(InventoryRequest request) {
        PerfumeDB perfume = perfumeJpaRepository.findById(request.getPerfumeId())
                .orElseThrow(() -> new EntityNotFoundException("Perfume no encontrado con ID: " + request.getPerfumeId()));

        InventoryDB inventory = new InventoryDB();
        inventory.setPerfume(perfume);
        inventory.setQuantity(request.getQuantity());
        inventory.setPrice(request.getPrice());
        inventory.setLocation(request.getLocation());
        inventory.setDate(LocalDate.now());

        return inventoryJpaRepository.save(inventory);
    }

    public InventoryDB getInventoryById(Integer id) {
        return inventoryJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
    }

    public List<InventoryDB> findAllInventories() {
        return inventoryJpaRepository.findAll();
    }

    public boolean deleteInventory(Integer id) {
        InventoryDB found = inventoryJpaRepository.findById(id).orElse(null);
        if (found != null) {
            inventoryJpaRepository.delete(found);
            return true;
        }
        return false;
    }
}
