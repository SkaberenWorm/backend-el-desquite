package cl.desquite.backend.configuration.oauth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.util.ResultadoProc;

@Component
public class InformacionAdicionalUsuarioToken implements TokenEnhancer {

	@Autowired
	IUsuarioService usuarioClienteService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		ResultadoProc<Usuario> usuarioClienteResult = usuarioClienteService
				.findByEmailOrUsuario(authentication.getName());
		Usuario usuarioCliente = usuarioClienteResult.getResultado();
		Map<String, Object> info = new HashMap<>();

		info.put("full_name", usuarioCliente.getNombre().concat(" ").concat(usuarioCliente.getApellidos()));
//		info.put("expireation_date", accessToken.getExpiration().getTime());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}
