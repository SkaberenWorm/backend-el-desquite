package cl.desquite.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.UsuarioToken;

@Repository
public interface UsuarioTokenRepository extends JpaRepository<UsuarioToken, Integer> {

    UsuarioToken findByToken(String token);
}
