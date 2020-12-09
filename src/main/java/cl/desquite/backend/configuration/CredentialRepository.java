package cl.desquite.backend.configuration;

import java.util.List;

import com.warrenstrange.googleauth.ICredentialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.Util;

@Component
public class CredentialRepository implements ICredentialRepository {

    @Autowired
    IUsuarioService usuarioService;

    @Override
    public String getSecretKey(String userName) {
        ResultadoProc<Usuario> usuario = this.usuarioService.findByEmail(userName);
        if (usuario.getResultado() == null) {
            return null;
        }
        return usuario.getResultado().getSecret();
    }

    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        try {
            Usuario usuario = this.usuarioService.findByEmail(userName).getResultado();
            if (usuario == null) {
                Util.printError("saveUserCredentials(...)", new Exception("El usuario " + userName + " no existe"));
            }
            usuario.setSecret(secretKey);
            this.usuarioService.save(usuario);
        } catch (Exception e) {
            Util.printError("saveUserCredentials(...)", e);
        }
    }

}
