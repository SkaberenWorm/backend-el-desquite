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
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class UsuarioRolService implements IUsuarioRolService {
	@Autowired
	UsuarioRolRepository usuarioRolRepository;

	@Override
	public ResultadoProc<List<UsuarioRole>> saveAll(List<UsuarioRole> usuarioRoles) {
		ResultadoProc<List<UsuarioRole>> salida = new ResultadoProc<List<UsuarioRole>>();
		try {
			salida.setResultado(usuarioRolRepository.saveAll(usuarioRoles));
			if (usuarioRoles.size() > 1) {
				salida.setMensaje("Roles asignados correctamente");
			} else {
				salida.setMensaje("Role asignado correctamente");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			salida.setMensaje("Se produjo un error al intentar asignar los roles al usuario");
		}
		return salida;

	}

	@Override
	@Transactional
	public ResultadoProc<Boolean> deleteAllByUsuario(Usuario usuario) {
		ResultadoProc<Boolean> salida = new ResultadoProc<Boolean>();
		try {
			usuarioRolRepository.deleteAllByUsuario(usuario);
			salida.setError(false);
			salida.setResultado(true);
		} catch (Exception e) {
			salida.setError(true);
			salida.setMensaje("Se produjo un error al intentar eliminar los roles actuales del usuario");
			salida.setResultado(false);
		}
		return salida;

	}

}
