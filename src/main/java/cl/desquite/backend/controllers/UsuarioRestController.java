package cl.desquite.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.util.ResultadoProc;
import cl.desquite.backend.util.SearchPagination;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	IUsuarioService usuarioService;

	@GetMapping("/{id}")
	public ResponseEntity<ResultadoProc<Usuario>> findById(@PathVariable("id") int usuarioId) {
		ResultadoProc<Usuario> salida = usuarioService.findById(usuarioId);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@GetMapping("/{id}/active")
	public ResponseEntity<ResultadoProc<Usuario>> findByIdAndActiveTrue(@PathVariable("id") int usuarioId) {
		ResultadoProc<Usuario> salida = usuarioService.findByIdAndActivoTrue(usuarioId);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@PostMapping("/page-with-filters")
	public ResponseEntity<ResultadoProc<Page<Usuario>>> findAllPaginatedWithFilters(
			@RequestBody SearchPagination<String> searchPagination) {
		PageRequest pageable = searchPagination.getPageRequest();
		String query = searchPagination.getSeek();
		ResultadoProc<Page<Usuario>> salida = usuarioService.findAllPaginatedWithFilters(pageable, query);
		return new ResponseEntity<ResultadoProc<Page<Usuario>>>(salida, HttpStatus.OK);
	}

	@GetMapping("/change-state/{id}")
	public ResponseEntity<ResultadoProc<Usuario>> changeState(@PathVariable("id") int usuarioId) {
		ResultadoProc<Usuario> salida = usuarioService.changeState(usuarioId);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@PostMapping
	@PutMapping
	public ResponseEntity<ResultadoProc<Usuario>> save(@RequestBody Usuario usuario) {
		ResultadoProc<Usuario> salida = usuarioService.save(usuario);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

}
