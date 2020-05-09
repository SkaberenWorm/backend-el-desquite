package cl.desquite.backend.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cl.desquite.backend.entities.Producto;
import cl.desquite.backend.util.ResultadoProc;

public interface IProductoService {

	/**
	 * Busca un producto por su ID.
	 * 
	 * @param productoId (Identificador del producto)
	 * @return ResultadoProc&lt;Producto&gt; Producto con el ID dado
	 */
	ResultadoProc<Producto> findById(int productoId);

	/**
	 * Busca un producto por su ID y que se encuentre activo.
	 * 
	 * @param productoId (Identificador del producto)
	 * @return ResultadoProc&lt;Producto&gt; Producto activo con el ID dado
	 */
	ResultadoProc<Producto> findByIdAndActivoTrue(int productoId);

	/**
	 * Guarda un producto
	 * 
	 * @param producto {@link Producto} que se desea guardar
	 * @return ResultadoProc&lt;Producto&gt; Producto guardado
	 */
	ResultadoProc<Producto> save(Producto producto);

	/**
	 * Retorna un {@link Page} de todos los productos registrados
	 * 
	 * @param pageable Entidad {@link PageRequest} que contiene los datos de la
	 *                 paginación
	 * @return ResultadoProc&lt;Page&lt;Producto&gt;&gt; Una página de productos
	 */
	ResultadoProc<Page<Producto>> findAllPaginated(PageRequest pageable);

	/**
	 * Retorna un {@link Page} de todos los productos que coinciden con los filtros
	 * 
	 * @param pageable     Entidad {@link PageRequest} que contiene los datos de la
	 *                     paginación
	 * @param buscador     Texto usado para filtrar por el nombre y descripcion del
	 *                     producto
	 * @param categoriasId Listado de los ID de las categorías, si es null solo te
	 *                     filtrará por el buscador
	 * @return ResultadoProc&lt;Page&lt;Producto&gt;&gt; Una página de productos
	 *         coincidentes con los filtros
	 */
	ResultadoProc<Page<Producto>> findAllPaginatedWithFilters(PageRequest pageable, String buscador,
			List<Integer> categoriasId);

	/**
	 * Cambia el estado del producto <br>
	 * Si activo es <code><b>true</b></code> lo cambia a <code><b>false</b></code>
	 * <br>
	 * Si activo es <code><b>false</b></code> lo cambia a <code><b>true</b></code>
	 * <br>
	 * 
	 * @param productoId (Id del producto)
	 * @return ResultadoProc&lt;Producto&gt; El producto al que le fue cambiado el
	 *         estado
	 */
	ResultadoProc<Producto> changeState(int productoId);

	/**
	 * Actualiza un producto
	 * 
	 * @param producto {@link Producto} que se desea actualizar
	 * @return ResultadoProc&lt;Producto&gt; Producto actualizado
	 */
	ResultadoProc<Producto> update(Producto productoParam);

}
