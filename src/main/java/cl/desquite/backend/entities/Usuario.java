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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
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
	private String clave;
	private boolean activo;
	@Transient
	private String nombreCompleto;

	public Usuario(int id) {
		this.id = id;
	}

	@Transient
	@JsonInclude(value = Include.NON_NULL)
	private String password;

	public void setPassword(String password) {
		this.clave = password;
		this.password = password;
	}

	public String getPassword() {
		return null;
	}

	@JsonIgnoreProperties(value = { "privilegios", "activo" })
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
	private Set<Role> roles;

	public String getNombreCompleto() {
		return (this.nombre + " " + this.apellidos);
	}

}
