package cl.desquite.backend.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "grupo_privilegios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "privilegios" })
@ToString(exclude = { "privilegios" })
public class GrupoPrivilegio implements Serializable {

	private static final long serialVersionUID = 773944317678163276L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String nombre;
	private int orden;
	private boolean activo;

	@OneToMany(mappedBy = "grupo")
	@JsonIgnoreProperties(value = { "grupo" })
	@Where(clause = "activo = 1")
	private Set<Privilegio> privilegios;

}
