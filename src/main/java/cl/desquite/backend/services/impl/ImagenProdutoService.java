package cl.desquite.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.desquite.backend.entities.ImagenProducto;
import cl.desquite.backend.repositories.ImagenProductoRepository;
import cl.desquite.backend.services.IImagenProductoService;
import cl.desquite.backend.util.ResultadoProc;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ImagenProdutoService implements IImagenProductoService {

	@Autowired
	ImagenProductoRepository imagenProductoRepository;

	public ResultadoProc<List<ImagenProducto>> saveAll(List<ImagenProducto> imagenes) {
		ResultadoProc<List<ImagenProducto>> salida = new ResultadoProc<List<ImagenProducto>>();
		try {
			salida.setResultado(imagenProductoRepository.saveAll(imagenes));
			salida.setMensaje("Imagenes guardadas correctamente");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			salida.setMensaje("Se produjo un error al intentar guardar las imágenes del producto");
		}
		return salida;
	}
}
