package cl.desquite.backend.services;

import cl.desquite.backend.entities.UsuarioToken;
import cl.desquite.backend.utils.ResultadoProc;

public interface IUsuarioTokenService {

    UsuarioToken findByTokenForNewPassword(String token);

    UsuarioToken findByTokenForResetPassword(String token);

    UsuarioToken findByTokenForUnlockUser(String token);

    ResultadoProc<UsuarioToken> save(UsuarioToken usuarioToken);

    ResultadoProc<Boolean> validateTokenForNewPassword(String token);

    ResultadoProc<Boolean> validateTokenForResetPassword(String token);

    ResultadoProc<Boolean> validateTokenForUnlockUser(String token);
}
