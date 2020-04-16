package cl.desquite.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cl.desquite.backend.entities.Role;
import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioRole;
import cl.desquite.backend.repositories.UsuarioRepository;
import cl.desquite.backend.services.IUsuarioRolService;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.util.ResultadoProc;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	IUsuarioRolService usuarioRolesService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public ResultadoProc<Usuario> findByEmailOrUsuario(String emailOrUser) {
		ResultadoProc<Usuario> salida = new ResultadoProc<Usuario>();
		try {
			Usuario usuario = usuarioRepository.findByEmailOrUser(emailOrUser);
			salida.setResultado(usuario);
			salida.setMensaje("Usuario encontrado correctamente");
			if (usuario == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado el usuario " + emailOrUser);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setError(true);
			salida.setMensaje("Se produjo un error inesperado al intentar obtener el usuario");
		}
		return salida;
	}

	@Override
	public UserDetails loadUserByUsername(String emailOrUser) throws UsernameNotFoundException {
		emailOrUser = emailOrUser.toLowerCase().trim();
		Usuario usuario = usuarioRepository.findByEmailOrUser(emailOrUser);
		if (usuario == null) {
			log.info("Usuario ingresado no existe");
			throw new UsernameNotFoundException("Usuario o Clave incorrectos");
		}
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		for (UsuarioRole rol : usuario.getRoles()) {
			grantedAuths.add(new SimpleGrantedAuthority(rol.getRol().getNombre()));
		}
		return new User(emailOrUser, usuario.getClave(), true, true, true, true, grantedAuths);
	}

	@Override
	public ResultadoProc<Usuario> findById(int usuarioId) {
		ResultadoProc<Usuario> salida = new ResultadoProc<Usuario>();
		try {
			Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
			if (usuario == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado el usuario con el código " + usuarioId);
			}
			salida.setResultado(usuario);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje(
					"Se produjo un error inesperado al intentar obtener el usuario con el código " + usuarioId);
		}
		return salida;
	}

	@Override
	public ResultadoProc<Usuario> findByIdAndActivoTrue(int usuarioId) {
		ResultadoProc<Usuario> salida = new ResultadoProc<Usuario>();
		try {
			Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
			salida.setResultado(usuario);

			if (usuario == null) {
				salida.setError(true);
				salida.setMensaje("No se ha encontrado el usuario con el código " + usuarioId);
			}
			if (!usuario.isActivo()) {
				salida.setError(true);
				salida.setMensaje("El usuario con el código " + usuarioId + " se encuentra inactivo");
				salida.setResultado(null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje(
					"Se produjo un error inesperado al intentar obtener el usuario con el código " + usuarioId);
		}
		return salida;
	}

	@Override
	public ResultadoProc<Page<Usuario>> findAllPaginatedWithFilters(PageRequest pageable, String buscador) {
		ResultadoProc<Page<Usuario>> salida = new ResultadoProc<Page<Usuario>>();
		try {
			Page<Usuario> usuarios = usuarioRepository.findAllPaginatedWithFilters(buscador, pageable);
			salida.setResultado(usuarios);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje("Se produjo un error inesperado al intentar listar los usuarios");
		}
		return salida;
	}

	@Override
	public ResultadoProc<Usuario> save(Usuario usuario) {
		ResultadoProc<Usuario> salida = new ResultadoProc<Usuario>();
		try {
			String mensaje = "";
			if (usuario.getId() > 0) {
				mensaje = "Usuario actualizado correctamente";
				Usuario usuarioOriginal = this.findById(usuario.getId()).getResultado();
				if (usuarioOriginal == null) {
					salida.setError(true);
					salida.setMensaje("No se econtró el usuario");
					return salida;
				}
				usuario.setClave(usuarioOriginal.getClave());
			} else {
				if (this.isRegistered(usuario)) {
					salida.setError(true);
					salida.setMensaje("El usuario con el email " + usuario.getEmail() + " ya se encuentra registrado");
					return salida;
				}
				mensaje = "Usuario registrado correctamente";
				usuario.setClave(passwordEncoder.encode(usuario.getClave()));
			}
			usuarioRepository.save(usuario);
			List<UsuarioRole> roles = asignarRoles(usuario);
			ResultadoProc<List<UsuarioRole>> salidaRoles = usuarioRolesService.saveAll(roles);
			if (salidaRoles.isError()) {
				salida.setError(true);
				salida.setMensaje(
						"El usuario fue registrado correctamente, pero ocurrio un problema al intentar asgnar el o los reoles");
				return salida;
			}
			salida.setMensaje(mensaje);
			usuario.setRoles(roles);
			salida.setResultado(usuario);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = usuario.getId() > 0 ? "actualizar" : "registrar";
			salida.setMensaje("Se produjo un error inesperado al intentar " + accion + " el usuario");
		}
		return salida;
	}

	@Override
	public ResultadoProc<Usuario> changeState(int usuarioId) {
		ResultadoProc<Usuario> salida = new ResultadoProc<Usuario>();
		try {
			String mensaje = "";
			Usuario usuarioOriginal = this.findById(usuarioId).getResultado();
			if (usuarioId > 0) {
				if (usuarioOriginal == null) {
					salida.setError(true);
					salida.setMensaje("No se econtró el usuario");
					return salida;
				}
				usuarioOriginal.setActivo(!usuarioOriginal.isActivo());
				if (usuarioOriginal.isActivo()) {
					mensaje = "El usuario " + usuarioOriginal.getNombreCompleto() + " está activo";
				} else {
					mensaje = "El usuario " + usuarioOriginal.getNombreCompleto() + " está inactivo";
				}
			}
			usuarioRepository.save(usuarioOriginal);
			salida.setMensaje(mensaje);
			salida.setResultado(usuarioOriginal);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.setMensaje("Se produjo un error inesperado al intentar cambiar el estado del usuario");
		}
		return salida;
	}

	private List<UsuarioRole> asignarRoles(Usuario usuario) {
		List<UsuarioRole> roles = new ArrayList<>();
		for (int i = 0; i < usuario.getRoles().size(); i++) {
			UsuarioRole usuarioRole = new UsuarioRole();
			usuarioRole.setId(usuario.getRoles().get(i).getId());
			usuarioRole.setRol(new Role(usuario.getRoles().get(i).getRol().getId()));
			usuarioRole.setUsuario(new Usuario(usuario.getId()));
			roles.add(usuarioRole);
		}
		return roles;
	}

	/**
	 * Indica si el usuario ya está registrado
	 * 
	 * @param usuario
	 * @return si es <code>true</code> es porque el usuario esta registrado
	 */
	private boolean isRegistered(Usuario usuario) {
		if (this.findByEmailOrUsuario(usuario.getEmail()).getResultado() != null) {
			return true;
		} else {
			return false;
		}
	}

}
