package cl.desquite.backend.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.desquite.backend.entities.UsuarioToken;
import cl.desquite.backend.repositories.UsuarioTokenRepository;
import cl.desquite.backend.services.IUsuarioTokenService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.Util;

@Service
public class UsuarioTokenService implements IUsuarioTokenService {

    @Autowired
    UsuarioTokenRepository usuarioTokenRepository;

    @Override
    public ResultadoProc<Boolean> validateTokenForNewPassword(String token) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        try {
            UsuarioToken usuarioToken = this.usuarioTokenRepository.findByToken(token);
            if (usuarioToken == null || !usuarioToken.isForNewPassword()) {
                return salida.fallo("Token inválido");
            }

            if (usuarioToken.getFechaCaducidad().before(new Date()) || !usuarioToken.isActivo()) {
                return salida.fallo("Token caducado");
            }

            salida.exitoso(true);
        } catch (Exception e) {
            Util.printError("validateToken(\"" + token + "\")", e);
            salida.fallo("Se produjo un error inesperado al intentar validar el token del usuario");
        }
        return salida.build();
    }

    @Override
    public ResultadoProc<Boolean> validateTokenForResetPassword(String token) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        try {
            UsuarioToken usuarioToken = this.usuarioTokenRepository.findByToken(token);
            if (usuarioToken == null || !usuarioToken.isForResetPassword()) {
                return salida.fallo("Token inválido");
            }

            if (usuarioToken.getFechaCaducidad().before(new Date()) || !usuarioToken.isActivo()) {
                return salida.fallo("Token caducado");
            }

            salida.exitoso(true);
        } catch (Exception e) {
            Util.printError("validateToken(\"" + token + "\")", e);
            salida.fallo("Se produjo un error inesperado al intentar validar el token del usuario");
        }
        return salida.build();
    }

    @Override
    public ResultadoProc<Boolean> validateTokenForUnlockUser(String token) {
        ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
        try {
            UsuarioToken usuarioToken = this.usuarioTokenRepository.findByToken(token);
            if (usuarioToken == null || !usuarioToken.isForUnlockUser()) {
                return salida.fallo("Token inválido");
            }

            if (usuarioToken.getFechaCaducidad().before(new Date()) || !usuarioToken.isActivo()) {
                return salida.fallo("Token caducado");
            }

            salida.exitoso(true);
        } catch (Exception e) {
            Util.printError("validateToken(\"" + token + "\")", e);
            salida.fallo("Se produjo un error inesperado al intentar validar el token del usuario");
        }
        return salida.build();
    }

    @Override
    public UsuarioToken findByTokenForNewPassword(String token) {
        try {
            UsuarioToken usuarioToken = this.usuarioTokenRepository.findByToken(token);
            if (!usuarioToken.isForNewPassword()) {
                return null;
            }
            return usuarioToken;
        } catch (Exception e) {
            Util.printError("findByTokenForNewPassword(\"" + token + "\")", e);
            return null;
        }
    }

    @Override
    public UsuarioToken findByTokenForResetPassword(String token) {
        try {
            UsuarioToken usuarioToken = this.usuarioTokenRepository.findByToken(token);
            if (!usuarioToken.isForResetPassword()) {
                return null;
            }
            return usuarioToken;
        } catch (Exception e) {
            Util.printError("findByTokenForResetPassword(\"" + token + "\")", e);
            return null;
        }
    }

    @Override
    public UsuarioToken findByTokenForUnlockUser(String token) {
        try {
            UsuarioToken usuarioToken = this.usuarioTokenRepository.findByToken(token);
            if (!usuarioToken.isForUnlockUser()) {
                return null;
            }
            return usuarioToken;
        } catch (Exception e) {
            Util.printError("findByTokenForUnlockUser(\"" + token + "\")", e);
            return null;
        }
    }

    @Override
    public ResultadoProc<UsuarioToken> save(UsuarioToken usuarioToken) {
        ResultadoProc.Builder<UsuarioToken> salida = new ResultadoProc.Builder<UsuarioToken>();
        try {
            if (usuarioToken.getTipo() == null || usuarioToken.getTipo().isEmpty()) {
                return salida.fallo("Se produjo un error, el tipo del token es obligatorio");
            }
            salida.exitoso(this.usuarioTokenRepository.save(usuarioToken));
        } catch (Exception e) {
            Util.printError("save(" + usuarioToken.toString() + ")", e);
            salida.fallo("Se produjo un error inesperado al intentar guardar el token del usuario");
        }
        return salida.build();
    }

}
