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
@RequestMapping("/create-password")
public class CreatePasswordRestController {

    @Autowired
    IUsuarioTokenService usuarioTokenService;

    @Autowired
    IUsuarioService usuarioService;

    @GetMapping("/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> validarToken(@PathVariable("token") String token) {
        ResultadoProc<Boolean> usuarioToken = this.usuarioTokenService.validateToken(token);
        return new ResponseEntity<ResultadoProc<Boolean>>(usuarioToken, HttpStatus.OK);
    }

    @PostMapping("/{token}")
    public ResponseEntity<ResultadoProc<Boolean>> cambiarClave(@PathVariable("token") String token,
            @RequestBody() HashMap<String, String> claves) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        if (claves.containsKey("clave") && claves.containsKey("claveConfirm")) {
            if (this.usuarioTokenService.validateToken(token).getResultado() == null
                    || !this.usuarioTokenService.validateToken(token).getResultado()) {
                return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("Token inválido"), HttpStatus.OK);
            }
            UsuarioToken usuarioToken = this.usuarioTokenService.findByToken(token);
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
            this.usuarioTokenService.save(usuarioToken);

            return new ResponseEntity<ResultadoProc<Boolean>>(salida.build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ResultadoProc<Boolean>>(salida.fallo("BAD REQUEST"), HttpStatus.BAD_REQUEST);
        }
    }

}
