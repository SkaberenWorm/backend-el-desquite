package cl.desquite.backend.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.models.CredentialLogin;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.SearchPagination;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	IUsuarioService usuarioService;

	@PreAuthorize("hasAuthority('USUARIO_VER')")
	@GetMapping("/{id}")
	public ResponseEntity<ResultadoProc<Usuario>> findById(@PathVariable("id") int usuarioId) {
		ResultadoProc<Usuario> salida = usuarioService.findById(usuarioId);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('USUARIO_VER')")
	@GetMapping("/{id}/active")
	public ResponseEntity<ResultadoProc<Usuario>> findByIdAndActiveTrue(@PathVariable("id") int usuarioId) {
		ResultadoProc<Usuario> salida = usuarioService.findByIdAndActivoTrue(usuarioId);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('USUARIO_LISTAR')")
	@PostMapping("/page-all-by-search")
	public ResponseEntity<ResultadoProc<Page<Usuario>>> findAllPaginatedWithSearch(
			@RequestBody SearchPagination<String> searchPagination) {
		PageRequest pageable = searchPagination.getPageRequest();
		String query = searchPagination.getSeek();
		ResultadoProc<Page<Usuario>> salida = usuarioService.findAllPaginatedWithSearch(pageable, query);
		return new ResponseEntity<ResultadoProc<Page<Usuario>>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('USUARIO_EDITAR')")
	@GetMapping("/change-state/{id}")
	public ResponseEntity<ResultadoProc<Usuario>> changeState(@PathVariable("id") int usuarioId) {
		ResultadoProc<Usuario> salida = usuarioService.changeState(usuarioId);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('USUARIO_CREAR')")
	@PostMapping
	public ResponseEntity<ResultadoProc<Usuario>> save(@RequestBody Usuario usuario) {
		ResultadoProc<Usuario> salida = usuarioService.save(usuario);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('USUARIO_EDITAR')")
	@PutMapping
	public ResponseEntity<ResultadoProc<Usuario>> update(@RequestBody Usuario usuario) {
		ResultadoProc<Usuario> salida = usuarioService.update(usuario);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

	@ApiOperation(value = "Envía un email al usuario para que pueda cambiar su contraseña")
	@PostMapping("/new-token-for-change-password")
	public ResponseEntity<ResultadoProc<Boolean>> createTokenForResetPassword(@RequestBody Usuario usuario) {
		ResultadoProc<Boolean> usuarioToken = this.usuarioService.createTokenForResetPassword(usuario);
		return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
	}

	@ApiOperation(value = "Envía un email al usuario para que pueda recuperar su contraseña")
	@GetMapping("/free/recovery-password")
	public ResponseEntity<ResultadoProc<Boolean>> recoveryPassword(@RequestParam("email") String usuario) {
		ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
		ResultadoProc<Usuario> usuarioResul = this.usuarioService.findByEmail(usuario.trim().toLowerCase());
		if (usuarioResul.isError()) {
			return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo(usuarioResul.getMensaje()), HttpStatus.OK);
		}
		if (usuarioResul.getResultado() == null) {
			return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("No se encontró el usuario " + usuario),
					HttpStatus.OK);
		}
		ResultadoProc<Boolean> usuarioToken = this.usuarioService
				.createTokenForResetPassword(usuarioResul.getResultado());
		return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
	}

	@ApiOperation(value = "Valida si las credenciales con correctas y devuelve los datos del usuario", notes = ""
			+ "Este es el <strong>paso 1</strong> para la autenticación del usuario \n"
			+ "Dentro de los datos del alumno se indica si tiene configurado la 2FA, si el usuario la tiene habilitada deberá verificar el código <a target=\"_blank\" href=\"/swagger-ui.html#/two-factor-authentication-controller/validateKeyUsingPOST\">AQUÍ</a> \n"
			+ "Posterior a los pasos anteriores el usuario debe pasar por el oauth/token para obtener el token \n\n"
			+ "El usuario tiene 5 intentos para ingresar correctamente las credenciales de lo contratio será bloqueado, este podrá desbloquearlo inmediatamente <a target=\"_blank\" href=\"/swagger-ui.html#/usuario-token-rest-controller/validarTokenForUnlockUserUsingGET\">AQUÍ</a> ingresando solamente su correo")
	@PostMapping("/free/validate-credentials")
	public ResponseEntity<ResultadoProc<Usuario>> validateCredentials(@RequestBody CredentialLogin credentials) {
		ResultadoProc<Usuario> salida = this.usuarioService.login(credentials);
		return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
	}

}
