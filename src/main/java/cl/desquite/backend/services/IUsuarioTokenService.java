package cl.desquite.backend.services;

import cl.desquite.backend.entities.UsuarioToken;
import cl.desquite.backend.utils.ResultadoProc;

public interface IUsuarioTokenService {

    UsuarioToken findByToken(String token);

    ResultadoProc<UsuarioToken> save(UsuarioToken usuarioToken);

    ResultadoProc<Boolean> validateToken(String token);
}
