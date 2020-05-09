package cl.desquite.backend.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "privilegios")
@NoArgsConstructor
public class Privilegio implements Serializable {

	private static final long serialVersionUID = 8474590121145515460L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nombre;
	private String descripcion;
	private boolean activo;

	public Privilegio(int id) {
		this.id = id;
	}

	// @ManyToMany(mappedBy = "privilegios")
	// @JsonIgnoreProperties(value = { "privilegios" })
	// private List<Role> roles;

}
