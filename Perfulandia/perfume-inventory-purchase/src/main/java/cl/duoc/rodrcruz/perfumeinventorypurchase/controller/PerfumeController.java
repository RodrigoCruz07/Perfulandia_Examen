package cl.duoc.rodrcruz.perfumeinventorypurchase.controller;

import cl.duoc.rodrcruz.perfumeinventorypurchase.service.PerfumeService;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request.PerfumeRequest;
import cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response.PerfumeResponse;
import cl.duoc.rodrcruz.perfumeinventorypurchase.model.Perfume;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/perfumes")
public class PerfumeController {
    @Autowired
    private PerfumeService perfumeService;

    @PostMapping

    public ResponseEntity<PerfumeResponse> registerPerfume(@RequestBody PerfumeRequest request) {
        Perfume perfume = convertRequest(request);
        PerfumeDB NewPerfumeDB = perfumeService.registerPerfume(perfume);
        PerfumeResponse perfumeResponse = convertResponse(NewPerfumeDB);
        return new ResponseEntity<>(perfumeResponse, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")

    public ResponseEntity<PerfumeResponse> getPerfumeById(@PathVariable Integer id) {
        try {
            PerfumeDB perfumeDB = perfumeService.getPerfume(id);
            PerfumeResponse response = convertResponse(perfumeDB);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{id}")

    public ResponseEntity<PerfumeResponse> updatePerfume(@PathVariable Integer id, @RequestBody PerfumeRequest request) {
        try {
            Perfume perfumeModel = convertRequest(request);
            PerfumeDB updatedPerfumeDB = perfumeService.updatePerfume(id, perfumeModel);
            PerfumeResponse response = convertResponse(updatedPerfumeDB);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deletePerfume(@PathVariable Integer id) {
        boolean deleted = perfumeService.deletePerfume(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping

    public ResponseEntity<List<PerfumeResponse>> getAllPerfumes() {
        List<PerfumeDB> perfumesDB = perfumeService.getAllPerfumes();
        List<PerfumeResponse> responses = perfumesDB.stream()
                .map(this::convertResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    private Perfume convertRequest(PerfumeRequest request) {
        return Perfume.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .size(request.getSize() != null ? request.getSize() : 0)
                .build();
    }

    private PerfumeResponse convertResponse(PerfumeDB perfumeDB) {
        return PerfumeResponse.builder()
                .id(perfumeDB.getId())
                .name(perfumeDB.getName())
                .brand(perfumeDB.getBrand())
                .size(perfumeDB.getSize())
                .build();
    }




}
