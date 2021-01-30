package cl.desquite.backend.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = { "roles" })
@ToString(exclude = { "roles" })
public class Usuario implements Serializable {

	private static final long serialVersionUID = -417381428394359888L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	private String nombre;
	@NotNull
	private String apellidos;
	@NotNull
	@Column(name = "correo")
	private String email;
	@JsonIgnore
	@Column(name = "clave")
	private String password;
	private boolean activo;
	@Transient
	private String nombreCompleto;
	@Column(name = "is_using_2fa")
	private boolean isUsing2FA;
	@JsonIgnore
	private String secret;
	@JsonIgnore
	private int intentosLogin;

	@JsonIgnore
	@Column(name = "validated_2fa")
	private boolean validated2Fa;

	@JsonIgnore
	private boolean validatedLogin;

	public Usuario() {
	}

	public Usuario(int id) {
		this.id = id;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
	@JsonIgnoreProperties(value = { "privilegios" })
	private Set<Role> roles;

	public String getEmail() {
		return this.email.toLowerCase().trim();
	}

	public String getNombreCompleto() {
		return (this.nombre + " " + this.apellidos);
	}

	public String getPassword() {
		if (this.password == null) {
			return "";
		}
		return this.password;
	}

	public Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
		return getGrantedAuthorities(getPrivileges(usuario.getRoles()));
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

}
