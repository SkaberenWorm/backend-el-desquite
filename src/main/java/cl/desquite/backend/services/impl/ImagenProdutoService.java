package cl.desquite.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.desquite.backend.entities.ImagenProducto;
import cl.desquite.backend.entities.Producto;
import cl.desquite.backend.repositories.ImagenProductoRepository;
import cl.desquite.backend.services.IImagenProductoService;
import cl.desquite.backend.util.ResultadoProc;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ImagenProdutoService implements IImagenProductoService {

	@Autowired
	ImagenProductoRepository imagenProductoRepository;

	@Override
	public ResultadoProc<List<ImagenProducto>> saveAll(List<ImagenProducto> imagenes) {
		ResultadoProc<List<ImagenProducto>> salida = new ResultadoProc<List<ImagenProducto>>();
		try {
			salida.setResultado(imagenProductoRepository.saveAll(imagenes));
			if (imagenes.size() > 1) {
				salida.setMensaje("Imágenes guardadas correctamente");
			} else {
				salida.setMensaje("Imágen guardada correctamente");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			if (imagenes.size() > 1) {
				salida.setMensaje("Se produjo un error al intentar guardar las imágenes del producto");
			} else {
				salida.setMensaje("Se produjo un error al intentar guardar la imágen del producto");
			}
		}
		return salida;
	}

	@Override
	public ResultadoProc<List<ImagenProducto>> findAllByProducto(Producto producto) {
		ResultadoProc<List<ImagenProducto>> salida = new ResultadoProc<List<ImagenProducto>>();
		try {
			salida.setResultado(imagenProductoRepository.findAllByProducto(producto));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			salida.setMensaje("Se produjo un error al intentar obtener las imágenes del producto");

		}
		return salida;
	}

	@Override
	public ResultadoProc<Boolean> deleteAll(List<ImagenProducto> imagenes) {
		ResultadoProc<Boolean> salida = new ResultadoProc<Boolean>();
		try {
			imagenProductoRepository.deleteAll(imagenes);
			salida.setResultado(true);
			if (imagenes.size() > 1) {
				salida.setMensaje("Imágenes eliminadas correctamente");
			} else {
				salida.setMensaje("Imágen eliminada correctamente");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			if (imagenes.size() > 1) {
				salida.setMensaje("Se produjo un error al intentar eliminar las imágenes del producto");
			} else {
				salida.setMensaje("Se produjo un error al intentar eliminar la imágen del producto");
			}
		}
		return salida;
	}

	@Transactional
	@Override
	public ResultadoProc<Boolean> compareChangesAndSave(List<ImagenProducto> imagenesNew,
			List<ImagenProducto> imagenesOld) {
		ResultadoProc<Boolean> salida = new ResultadoProc<Boolean>();
		try {
			String mensajeSalida = "";
			// Verificamos si los listados contienen las mismas imágenes
			boolean mismoListado = false;
			if (imagenesNew.size() == imagenesOld.size()) {
				mismoListado = true;
				for (ImagenProducto imagenProductoOld : imagenesOld) {
					boolean mismaImagen = false;
					for (ImagenProducto imagenProductoNew : imagenesNew) {
						if (imagenProductoNew.getId().equals(imagenProductoOld.getId())) {
							mismaImagen = true;
						}
					}
					if (!mismaImagen) {
						mismoListado = false;
					}
				}
			}

			if (mismoListado) {
				salida.setResultado(true);
				return salida;
			}
			// Fin => Verificamos si los listados contienen las mismas imágenes

			// Agregamos imágenes nuevas
			List<ImagenProducto> imagenesToSave = new ArrayList<ImagenProducto>();
			for (ImagenProducto imagenProductoNew : imagenesNew) {
				boolean mismaImagen = false;
				for (ImagenProducto imagenProductoOld : imagenesOld) {
					if (imagenProductoNew.getId().equals(imagenProductoOld.getId())) {
						mismaImagen = true;
					}
				}
				if (!mismaImagen) {
					imagenesToSave.add(imagenProductoNew);
				}
			}
			if (imagenesToSave.size() > 0) {
				if (this.saveAll(imagenesToSave).isError()) {
					salida.setError(true);
					if (imagenesToSave.size() > 1) {
						mensajeSalida += "Se produjo un error al intentar agregar las nuevas imágenes <br>";
					} else {
						mensajeSalida += "Se produjo un error al intentar agregar la nueva imágen <br>";
					}
				}
			}
			// Fin => Agregamos imágenes nuevas

			// Eliminar imágenes antiguas
			List<ImagenProducto> imagenesToDelete = new ArrayList<>();
			for (ImagenProducto imagenProductoOld : imagenesOld) {
				boolean mismaImagen = false;
				for (ImagenProducto imagenProductoNew : imagenesNew) {
					if (imagenProductoNew.getId().equals(imagenProductoOld.getId())) {
						mismaImagen = true;
					}
				}
				if (!mismaImagen) {
					imagenesToDelete.add(imagenProductoOld);
				}
			}
			if (imagenesToDelete.size() > 0) {
				if (this.deleteAll(imagenesToDelete).isError()) {
					salida.setError(true);
					if (imagenesToDelete.size() > 1) {
						mensajeSalida += "Se produjo un error al intentar eliminar las imágenes antiguas<br>";
					} else {
						mensajeSalida += "Se produjo un error al intentar eliminar la imágen antigua<br>";
					}
				}
			}
			// FIN => Eliminar imágenes antiguas

			salida.setMensaje(mensajeSalida);
			salida.setResultado(true);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			salida.setResultado(false);
			salida.setMensaje("Se produjo un error al intentar guardar las imágenes del producto");
		}
		return salida;
	}
}
