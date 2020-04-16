package cl.desquite.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

	/**
	 * Retorna un {@link Page} de todos los productos que coinciden con los filtros.
	 * 
	 * @param query        Consulta a buscar (ejemplo: "completo")
	 * @param categoriasId Listado de categorías
	 * @param pageable     {@link PageRequest}
	 * @return Page&lt;Producto&gt;
	 */
	@Query("select p from Producto p where p.descripcion like %?1% and p.categoria.id in ?2")
	Page<Producto> findAllByQueryAndCategories(String query, List<Integer> categoriasId, Pageable pageable);

	/**
	 * Retorna un {@link Page} de todos los productos que coinciden con al query
	 * 
	 * @param query    Consulta a buscar (ejemplo: "completo")
	 * @param pageable {@link PageRequest}
	 * @return Page&lt;Producto&gt;
	 */
	@Query("select p from Producto p where p.descripcion like %?1%")
	Page<Producto> findAllByQuery(String query, Pageable pageable);

}
