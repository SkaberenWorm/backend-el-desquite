package cl.desquite.backend.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.utils.ResultadoProc;

@RequestMapping("/login")
@RestController
public class LoginRestController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    IUsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ResultadoProc<Usuario>> login(@RequestBody HashMap<String, String> credentials) {
        ResultadoProc<Usuario> salida = this.usuarioService.login(credentials);
        return new ResponseEntity<ResultadoProc<Usuario>>(salida, HttpStatus.OK);
    }
}
