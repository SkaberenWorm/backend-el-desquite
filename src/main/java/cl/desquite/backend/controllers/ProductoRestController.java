package cl.desquite.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.desquite.backend.entities.Producto;
import cl.desquite.backend.services.IProductoService;
import cl.desquite.backend.util.ProductoFilter;
import cl.desquite.backend.util.ResultadoProc;
import cl.desquite.backend.util.SearchPagination;

@RequestMapping("/api/producto")
@RestController
public class ProductoRestController {
	@Autowired
	IProductoService productoService;

	@PreAuthorize("hasAuthority('PRODUCTO_VER')")
	@GetMapping("/{id}")
	public ResponseEntity<ResultadoProc<Producto>> findById(@PathVariable("id") int productoId) {
		ResultadoProc<Producto> salida = productoService.findById(productoId);
		return new ResponseEntity<ResultadoProc<Producto>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('PRODUCTO_VER')")
	@GetMapping("/id/active")
	public ResponseEntity<ResultadoProc<Producto>> findByIdAndActiveTrue(@PathVariable int productoId) {
		ResultadoProc<Producto> salida = productoService.findByIdAndActivoTrue(productoId);
		return new ResponseEntity<ResultadoProc<Producto>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('PRODUCTO_LISTAR')")
	@GetMapping("/page-all")
	public ResponseEntity<ResultadoProc<Page<Producto>>> findAllPaginated(
			@RequestBody SearchPagination<String> searchPagination) {
		PageRequest pageable = searchPagination.getPageRequest();
		ResultadoProc<Page<Producto>> salida = productoService.findAllPaginated(pageable);
		return new ResponseEntity<ResultadoProc<Page<Producto>>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('PRODUCTO_LISTAR')")
	@PostMapping("/page-with-filters")
	public ResponseEntity<ResultadoProc<Page<Producto>>> findAllPaginatedWithFilters(
			@RequestBody SearchPagination<ProductoFilter> searchPagination) {
		PageRequest pageable = searchPagination.getPageRequest();
		String query = searchPagination.getSeek().getQuery();
		List<Integer> categoriasId = searchPagination.getSeek().getCategoriasId();
		ResultadoProc<Page<Producto>> salida = productoService.findAllPaginatedWithFilters(pageable, query,
				categoriasId);
		return new ResponseEntity<ResultadoProc<Page<Producto>>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('PRODUCTO_CREAR')")
	@PostMapping
	public ResponseEntity<ResultadoProc<Producto>> save(@RequestBody Producto producto) {
		ResultadoProc<Producto> salida = productoService.save(producto);
		return new ResponseEntity<ResultadoProc<Producto>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('PRODUCTO_EDITAR')")
	@PutMapping
	public ResponseEntity<ResultadoProc<Producto>> update(@RequestBody Producto producto) {
		ResultadoProc<Producto> salida = productoService.update(producto);
		return new ResponseEntity<ResultadoProc<Producto>>(salida, HttpStatus.OK);
	}

}
