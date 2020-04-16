package cl.desquite.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	@Query("select c from Categoria c where c.descripcion like %?1%")
	Page<Categoria> findAllByQuery(String buscador, PageRequest pageable);

}
