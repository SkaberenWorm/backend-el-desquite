package cl.desquite.backend.configuration.oauth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import cl.desquite.backend.entities.Role;
import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.util.ResultadoProc;

@Component
public class InformacionAdicionalUsuarioToken implements TokenEnhancer {

	@Autowired
	IUsuarioService usuarioClienteService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		ResultadoProc<Usuario> usuarioResult = usuarioClienteService.findByEmail(authentication.getName());
		Usuario usuario = usuarioResult.getResultado();
		Map<String, Object> info = new HashMap<>();
		Set<String> roles = new HashSet<>();
		usuario.getRoles().forEach((final Role rol) -> roles.add(rol.getNombre()));

		info.put("full_name", usuario.getNombre().concat(" ").concat(usuario.getApellidos()));
		info.put("roles", roles);

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}
