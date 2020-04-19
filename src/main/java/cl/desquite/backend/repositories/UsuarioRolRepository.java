package cl.desquite.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioRole;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRole, Integer> {

	@Transactional
	void deleteAllByUsuario(Usuario usuario);

}
