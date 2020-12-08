package cl.desquite.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.desquite.backend.entities.GrupoPrivilegio;
import cl.desquite.backend.services.IGrupoPrivilegioService;
import cl.desquite.backend.utils.ResultadoProc;

@RestController
@RequestMapping("/api/grupo-privilegio")
public class GrupoPrivilegioRestController {

	@Autowired
	IGrupoPrivilegioService grupoPrivilegioService;

	@GetMapping("/{id}")
	public ResponseEntity<ResultadoProc<GrupoPrivilegio>> findById(@PathVariable("id") int grupoPrivilegioId) {
		ResultadoProc<GrupoPrivilegio> salida = this.grupoPrivilegioService.findById(grupoPrivilegioId);
		return new ResponseEntity<ResultadoProc<GrupoPrivilegio>>(salida, HttpStatus.OK);
	}

	@GetMapping("/find-all-activos")
	public ResponseEntity<ResultadoProc<List<GrupoPrivilegio>>> findAllActivos() {
		ResultadoProc<List<GrupoPrivilegio>> salida = this.grupoPrivilegioService.findAllActivos();
		return new ResponseEntity<ResultadoProc<List<GrupoPrivilegio>>>(salida, HttpStatus.OK);
	}
}