package cl.duoc.rodrcruz.usersellermodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleJpaRepository extends JpaRepository<RoleDB, Integer> {
    Optional<RoleDB> findByName(String name);

}
