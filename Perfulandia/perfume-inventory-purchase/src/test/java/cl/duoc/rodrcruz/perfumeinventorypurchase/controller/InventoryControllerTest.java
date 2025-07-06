package cl.duoc.rodrcruz.perfumeinventorypurchase.controller;

import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.InventoryRequest;

import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response.InventoryResponse;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.InventoryDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @Test
    void createInventory_success() {
        InventoryRequest request = new InventoryRequest();
        request.setPerfumeId(10);
        request.setQuantity(5);
        request.setPrice(12000.5);
        request.setLocation("Santiago");

        PerfumeDB perfumeDB = new PerfumeDB();
        perfumeDB.setId(10);
        perfumeDB.setName("Bleu de Chanel");
        perfumeDB.setBrand("Chanel");
        perfumeDB.setSize(100);

        InventoryDB savedInventory = new InventoryDB();
        savedInventory.setId(1);
        savedInventory.setPerfume(perfumeDB);
        savedInventory.setQuantity(request.getQuantity());
        savedInventory.setPrice(request.getPrice());
        savedInventory.setLocation(request.getLocation());
        savedInventory.setDate(LocalDate.now());

        when(inventoryService.registerInventory(request)).thenReturn(savedInventory);

        ResponseEntity<InventoryResponse> response = inventoryController.createInventory(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        InventoryResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(savedInventory.getId(), body.getId());
        assertEquals(perfumeDB.getName(), body.getPerfumeName());
        assertEquals(savedInventory.getQuantity(), body.getQuantity());
        assertEquals(savedInventory.getPrice(), body.getPrice());
        assertEquals(savedInventory.getLocation(), body.getLocation());
        assertNotNull(body.getDate());

        verify(inventoryService, times(1)).registerInventory(request);
    }

    @Test
    void getInventoryById_success() {
        int id = 1;

        PerfumeDB perfumeDB = new PerfumeDB();
        perfumeDB.setName("Cool Water");
        perfumeDB.setBrand("Davidoff");
        perfumeDB.setSize(75);

        InventoryDB inventoryDB = new InventoryDB();
        inventoryDB.setId(id);
        inventoryDB.setPerfume(perfumeDB);
        inventoryDB.setQuantity(8);
        inventoryDB.setPrice(8000.0);
        inventoryDB.setLocation("Valparaiso");
        inventoryDB.setDate(LocalDate.now());

        when(inventoryService.getInventoryById(id)).thenReturn(inventoryDB);

        ResponseEntity<InventoryResponse> response = inventoryController.getInventoryById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        InventoryResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(inventoryDB.getId(), body.getId());
        assertEquals(perfumeDB.getName(), body.getPerfumeName());

        verify(inventoryService, times(1)).getInventoryById(id);
    }

    @Test
    void getAllInventories_success() {
        PerfumeDB perfume1 = new PerfumeDB();
        perfume1.setName("Light Blue");
        perfume1.setBrand("Dolce & Gabbana");
        perfume1.setSize(50);

        PerfumeDB perfume2 = new PerfumeDB();
        perfume2.setName("Acqua Di Gio");
        perfume2.setBrand("Giorgio Armani");
        perfume2.setSize(100);

        InventoryDB inv1 = new InventoryDB();
        inv1.setId(1);
        inv1.setPerfume(perfume1);
        inv1.setQuantity(15);
        inv1.setPrice(9000);
        inv1.setLocation("Concepcion");
        inv1.setDate(LocalDate.now());

        InventoryDB inv2 = new InventoryDB();
        inv2.setId(2);
        inv2.setPerfume(perfume2);
        inv2.setQuantity(10);
        inv2.setPrice(15000);
        inv2.setLocation("La Serena");
        inv2.setDate(LocalDate.now());

        List<InventoryDB> mockList = Arrays.asList(inv1, inv2);

        when(inventoryService.findAllInventories()).thenReturn(mockList);

        ResponseEntity<List<InventoryResponse>> response = inventoryController.getAllInventories();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        verify(inventoryService, times(1)).findAllInventories();
    }

    @Test
    void deleteInventory_success() {
        int id = 1;

        when(inventoryService.deleteInventory(id)).thenReturn(true);

        ResponseEntity<String> response = inventoryController.deleteInventory(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Inventario eliminado con éxito.", response.getBody());

        verify(inventoryService, times(1)).deleteInventory(id);
    }

    @Test
    void deleteInventory_notFound() {
        int id = 1;

        when(inventoryService.deleteInventory(id)).thenReturn(false);

        ResponseEntity<String> response = inventoryController.deleteInventory(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Inventario no encontrado.", response.getBody());

        verify(inventoryService, times(1)).deleteInventory(id);
    }
}
