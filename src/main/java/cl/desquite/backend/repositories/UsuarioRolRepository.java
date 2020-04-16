package cl.desquite.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.desquite.backend.entities.UsuarioRole;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRole, Integer> {

}
