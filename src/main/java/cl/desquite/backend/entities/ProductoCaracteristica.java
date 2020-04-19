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
@Table(name = "producto_caracteristicas")
@NoArgsConstructor
public class ProductoCaracteristica implements Serializable {

	private static final long serialVersionUID = -6878677585573351668L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "caracteristica_id")
	private Caracteristica caracteristica;

	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;

}
