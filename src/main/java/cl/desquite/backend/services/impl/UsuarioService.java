package cl.desquite.backend.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.entities.Privilegio;
import cl.desquite.backend.entities.Role;
import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioToken;
import cl.desquite.backend.repositories.UsuarioRepository;
import cl.desquite.backend.services.IEmailService;
import cl.desquite.backend.services.IUsuarioRolService;
import cl.desquite.backend.services.IUsuarioService;
import cl.desquite.backend.services.IUsuarioTokenService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.ResultadoProc.Builder;
import cl.desquite.backend.utils.Util;

@CommonsLog
@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	IUsuarioRolService usuarioRolesService;

	@Autowired
	IUsuarioTokenService usuarioTokenService;

	@Autowired
	IEmailService emailService;

	@Value("${sistema-front-url-base}")
	private String urlBaseSistemaFront;

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

		if (!usuario.isActivo()) {
			log.info("Usuario inactivo");
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
				salida.fallo("No se ha encontrado el usuario");
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
	public ResultadoProc<Page<Usuario>> findAllPaginatedWithSearch(PageRequest pageable, String buscador) {
		Builder<Page<Usuario>> salida = new Builder<Page<Usuario>>();
		try {
			Page<Usuario> usuarios = usuarioRepository.findAllPaginatedWithSearch(buscador, pageable);
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
				usuarioRepository.save(usuario);
				// * Creamos el token
				UsuarioToken usuarioToken = this.usuarioTokenService.save(new UsuarioToken(usuario)).getResultado();
				// Enviamos correo para que el usuario ingrese su nueva password
				this.sentEmailForNewPassword(usuario, usuarioToken);

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
				Usuario usuarioOriginal = this.findById(usuario.getId()).getResultado();
				if (usuarioOriginal == null) {
					salida.fallo("No se econtró el usuario");
					return salida.build();
				}
				usuario.setPassword(usuarioOriginal.getPassword());
				usuarioRepository.save(usuario);
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
			Usuario usuarioOriginal = this.findById(usuarioId).getResultado();
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

	@Override
	public ResultadoProc<Boolean> cambiarClave(int usuarioId, String password) {
		ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
		try {
			Usuario usuario = this.findById(usuarioId).getResultado();
			if (usuario == null) {
				return salida.fallo("No se encontró el usuario");
			}
			usuario.setPassword(passwordEncoder.encode(password));
			this.usuarioRepository.save(usuario);
			return salida.exitoso(true);
		} catch (Exception e) {
			Util.printError("cambiarClave(" + usuarioId + ", \"**********\")", e);
			return salida.fallo("Se produjo un error inespedado al intentar cambiar la clave");
		}
	}

	@Override
	public ResultadoProc<Boolean> createTokenForResetPassword(Usuario usuario) {
		ResultadoProc.Builder<Boolean> salida = new ResultadoProc.Builder<Boolean>();
		try {
			UsuarioToken usuarioToken = this.usuarioTokenService.save(new UsuarioToken(usuario)).getResultado();
			boolean sendEmail = this.sentEmailForChangePassword(usuario, usuarioToken);
			if (!sendEmail) {
				return salida.fallo("Se produjo un error inesperado al intentar notificarle al usuario");
			}
			return salida.exitoso(true, "Se a creado un nuevo token para el usuario " + usuario.getNombreCompleto()
					+ ", se le ha notificado por correo");
		} catch (Exception e) {
			Util.printError("createTokenForResetPassword(" + usuario.toString() + ")", e);
			return salida
					.fallo("Se produjo un error inesperado al intentar generar el token o al notificarle al usuario");
		}
	}

	/**
	 * Envia el correo al usuario para el ingreso de su clave
	 * 
	 * @param usuario
	 * @param usuarioToken
	 */
	@Async
	private CompletableFuture<Boolean> sentEmailForNewPassword(Usuario usuario, UsuarioToken usuarioToken) {
		try {

			if (usuarioToken != null) {
				String token = usuarioToken.getToken();

				String titulo = "Configura tu clave";
				String sendTo = usuario.getEmail();
				String nombreUsuario = usuario.getNombre() + " " + usuario.getApellidos();
				String subject = "Haz sido registrado en 'El Desquite'";
				String enlace = this.urlBaseSistemaFront + "cambiar-password/" + token;
				String mensajeBtn = "llevame!";
				String cuerpoEmail = "<p mb-1>" + nombreUsuario + ", <p>";
				cuerpoEmail += "<p mb-1> Presiona el botón e ingresa tu clave para finalizar el proceso de registro<p>";

				this.emailService.sendEmail(titulo, sendTo, nombreUsuario, subject, cuerpoEmail, enlace, mensajeBtn);
			}
		} catch (Exception e) {
			Util.printError("sentEmail(" + usuario + ", " + usuarioToken + ")", e);
		}
		return CompletableFuture.completedFuture(true);
	}

	private boolean sentEmailForChangePassword(Usuario usuario, UsuarioToken usuarioToken) {
		try {
			if (usuarioToken != null) {
				String token = usuarioToken.getToken();

				String titulo = "Cambia tu clave";
				String sendTo = usuario.getEmail();
				String nombreUsuario = usuario.getNombre() + " " + usuario.getApellidos();
				String subject = "Solicitud cambio de clave";
				String enlace = this.urlBaseSistemaFront + "cambiar-password/" + token;
				String mensajeBtn = "Cambiar";
				String cuerpoEmail = "<p mb-1>" + nombreUsuario + ", <p>";
				cuerpoEmail += "<p mb-1> Se ha solicitado un cambio de clave, para continuar presiona el botón<p>";

				return this.emailService.sendEmailNoAsync(titulo, sendTo, nombreUsuario, subject, cuerpoEmail, enlace,
						mensajeBtn);
			} else {
				return false;
			}
		} catch (Exception e) {
			Util.printError("sentEmailForChangePassword(" + usuario + ", " + usuarioToken + ")", e);
			return false;
		}
	}

	/**
	 * Indica si el usuario ya está registrado
	 * 
	 * @param usuario
	 * @return si es <code>true</code> es porque el usuario esta registrado
	 */
	private boolean isRegistered(Usuario usuario) {
		if (this.findByEmail(usuario.getEmail()).getResultado() != null) {
			return true;
		} else {
			return false;
		}
	}

}
