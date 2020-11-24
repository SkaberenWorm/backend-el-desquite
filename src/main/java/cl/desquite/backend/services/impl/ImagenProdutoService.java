package cl.desquite.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.entities.ImagenProducto;
import cl.desquite.backend.entities.Producto;
import cl.desquite.backend.repositories.ImagenProductoRepository;
import cl.desquite.backend.services.IImagenProductoService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.ResultadoProc.Builder;

@Service
@CommonsLog
public class ImagenProdutoService implements IImagenProductoService {

	@Autowired
	ImagenProductoRepository imagenProductoRepository;

	@Override
	public ResultadoProc<List<ImagenProducto>> saveAll(List<ImagenProducto> imagenes) {
		Builder<List<ImagenProducto>> salida = new Builder<List<ImagenProducto>>();
		try {
			imagenProductoRepository.saveAll(imagenes);
			if (imagenes.size() > 1) {
				salida.exitoso(imagenes, "Imágenes guardadas correctamente");
			} else {
				salida.exitoso(imagenes, "Imágen guardada correctamente");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (imagenes.size() > 1) {
				salida.fallo("Se produjo un error al intentar guardar las imágenes del producto");
			} else {
				salida.fallo("Se produjo un error al intentar guardar la imágen del producto");
			}
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<List<ImagenProducto>> findAllByProducto(Producto producto) {
		Builder<List<ImagenProducto>> salida = new Builder<List<ImagenProducto>>();
		try {
			salida.exitoso(imagenProductoRepository.findAllByProducto(producto));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Boolean> deleteAll(List<ImagenProducto> imagenes) {
		Builder<Boolean> salida = new Builder<Boolean>();
		try {
			imagenProductoRepository.deleteAll(imagenes);
			if (imagenes.size() > 1) {
				salida.exitoso(true, "Imágenes eliminadas correctamente");
			} else {
				salida.exitoso(true, "Imágen eliminada correctamente");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (imagenes.size() > 1) {
				salida.fallo("Se produjo un error al intentar eliminar las imágenes del producto");
			} else {
				salida.fallo("Se produjo un error al intentar eliminar la imágen del producto");
			}
		}
		return salida.build();
	}

	@Transactional
	@Override
	public ResultadoProc<Boolean> compareChangesAndSave(List<ImagenProducto> imagenesNew,
			List<ImagenProducto> imagenesOld) {
		Builder<Boolean> salida = new Builder<Boolean>();
		try {
			String mensajeSalida = "";
			boolean isError = false;
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
				salida.exitoso(true);
				return salida.build();
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
					isError = true;
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
					isError = true;
					if (imagenesToDelete.size() > 1) {
						mensajeSalida += "Se produjo un error al intentar eliminar las imágenes antiguas<br>";
					} else {
						mensajeSalida += "Se produjo un error al intentar eliminar la imágen antigua<br>";
					}
				}
			}
			// FIN => Eliminar imágenes antiguas
			if (isError) {
				salida.fallo(mensajeSalida);
			} else {
				salida.exitoso(true, mensajeSalida);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error al intentar guardar las imágenes del producto");
		}
		return salida.build();
	}
}
