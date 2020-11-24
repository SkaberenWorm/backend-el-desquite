package cl.desquite.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cl.desquite.backend.entities.Categoria;
import cl.desquite.backend.utils.ResultadoProc;

public interface ICategoriaService {

	/**
	 * Busca una categoría por su ID.
	 * 
	 * @param categoriaId (Identificador de la categoría)
	 * @return ResultadoProc&lt;Categoria&gt; Categoría con el ID dado
	 */
	ResultadoProc<Categoria> findById(int categoriaId);

	/**
	 * Busca una categoria por su ID y que se encuentre activa
	 * 
	 * @param categoriaId (Identificador de la categoría)
	 * @return ResultadoProc&lt;Categoria&gt; Categoría activa con el ID dado
	 */
	ResultadoProc<Categoria> findByIdAndActivoTrue(int categoriaId);

	/**
	 * Guarda o actualiza una categoría
	 * 
	 * @param categoria {@link Categoria}
	 * @return ResultadoProc&lt;Categoria&gt; Categoria guardada/actualizada
	 */
	ResultadoProc<Categoria> save(Categoria categoria);

	/**
	 * Retorna un {@link Page} de todas las categorías que coinciden con los la
	 * query
	 * 
	 * @param pageable Entidad {@link PageRequest} que contiene los datos de la
	 *                 paginación
	 * @param buscador Texto usado para filtrar por la descripcion de la categoria
	 * @return ResultadoProc&lt;Page&lt;Categoria&gt;&gt; Una página de categorias
	 *         coincidentes con los filtros
	 */
	ResultadoProc<Page<Categoria>> findAllPaginatedWithSearch(PageRequest pageable, String buscador);

}
