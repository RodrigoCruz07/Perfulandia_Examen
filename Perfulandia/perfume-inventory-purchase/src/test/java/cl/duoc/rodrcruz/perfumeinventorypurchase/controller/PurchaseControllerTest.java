package cl.duoc.rodrcruz.perfumeinventorypurchase.controller;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.PurchaseRequest;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response.PurchaseResponse;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PurchaseDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    private PurchaseDB testPurchaseDB;
    private PurchaseRequest testRequest;
    private PurchaseResponse testResponse;

    @BeforeEach
    void setUp() {
        testPurchaseDB = new PurchaseDB();
        testPurchaseDB.setId(1);
        testPurchaseDB.setUserid(1);
        testPurchaseDB.setSellerid(1);
        testPurchaseDB.setPerfumename("Chanel N°5");
        testPurchaseDB.setQuantity(2);
        testPurchaseDB.setPrice(120000);
        testPurchaseDB.setPurchasedate(LocalDateTime.now());

        testRequest = new PurchaseRequest();
        testRequest.setUserid(1);
        testRequest.setSellerid(1);
        testRequest.setPerfumeid(1);
        testRequest.setQuantity(2);
        testRequest.setPrice(120000);

        testResponse = new PurchaseResponse(
                1,
                3,
                2,
                "Chanel N°5",
                2,
                120000.0,
                LocalDateTime.now()
        );
    }

    @Test
    void createPurchase_ShouldReturnCreatedPurchase() {
        // Arrange
        when(purchaseService.registerPurchase(any(PurchaseRequest.class))).thenReturn(testPurchaseDB);

        // Act
        ResponseEntity<PurchaseResponse> response = purchaseController.createPurchase(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testPurchaseDB.getId(), response.getBody().getId());
        verify(purchaseService).registerPurchase(testRequest);
    }

    @Test
    void getPurchaseById_ShouldReturnPurchaseWhenExists() {
        // Arrange
        when(purchaseService.getPurchaseById(1)).thenReturn(testPurchaseDB);

        // Act
        ResponseEntity<PurchaseResponse> response = purchaseController.getPurchaseById(1);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testPurchaseDB.getId(), response.getBody().getId());
        verify(purchaseService).getPurchaseById(1);
    }

    @Test
    void deletePurchase_ShouldReturnOkWhenDeleted() {
        // Arrange
        when(purchaseService.deletePurchase(1)).thenReturn(true);

        // Act
        ResponseEntity<String> response = purchaseController.deletePurchase(1);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Compra eliminada con éxito.", response.getBody());
        verify(purchaseService).deletePurchase(1);
    }

    @Test
    void deletePurchase_ShouldReturnNotFoundWhenNotExists() {
        when(purchaseService.deletePurchase(1)).thenReturn(false);

        ResponseEntity<String> response = purchaseController.deletePurchase(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Compra no encontrada.", response.getBody());
        verify(purchaseService).deletePurchase(1);
    }

    @Test
    void getAllPurchases_ShouldReturnAllPurchases() {

        List<PurchaseDB> purchases = Arrays.asList(testPurchaseDB);
        when(purchaseService.findAllPurchases()).thenReturn(purchases);


        ResponseEntity<List<PurchaseResponse>> response = purchaseController.getAllPurchases();


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testPurchaseDB.getId(), response.getBody().getFirst().getId());
        verify(purchaseService).findAllPurchases();
    }
}