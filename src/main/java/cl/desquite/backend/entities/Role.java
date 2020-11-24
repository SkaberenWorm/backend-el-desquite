package cl.desquite.backend.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = { "privilegios" })
@ToString(exclude = { "privilegios" })
public class Role implements Serializable {

	private static final long serialVersionUID = -8933379875810486482L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nombre;
	private String descripcion;
	private boolean activo;

	public Role(int id) {
		this.id = id;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "rol_privilegios", joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilegio_id", referencedColumnName = "id"))
	private Set<Privilegio> privilegios;

	// @Transient
	// private String privilegiosString;

	// public String getPrivilegiosString() {
	// String privilegiosDescrip = "";
	// if (this.getPrivilegios() != null) {
	// for (Privilegio privilegio : this.getPrivilegios()) {
	// privilegiosDescrip += privilegio.getDescripcion() + "\n";
	// }
	// }
	// return privilegiosDescrip;
	// }
}
