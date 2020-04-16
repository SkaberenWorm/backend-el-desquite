package cl.desquite.backend.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {

	private static final long serialVersionUID = -417381428394359888L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	private String nombre;
	@NotNull
	private String apellidos;
	@Email
	@NotNull
	@Column(name = "correo")
	private String email;
	@JsonIgnore
	private String clave;
	@Column(name = "usuario")
	private String user;
	private boolean activo;
	@Transient
	private String nombreCompleto;

	public Usuario(int id) {
		this.id = id;
	}

	@Transient
	private String password;

	public void setPassword(String password) {
		this.clave = password;
		this.password = password;
	}

	public String getPassword() {
		return null;
	}

	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "usuario" })
	private List<UsuarioRole> roles;

	public String getNombreCompleto() {
		return (this.nombre + " " + this.apellidos);
	}

}
