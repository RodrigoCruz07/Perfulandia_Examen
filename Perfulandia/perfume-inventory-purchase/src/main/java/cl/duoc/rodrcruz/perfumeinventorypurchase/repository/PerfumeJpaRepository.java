package cl.duoc.rodrcruz.perfumeinventorypurchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfumeJpaRepository extends JpaRepository<PerfumeDB, Integer> {
    Optional<PerfumeDB> findByNameAndBrand(String name, String brand);
}
