package cl.desquite.backend.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.desquite.backend.entities.UsuarioToken;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.services.IUsuarioTokenService;
import cl.desquite.backend.utils.ResultadoProc;

@RestController
@RequestMapping("/usuario-token")
public class UsuarioTokenRestController {

    @Autowired
    IUsuarioTokenService usuarioTokenService;

    @Autowired
    IUsuarioService usuarioService;

    @GetMapping("/validate-new-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarTokenForNewPassword(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateTokenForNewPassword(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @GetMapping("/validate-change-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarTokenForChangePassword(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateTokenForResetPassword(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @GetMapping("/validate-unlock-user/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarTokenForUnlockUser(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateTokenForUnlockUser(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @PostMapping("/change-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> cambiarClave(@PathVariable("token") String token,
            @RequestBody() HashMap<String, String> claves) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        if (claves.containsKey("clave") && claves.containsKey("claveConfirm")) {
            if (this.usuarioTokenService.validateTokenForResetPassword(token).getResultado() == null
                    || !this.usuarioTokenService.validateTokenForResetPassword(token).getResultado()) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }
            UsuarioToken usuarioToken = this.usuarioTokenService.findByTokenForResetPassword(token);
            if (usuarioToken == null) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }

            if (!claves.get("clave").equals(claves.get("claveConfirm"))) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Las claves no coinciden"),
                        HttpStatus.OK);
            }

            salida.exitoso(this.usuarioService.cambiarClave(usuarioToken.getUsuario().getId(), claves.get("clave"))
                    .getResultado(), "Clave creada correctamente");

            usuarioToken.setActivo(false);
            usuarioToken.setForResetPassword();
            this.usuarioTokenService.save(usuarioToken);

            return new ResponseEntity<ResultadoProc<Boolean>>(salida.build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("BAD REQUEST"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/new-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> nuevaClave(@PathVariable("token") String token,
            @RequestBody() HashMap<String, String> claves) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        if (claves.containsKey("clave") && claves.containsKey("claveConfirm")) {
            if (this.usuarioTokenService.validateTokenForNewPassword(token).getResultado() == null
                    || !this.usuarioTokenService.validateTokenForNewPassword(token).getResultado()) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }
            UsuarioToken usuarioToken = this.usuarioTokenService.findByTokenForResetPassword(token);
            if (usuarioToken == null) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }

            if (!claves.get("clave").equals(claves.get("claveConfirm"))) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Las claves no coinciden"),
                        HttpStatus.OK);
            }

            salida.exitoso(this.usuarioService.cambiarClave(usuarioToken.getUsuario().getId(), claves.get("clave"))
                    .getResultado(), "Clave creada correctamente");

            usuarioToken.setActivo(false);
            usuarioToken.setForResetPassword();
            this.usuarioTokenService.save(usuarioToken);

            return new ResponseEntity<ResultadoProc<Boolean>>(salida.build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("BAD REQUEST"), HttpStatus.BAD_REQUEST);
        }
    }

}
