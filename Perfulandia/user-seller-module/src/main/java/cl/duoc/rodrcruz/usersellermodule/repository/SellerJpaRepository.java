package cl.duoc.rodrcruz.usersellermodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerJpaRepository extends JpaRepository<SellerDB, Integer> {
    Optional<SellerDB> findByEmail(String email);

    Boolean existsByEmail(String email);
}
