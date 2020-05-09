package cl.desquite.backend.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
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
import org.springframework.transaction.annotation.Transactional;

import cl.desquite.backend.entities.Privilegio;
import cl.desquite.backend.entities.Role;
import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioRole;
import cl.desquite.backend.repositories.UsuarioRepository;
import cl.desquite.backend.services.IUsuarioRolService;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.util.ResultadoProc;
import cl.desquite.backend.util.ResultadoProc.Builder;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	IUsuarioRolService usuarioRolesService;

	// @Autowired
	BCryptPasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		email = email.toLowerCase().trim();
		Usuario usuario = usuarioRepository.findByEmail(email);
		if (usuario == null) {
			log.info("Usuario ingresado no existe");
			throw new UsernameNotFoundException("Usuario o Clave incorrectos");
		}

		return new User(email, usuario.getPassword(), true, true, true, true, getAuthorities(usuario.getRoles()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private Set<String> getPrivileges(Collection<Role> roles) {
		Set<String> privileges = new HashSet<String>();
		Set<Privilegio> collection = new HashSet<Privilegio>();
		for (Role role : roles) {
			collection.addAll(role.getPrivilegios());
		}
		for (Privilegio item : collection) {
			privileges.add(item.getNombre());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(Set<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

	@Override
	public ResultadoProc<Usuario> findByEmail(String email) {
		Builder<Usuario> salida = new Builder<Usuario>();
		try {
			Usuario usuario = usuarioRepository.findByEmail(email);
			salida.exitoso(usuario, "Usuario encontrado correctamente");
			if (usuario == null) {
				salida.fallo("No se ha encontrado el usuario " + email);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar obtener el usuario");
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Usuario> findById(int usuarioId) {
		Builder<Usuario> salida = new Builder<Usuario>();
		try {
			Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
			if (usuario == null) {
				salida.fallo("No se ha encontrado el usuario con el código " + usuarioId);
			}
			salida.exitoso(usuario);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar obtener el usuario con el código " + usuarioId);
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Usuario> findByIdAndActivoTrue(int usuarioId) {
		Builder<Usuario> salida = new Builder<Usuario>();
		try {
			Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
			salida.exitoso(usuario);

			if (usuario == null) {
				salida.fallo("No se ha encontrado el usuario con el código " + usuarioId);
			}
			if (!usuario.isActivo()) {
				salida.fallo("El usuario con el código " + usuarioId + " se encuentra inactivo");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar obtener el usuario con el código " + usuarioId);
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Page<Usuario>> findAllPaginatedWithFilters(PageRequest pageable, String buscador) {
		Builder<Page<Usuario>> salida = new Builder<Page<Usuario>>();
		try {
			Page<Usuario> usuarios = usuarioRepository.findAllPaginatedWithFilters(buscador, pageable);
			salida.exitoso(usuarios);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar listar los usuarios");
		}
		return salida.build();
	}

	@Transactional
	@Override
	public ResultadoProc<Usuario> save(Usuario usuario) {
		Builder<Usuario> salida = new Builder<Usuario>();
		try {
			String mensaje = "";
			if (usuario.getId() == 0) {
				if (this.isRegistered(usuario)) {
					salida.fallo("El usuario con el email " + usuario.getEmail() + " ya se encuentra registrado");
					return salida.build();
				}
				mensaje = "Usuario registrado correctamente";
				usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

				usuarioRepository.save(usuario);
				List<UsuarioRole> roles = asignarRoles(usuario);

				ResultadoProc<Boolean> salidaDelete = usuarioRolesService.deleteAllByUsuario(usuario);
				if (salidaDelete.isError()) {
					salida.fallo(salidaDelete.getMensaje());
					return salida.build();
				}
				ResultadoProc<List<UsuarioRole>> salidaRoles = usuarioRolesService.saveAll(roles);
				if (salidaRoles.isError()) {
					salida.fallo(
							"El usuario fue registrado correctamente, pero ocurrio un problema al intentar asignar el o los roles");
					return salida.build();
				}
				salida.exitoso(usuario, mensaje);
			} else {
				salida.fallo("Se está intentando editar un usuario, esta acción no está permitida");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = usuario.getId() > 0 ? "actualizar" : "registrar";
			salida.fallo("Se produjo un error inesperado al intentar " + accion + " el usuario");
		}
		return salida.build();
	}

	@Transactional
	@Override
	public ResultadoProc<Usuario> update(Usuario usuario) {
		Builder<Usuario> salida = new Builder<Usuario>();
		try {
			String mensaje = "";
			if (usuario.getId() > 0) {
				mensaje = "Usuario actualizado correctamente";
				Usuario usuarioOriginal = this.findById(usuario.getId()).getSalida();
				if (usuarioOriginal == null) {
					salida.fallo("No se econtró el usuario");
					return salida.build();
				}
				usuario.setPassword(usuarioOriginal.getPassword());

				usuarioRepository.save(usuario);
				List<UsuarioRole> roles = asignarRoles(usuario);

				ResultadoProc<Boolean> salidaDelete = usuarioRolesService.deleteAllByUsuario(usuario);
				if (salidaDelete.isError()) {
					salida.fallo(salidaDelete.getMensaje());
					return salida.build();
				}
				ResultadoProc<List<UsuarioRole>> salidaRoles = usuarioRolesService.saveAll(roles);
				if (salidaRoles.isError()) {
					salida.fallo(
							"El usuario fue registrado correctamente, pero ocurrio un problema al intentar asignar el o los roles");
					return salida.build();
				}

				salida.exitoso(usuario, mensaje);
			} else {
				salida.fallo("Se está intentando registrar un usuario, esta acción no está permitida");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String accion = usuario.getId() > 0 ? "actualizar" : "registrar";
			salida.fallo("Se produjo un error inesperado al intentar " + accion + " el usuario");
		}
		return salida.build();
	}

	@Override
	public ResultadoProc<Usuario> changeState(int usuarioId) {
		Builder<Usuario> salida = new Builder<Usuario>();
		try {
			String mensaje = "";
			Usuario usuarioOriginal = this.findById(usuarioId).getSalida();
			if (usuarioId > 0) {
				if (usuarioOriginal == null) {
					salida.fallo("No se econtró el usuario");
					return salida.build();
				}
				usuarioOriginal.setActivo(!usuarioOriginal.isActivo());
				if (usuarioOriginal.isActivo()) {
					mensaje = "El usuario " + usuarioOriginal.getNombreCompleto() + " está activo";
				} else {
					mensaje = "El usuario " + usuarioOriginal.getNombreCompleto() + " está inactivo";
				}
			}
			usuarioRepository.save(usuarioOriginal);
			salida.exitoso(usuarioOriginal, mensaje);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			salida.fallo("Se produjo un error inesperado al intentar cambiar el estado del usuario");
		}
		return salida.build();
	}

	private List<UsuarioRole> asignarRoles(Usuario usuario) {
		List<UsuarioRole> roles = new ArrayList<>();
		for (Role rol : usuario.getRoles()) {
			roles.add(new UsuarioRole(0, usuario, rol));
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
		if (this.findByEmail(usuario.getEmail()).getSalida() != null) {
			return true;
		} else {
			return false;
		}
	}

}
