package cl.desquite.backend.controllers;

import java.util.List;

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

import cl.desquite.backend.entities.Role;
import cl.desquite.backend.services.IRoleService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.SearchPagination;

@RestController
@RequestMapping("/api/rol")
public class RoleRestController {

	@Autowired
	IRoleService roleService;

	@GetMapping("/{id}")
	public ResponseEntity<ResultadoProc<Role>> findById(@PathVariable("id") int roleId) {
		ResultadoProc<Role> salida = roleService.findById(roleId);
		return new ResponseEntity<ResultadoProc<Role>>(salida, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResultadoProc<Role>> save(@RequestBody Role role) {
		ResultadoProc<Role> salida = roleService.save(role);
		return new ResponseEntity<ResultadoProc<Role>>(salida, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<ResultadoProc<Role>> update(@RequestBody Role role) {
		ResultadoProc<Role> salida = roleService.update(role);
		return new ResponseEntity<ResultadoProc<Role>>(salida, HttpStatus.OK);
	}

	@PostMapping("/page-all-by-search")
	public ResponseEntity<ResultadoProc<Page<Role>>> findAllPaginatedBySearch(
			@RequestBody SearchPagination<String> searchPagination) {
		PageRequest pageable = searchPagination.getPageRequest();
		String search = searchPagination.getSeek();
		ResultadoProc<Page<Role>> salida = roleService.findAllPaginatedBySearch(search, pageable);
		return new ResponseEntity<ResultadoProc<Page<Role>>>(salida, HttpStatus.OK);
	}

	@GetMapping("/find-all-activos")
	public ResponseEntity<ResultadoProc<List<Role>>> findAllActivos() {
		ResultadoProc<List<Role>> salida = this.roleService.findAllActivos();
		return new ResponseEntity<ResultadoProc<List<Role>>>(salida, HttpStatus.OK);
	}
}