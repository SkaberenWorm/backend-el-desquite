package cl.desquite.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cl.desquite.backend.entities.Categoria;
import cl.desquite.backend.repositories.CategoriaRepository;
import cl.desquite.backend.services.ICategoriaService;
import cl.desquite.backend.util.ResultadoProc;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class CategoriaService implements ICategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;

	@Override
	public ResultadoProc<Categoria> findById(int categoriaId) {
		ResultadoProc<Categoria> salida = new ResultadoProc<Categoria>();
		try {
			Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
			if (categoria == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado la categoría con el ID " + categoriaId);
			}
			salida.setResultado(categoria);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje(
					"Se produjo un error inesperado al intentar obtener la categoria con el ID " + categoriaId);
		}
		return salida;
	}

	@Override
	public ResultadoProc<Categoria> findByIdAndActivoTrue(int categoriaId) {
		ResultadoProc<Categoria> salida = new ResultadoProc<Categoria>();
		try {
			Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
			salida.setResultado(categoria);

			if (categoria == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado la categoría con el ID " + categoriaId);
			}
			if (!categoria.isActivo()) {
				salida.setError(true);
				salida.setMensaje("La categoría con el ID " + categoriaId + " se encuentra inactiva");
				salida.setResultado(null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje(
					"Se produjo un error inesperado al intentar obtener la categoria con el código " + categoriaId);
		}
		return salida;
	}

	@Override
	public ResultadoProc<Page<Categoria>> findAllPaginatedWithFilters(PageRequest pageable, String buscador) {
		ResultadoProc<Page<Categoria>> salida = new ResultadoProc<Page<Categoria>>();
		try {

			Page<Categoria> categorias = categoriaRepository.findAllByQuery(buscador, pageable);
			salida.setResultado(categorias);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje("Se produjo un error inesperado al intentar listar las categorias");
		}
		return salida;
	}

	@Override
	public ResultadoProc<Categoria> save(Categoria categoria) {
		ResultadoProc<Categoria> salida = new ResultadoProc<Categoria>();
		try {
			String mensaje = "";
			if (categoria.getId() > 0) {
				mensaje = "Categoría actualizada correctamente";
			} else {
				mensaje = "Categoría registrada correctamente";
			}
			salida.setResultado(categoriaRepository.save(categoria));
			salida.setMensaje(mensaje);
			salida.setResultado(categoria);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = categoria.getId() > 0 ? "actualizar" : "registrar";
			salida.setMensaje("Se produjo un error inesperado al intentar " + accion + " la categoría");
		}
		return salida;
	}

}
