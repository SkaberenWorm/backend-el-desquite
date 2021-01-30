package cl.desquite.backend.controllers;

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
import cl.desquite.backend.models.Clave;
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

    @GetMapping("/free/validate-new-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarTokenForNewPassword(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateTokenForNewPassword(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @GetMapping("/free/validate-change-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarTokenForChangePassword(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateTokenForResetPassword(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @GetMapping("/free/validate-unlock-user/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarTokenForUnlockUser(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateTokenForUnlockUser(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @PostMapping("/free/change-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> cambiarClave(@PathVariable("token") String token,
            @RequestBody() Clave clave) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        if (clave.getClave() != null && !clave.getClave().isEmpty() && clave.getClaveConfirm() != null
                && !clave.getClaveConfirm().isEmpty()) {
            Boolean tokenValido = this.usuarioTokenService.validateTokenForResetPassword(token).getResultado();
            if (tokenValido == null || !tokenValido) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }
            UsuarioToken usuarioToken = this.usuarioTokenService.findByTokenForResetPassword(token);
            if (usuarioToken == null) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }

            if (!clave.getClave().equals(clave.getClaveConfirm())) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Las claves no coinciden"),
                        HttpStatus.OK);
            }

            salida.exitoso(this.usuarioService.cambiarClave(usuarioToken.getUsuario().getId(), clave.getClave())
                    .getResultado(), "Su clave ha sido cambiada exitosamente");

            usuarioToken.setActivo(false);
            usuarioToken.setForResetPassword();
            this.usuarioTokenService.save(usuarioToken);

            return new ResponseEntity<ResultadoProc<Boolean>>(salida.build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("BAD REQUEST"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/free/new-password/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> nuevaClave(@PathVariable("token") String token,
            @RequestBody() Clave clave) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        if (clave.getClave() != null && !clave.getClave().isEmpty() && clave.getClaveConfirm() != null
                && !clave.getClaveConfirm().isEmpty()) {
            Boolean tokenValido = this.usuarioTokenService.validateTokenForNewPassword(token).getResultado();
            if (tokenValido == null || !tokenValido) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }
            UsuarioToken usuarioToken = this.usuarioTokenService.findByTokenForNewPassword(token);
            if (usuarioToken == null) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }

            if (!clave.getClave().equals(clave.getClaveConfirm())) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Las claves no coinciden"),
                        HttpStatus.OK);
            }

            salida.exitoso(this.usuarioService.cambiarClave(usuarioToken.getUsuario().getId(), clave.getClave())
                    .getResultado(), "Su clave ha sido creada exitosamente");

            usuarioToken.setActivo(false);
            usuarioToken.setForResetPassword();
            this.usuarioTokenService.save(usuarioToken);

            return new ResponseEntity<ResultadoProc<Boolean>>(salida.build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("BAD REQUEST"), HttpStatus.BAD_REQUEST);
        }
    }

}
