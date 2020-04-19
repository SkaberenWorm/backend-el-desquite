package cl.desquite.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.desquite.backend.entities.ImagenProducto;
import cl.desquite.backend.entities.Producto;
import cl.desquite.backend.repositories.ProductoRepository;
import cl.desquite.backend.services.IImagenProductoService;
import cl.desquite.backend.services.IProductoService;
import cl.desquite.backend.util.ResultadoProc;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ProductoService implements IProductoService {

	@Autowired
	ProductoRepository productoRepository;
	@Autowired
	IImagenProductoService imagenProductoService;

	@Override
	public ResultadoProc<Producto> findById(int productoId) {
		ResultadoProc<Producto> salida = new ResultadoProc<Producto>();
		try {
			Producto producto = productoRepository.findById(productoId).orElse(null);
			if (producto == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado el producto con el código " + productoId);
			}
			salida.setResultado(producto);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje(
					"Se produjo un error inesperado al intentar obtener el producto con el código " + productoId);
		}
		return salida;
	}

	@Override
	public ResultadoProc<Producto> findByIdAndActivoTrue(int productoId) {
		ResultadoProc<Producto> salida = new ResultadoProc<Producto>();
		try {
			Producto producto = productoRepository.findById(productoId).orElse(null);
			salida.setResultado(producto);

			if (producto == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado el producto con el código " + productoId);
			}
			if (!producto.isActivo()) {
				salida.setError(true);
				salida.setMensaje("El producto con el código " + productoId + " se encuentra inactivo");
				salida.setResultado(null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje(
					"Se produjo un error inesperado al intentar obtener el producto con el código " + productoId);
		}
		return salida;
	}

	@Override
	public ResultadoProc<Page<Producto>> findAllPaginated(PageRequest pageable) {
		ResultadoProc<Page<Producto>> salida = new ResultadoProc<Page<Producto>>();
		try {
			Page<Producto> productos = productoRepository.findAll(pageable);
			salida.setResultado(productos);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje("Se produjo un error inesperado al intentar listar los productos");
		}
		return salida;
	}

	@Override
	public ResultadoProc<Page<Producto>> findAllPaginatedWithFilters(PageRequest pageable, String buscador,
			List<Integer> categoriasId) {
		ResultadoProc<Page<Producto>> salida = new ResultadoProc<Page<Producto>>();
		try {
			Page<Producto> productos;
			if (!(categoriasId != null && categoriasId.size() > 0)) {
				productos = productoRepository.findAllByQuery(buscador, pageable);
			} else {
				productos = productoRepository.findAllByQueryAndCategories(buscador, categoriasId, pageable);
			}
			salida.setResultado(productos);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje("Se produjo un error inesperado al intentar listar los productos");
		}
		return salida;
	}

	@Override
	public ResultadoProc<Producto> save(Producto productoParam) {
		ResultadoProc<Producto> salida = new ResultadoProc<Producto>();
		try {
			String mensaje = "";
			if (productoParam.getId() == 0) {
				mensaje = "Producto registrado correctamente";

				Producto producto = productoRepository.save(productoParam);
				producto.setImagenes(this.asignarProducto(producto.getImagenes(), producto));
				ResultadoProc<List<ImagenProducto>> imagenes = imagenProductoService.saveAll(producto.getImagenes());
				if (imagenes.isError()) {
					salida.setError(true);
					salida.setMensaje(imagenes.getMensaje());
					return salida;
				}

				producto.setImagenes(imagenes.getResultado());
				salida.setResultado(producto);
				salida.setMensaje(mensaje);
				salida.setResultado(producto);
			} else {
				salida.setError(true);
				salida.setMensaje("Se está intentando actualizar un producto, esta acción no esta permitida");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = productoParam.getId() > 0 ? "actualizar" : "registrar";
			salida.setMensaje("Se produjo un error inesperado al intentar " + accion + " el producto");
		}
		return salida;
	}

	@Transactional
	@Override
	public ResultadoProc<Producto> update(Producto producto) {
		ResultadoProc<Producto> salida = new ResultadoProc<Producto>();
		try {
			String mensaje = "";
			if (producto.getId() > 0) {
				mensaje = "Producto actualizado correctamente";

				// Obtenemos las imágenes actuales del producto para compararlas con las nuevas
				ResultadoProc<List<ImagenProducto>> imagenesOld = imagenProductoService.findAllByProducto(producto);
				if (imagenesOld.isError()) {
					salida.setError(true);
					salida.setMensaje(imagenesOld.getMensaje());
					return salida;
				}

				// Agregamos la referencia del producto en la imágen
				producto.setImagenes(this.asignarProducto(producto.getImagenes(), new Producto(producto.getId())));

				// Actualizamos imágenes si es necesario
				ResultadoProc<Boolean> imagenes = imagenProductoService.compareChangesAndSave(producto.getImagenes(),
						imagenesOld.getResultado());
				if (imagenes.isError()) {
					salida.setError(true);
					salida.setMensaje(imagenes.getMensaje());
					return salida;
				}

				productoRepository.save(producto);

				producto = this.findById(producto.getId()).getResultado();

				salida.setResultado(producto);
				salida.setMensaje(mensaje);
				salida.setResultado(producto);
			} else {
				salida.setError(true);
				salida.setMensaje("Se está intentando registrar un nuevo producto, esta acción no esta permitida");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = producto.getId() > 0 ? "actualizar" : "registrar";
			salida.setMensaje("Se produjo un error inesperado al intentar " + accion + " el producto");
		}
		return salida;
	}

	/**
	 * Asigna el {@link Producto} como referencia a la imágen, ya que por defecto en
	 * el json viene null.
	 * 
	 * @param imagenes Listado de {@link ImagenProducto}
	 * @param producto Entidad {@link Producto}
	 * @return Listado de las imagenes del producto
	 */
	private List<ImagenProducto> asignarProducto(List<ImagenProducto> imagenes, Producto producto) {
		for (ImagenProducto imagenProducto : imagenes) {
			imagenProducto.setProducto(producto);
		}
		return imagenes;
	}

	@Override
	public ResultadoProc<Producto> changeState(int productoId) {
		ResultadoProc<Producto> salida = new ResultadoProc<Producto>();
		try {
			String mensaje = "";
			Producto productoOriginal = this.findById(productoId).getResultado();
			if (productoId > 0) {
				if (productoOriginal == null) {
					salida.setError(true);
					salida.setMensaje("No se econtró el producto");
					return salida;
				}
				productoOriginal.setActivo(!productoOriginal.isActivo());
				if (productoOriginal.isActivo()) {
					mensaje = "El producto " + productoOriginal.getDescripcion() + " está activo";
				} else {
					mensaje = "El producto " + productoOriginal.getDescripcion() + " está inactivo";
				}
			}
			productoRepository.save(productoOriginal);
			salida.setMensaje(mensaje);
			salida.setResultado(productoOriginal);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje("Se produjo un error inesperado al intentar cambiar el estado del producto");
		}
		return salida;
	}

}
