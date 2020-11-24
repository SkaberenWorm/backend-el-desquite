package cl.desquite.backend.entities;

import java.io.Serializable;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
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

	public Usuario(int id) {
		this.id = id;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
	@JsonIgnoreProperties(value = { "privilegios" })
	private Set<Role> roles;

	public String getNombreCompleto() {
		return (this.nombre + " " + this.apellidos);
	}

	public String getPassword() {
		if (this.password == null) {
			return "";
		}
		return this.password;
	}

}
