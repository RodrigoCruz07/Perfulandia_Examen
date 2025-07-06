package cl.duoc.rodrcruz.usersellermodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserDB, Integer> {
    List<UserDB> findByNameOrEmail(String username,String email);
}
