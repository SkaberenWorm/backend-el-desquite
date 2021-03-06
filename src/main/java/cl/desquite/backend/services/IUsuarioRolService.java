package cl.desquite.backend.services;

import java.util.List;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioRole;
import cl.desquite.backend.utils.ResultadoProc;

public interface IUsuarioRolService {

	/**
	 * Guarda un listado de {@link UsuarioRole}
	 * 
	 * @param usuarioRoles (Entidad {@link UsuarioRole})
	 * @return EL listado de {@link UsuarioRole} guardados
	 */
	ResultadoProc<List<UsuarioRole>> saveAll(List<UsuarioRole> usuarioRoles);

	ResultadoProc<Boolean> deleteAllByUsuario(Usuario usuario);

}
