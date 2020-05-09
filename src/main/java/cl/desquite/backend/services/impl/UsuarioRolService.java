package cl.desquite.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioRole;
import cl.desquite.backend.repositories.UsuarioRolRepository;
import cl.desquite.backend.services.IUsuarioRolService;
import cl.desquite.backend.util.ResultadoProc;
import cl.desquite.backend.util.ResultadoProc.Builder;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class UsuarioRolService implements IUsuarioRolService {
	@Autowired
	UsuarioRolRepository usuarioRolRepository;

	@Override
	public ResultadoProc<List<UsuarioRole>> saveAll(List<UsuarioRole> usuarioRoles) {
		Builder<List<UsuarioRole>> salida = new Builder<List<UsuarioRole>>();
		try {
			usuarioRolRepository.saveAll(usuarioRoles);
			if (usuarioRoles.size() > 1) {
				salida.exitoso(usuarioRoles, "Roles asignados correctamente");
			} else {
				salida.exitoso(usuarioRoles, "Role asignado correctamente");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error al intentar asignar los roles al usuario");
		}
		return salida.build();

	}

	@Override
	@Transactional
	public ResultadoProc<Boolean> deleteAllByUsuario(Usuario usuario) {
		Builder<Boolean> salida = new Builder<Boolean>();
		try {
			usuarioRolRepository.deleteAllByUsuario(usuario);
			salida.exitoso(true);
		} catch (Exception e) {
			salida.fallo("Se produjo un error al intentar eliminar los roles actuales del usuario");
		}
		return salida.build();
	}

}
