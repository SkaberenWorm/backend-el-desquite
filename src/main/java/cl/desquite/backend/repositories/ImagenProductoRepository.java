package cl.desquite.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.ImagenProducto;
import cl.desquite.backend.entities.Producto;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Integer> {

	/**
	 * Lista las imágenes de un producto
	 * 
	 * @param producto Entidad {@link Producto}
	 * @return List&gl;ImagenProducto&gt; Listado de imágenes del producto dado
	 */
	List<ImagenProducto> findAllByProducto(Producto producto);
}
