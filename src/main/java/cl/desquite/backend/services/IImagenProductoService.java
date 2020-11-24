package cl.desquite.backend.services;

import java.util.List;

import cl.desquite.backend.entities.ImagenProducto;
import cl.desquite.backend.entities.Producto;
import cl.desquite.backend.utils.ResultadoProc;

public interface IImagenProductoService {

	/**
	 * Guarda un listado de imágenes
	 * 
	 * @param imagenes listado de {@link ImagenProducto}
	 * @return ResultadoProc&lt;List&lt;ImagenProducto&gt;&gt; Imágenes guardadas
	 */
	ResultadoProc<List<ImagenProducto>> saveAll(List<ImagenProducto> imagenes);

	/**
	 * Compara las imágenes a actualizar con las que el producto atualmente tiene
	 * <br>
	 * <ul>
	 * <li>Si los listados tienen las mismas imágenes, se retornará
	 * <code>TRUE</code></li>
	 * <li>Si el listado de imagenesNew no coincide con las imágenes del listado
	 * imagenesOld, se compararán y se realizaran los cambios correspondientes,
	 * manteniendo las imágenes que coinciden y agregando o eliminando las nuevas
	 * imágenes que no coinciden</li>
	 *
	 * </ul>
	 * 
	 * @param imagenesNew listado de {@link ImagenProducto} a actualizar
	 * @param imagenesOld listado de {@link ImagenProducto} que tiene actualmente el
	 *                    producto
	 * @return ResultadoProc&lt;Boolean&gt; <code>True</code> si se actualizaron las
	 *         imágenes
	 */
	ResultadoProc<Boolean> compareChangesAndSave(List<ImagenProducto> imagenesNew, List<ImagenProducto> imagenesOld);

	/**
	 * Elimina un listado de imágenes
	 * 
	 * @param imagenes Listado de {@link ImagenProducto} a eliminar
	 * @return ResultadoProc&lt;Boolean&gt; <code>True</code> si se eliminaron las
	 *         imágenes
	 */
	ResultadoProc<Boolean> deleteAll(List<ImagenProducto> imagenes);

	/**
	 * Lista las imágenes de un producto
	 * 
	 * @param producto Entidad {@link Producto}
	 * @return ResultadoProc&lt;List&lt;ImagenProducto&gt;&gt; Imágenes del producto
	 *         dado
	 */
	ResultadoProc<List<ImagenProducto>> findAllByProducto(Producto producto);

}
