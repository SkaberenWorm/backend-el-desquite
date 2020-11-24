package cl.desquite.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.entities.Categoria;
import cl.desquite.backend.repositories.CategoriaRepository;
import cl.desquite.backend.services.ICategoriaService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.ResultadoProc.Builder;

@Service
@CommonsLog
public class CategoriaService implements ICategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;

	@Override
	public ResultadoProc<Categoria> findById(int categoriaId) {
		Builder<Categoria> salida = new Builder<Categoria>();
		try {
			Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
			if (categoria == null) {
				salida.fallo("No se ha encontrado la categoría con el ID " + categoriaId);
			}
			salida.exitoso(categoria);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar obtener la categoria con el ID " + categoriaId);
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Categoria> findByIdAndActivoTrue(int categoriaId) {
		Builder<Categoria> salida = new Builder<Categoria>();
		try {
			Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
			salida.exitoso(categoria);

			if (categoria == null) {
				salida.fallo("No se ha encontrado la categoría con el ID " + categoriaId);
			}
			if (!categoria.isActivo()) {
				salida.fallo("La categoría con el ID " + categoriaId + " se encuentra inactiva");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo(
					"Se produjo un error inesperado al intentar obtener la categoria con el código " + categoriaId);
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Page<Categoria>> findAllPaginatedWithSearch(PageRequest pageable, String buscador) {
		Builder<Page<Categoria>> salida = new Builder<Page<Categoria>>();
		try {

			Page<Categoria> categorias = categoriaRepository.findAllByQuery(buscador, pageable);
			salida.exitoso(categorias);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar listar las categorias");
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Categoria> save(Categoria categoria) {
		Builder<Categoria> salida = new Builder<Categoria>();
		try {
			String mensaje = "";
			if (categoria.getId() > 0) {
				mensaje = "Categoría actualizada correctamente";
			} else {
				mensaje = "Categoría registrada correctamente";
			}
			categoriaRepository.save(categoria);
			salida.exitoso(categoria, mensaje);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = categoria.getId() > 0 ? "actualizar" : "registrar";
			salida.fallo("Se produjo un error inesperado al intentar " + accion + " la categoría");
		}
		return salida.build();
	}

}
