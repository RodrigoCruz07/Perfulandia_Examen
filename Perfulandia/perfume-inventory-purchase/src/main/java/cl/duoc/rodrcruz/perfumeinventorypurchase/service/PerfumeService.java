package cl.duoc.rodrcruz.perfumeinventorypurchase.service;

import cl.duoc.rodrcruz.perfumeinventorypurchase.model.Perfume;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeDB;
import cl.duoc.rodrcruz.perfumeinventorypurchase.repository.PerfumeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfumeService {
    @Autowired
    private PerfumeJpaRepository perfumeJpaRepository;


    public PerfumeDB getPerfume(Integer id) {
        return perfumeJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfume not found"));
    }
    public PerfumeDB registerPerfume(Perfume perfume) {
        Optional<PerfumeDB> found = perfumeJpaRepository.findByNameAndBrand(perfume.getName(), perfume.getBrand());
        if (found.isPresent()) {
            return found.get();
        }
        PerfumeDB NewPerfumeDB = new PerfumeDB();
        NewPerfumeDB.setName(perfume.getName());
        NewPerfumeDB.setBrand(perfume.getBrand());
        NewPerfumeDB.setSize(perfume.getSize());
        return perfumeJpaRepository.save(NewPerfumeDB);

    }

    public PerfumeDB updatePerfume(Integer id, Perfume UpPerfume) {
        PerfumeDB found = perfumeJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfume not found"));
        if (UpPerfume.getName() != null && UpPerfume.getName().isEmpty()) {
            found.setName(UpPerfume.getName());
        }
        if (UpPerfume.getBrand() != null && UpPerfume.getBrand().isEmpty()) {
            found.setBrand(UpPerfume.getBrand());
        }
        if(UpPerfume.getSize()>0 ){
            found.setSize(UpPerfume.getSize());
        }
        return perfumeJpaRepository.save(found);
    }

    public boolean deletePerfume(Integer id) {
        Optional<PerfumeDB> perfumeDB = perfumeJpaRepository.findById(id);
        if (perfumeDB.isPresent()) {
            perfumeJpaRepository.delete(perfumeDB.get());
            return true;
        }
        return false;
    }
    public List<PerfumeDB> getAllPerfumes() {
        return perfumeJpaRepository.findAll();
    }


}


