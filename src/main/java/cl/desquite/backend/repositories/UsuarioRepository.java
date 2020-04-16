package cl.desquite.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	/**
	 * 
	 * @param emailOrUser (Email o user del usuario)
	 * @return El usuario que coincida con el email o el user
	 */
	@Query("select u from Usuario u where u.email = ?1 or u.user = ?1")
	Usuario findByEmailOrUser(String emailOrUser);

	/**
	 * Retorna un {@link Page} de todos los usuarios que coinciden con la query
	 * 
	 * @param query    Texto a buscar (ejemplo: Ismael)
	 * @param pageable {@link PageRequest}
	 * @return Page&lt;Usuario&gt;
	 */
	@Query("select u from Usuario u where u.nombre like %?1% or u.apellidos like %?1% or u.email like %?1%")
	Page<Usuario> findAllPaginatedWithFilters(String query, Pageable pageable);

}
