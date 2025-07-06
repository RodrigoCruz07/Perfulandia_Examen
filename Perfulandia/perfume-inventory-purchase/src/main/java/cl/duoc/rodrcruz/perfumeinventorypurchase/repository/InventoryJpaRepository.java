package cl.duoc.rodrcruz.perfumeinventorypurchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryDB, Integer> {
    Optional<InventoryDB> findByPerfume_Id(Integer perfumeId);
}
