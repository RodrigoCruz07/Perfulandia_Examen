package cl.duoc.rodrcruz.perfumeinventorypurchase.controller;

import cl.duoc.rodrcruz.perfumeinventorypurchase.service.PerfumeService;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.PerfumeRequest;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response.PerfumeResponse;
import cl.duoc.rodrcruz.perfumeinventorypurchase.model.Perfume;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeDB;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfumeControllerTest {
    @Mock
    private PerfumeService perfumeService;
    @InjectMocks
    private PerfumeController perfumeController;

    @Test
    void registerPerfume() {
        PerfumeRequest request = PerfumeRequest.builder().name("Aventus").brand("Creed").size(100).build();

        PerfumeDB newPerfume = new PerfumeDB();
        newPerfume.setId(1);
        newPerfume.setName("Aventus");
        newPerfume.setBrand("Creed");
        newPerfume.setSize(100);
        //simulas que pasa por capa de service(Definimos qué debe devolver el mock cuando se invoque.)
        when(perfumeService.registerPerfume(any(Perfume.class)))
                .thenReturn(newPerfume);

        ResponseEntity<PerfumeResponse> response =
                perfumeController.registerPerfume(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Aventus", response.getBody().getName());
        //Comprobamos que el controlador agrego correctamente.
        verify(perfumeService).registerPerfume(any(Perfume.class));
    }

    @Test
    void getPerfumeById() {
        PerfumeDB perfume = new PerfumeDB();
        perfume.setId(1);
        perfume.setName("Aventus");
        perfume.setBrand("Creed");
        perfume.setSize(100);


        when(perfumeService.getPerfume(1)).thenReturn(perfume);

        ResponseEntity<PerfumeResponse> response = perfumeController.getPerfumeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Aventus", response.getBody().getName());
        verify(perfumeService).getPerfume(1);
    }

    @Test
    void updatePerfume() {
        PerfumeRequest request = PerfumeRequest.builder()
                .name("Invictus")
                .brand("Paco Rabanne")
                .size(100)
                .build();
        //eq = igual a
        when(perfumeService.updatePerfume(eq(3), any(Perfume.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<PerfumeResponse> response = perfumeController.updatePerfume(3, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePerfume() {
        when(perfumeService.deletePerfume(1)).thenReturn(true);

        ResponseEntity<Void> response = perfumeController.deletePerfume(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(perfumeService).deletePerfume(1);
    }

    @Test
    void getAllPerfumes() {
        PerfumeDB perfume1 = new PerfumeDB();
        perfume1.setId(1);
        perfume1.setName("kako");
        perfume1.setBrand("brand1");
        perfume1.setSize(70);
        PerfumeDB perfume2 = new PerfumeDB();
        perfume2.setId(1);
        perfume2.setName("rodrigo");
        perfume2.setBrand("brand2");
        perfume2.setSize(90);
        PerfumeDB perfume3 = new PerfumeDB();
        perfume3.setId(1);
        perfume3.setName("Miguel");
        perfume3.setBrand("brand3");
        perfume3.setSize(30);


        List<PerfumeDB> perfumesDB = Arrays.asList(perfume1, perfume2,perfume3);

        when(perfumeService.getAllPerfumes()).thenReturn(perfumesDB);

        ResponseEntity<List<PerfumeResponse>> response = perfumeController.getAllPerfumes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size()); // Verifica si se devuelven dos elementos

        // Verifica
        verify(perfumeService).getAllPerfumes();
    }


}