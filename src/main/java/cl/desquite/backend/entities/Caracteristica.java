package cl.desquite.backend.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "caracteristicas")
@NoArgsConstructor
public class Caracteristica implements Serializable {

	private static final long serialVersionUID = 4760426588049428730L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String descripcion;
	private boolean activo;

	@ManyToOne
	@JoinColumn(name = "tipo_caracteristica_id")
	private TipoCaracteristica tipoCaracteristica;
}
