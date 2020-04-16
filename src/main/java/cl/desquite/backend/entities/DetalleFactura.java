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
@Table(name = "detalles")
@Data
@NoArgsConstructor
public class DetalleFactura implements Serializable {

	private static final long serialVersionUID = -2226527957486552526L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "factura_id")
	private Factura factura;

	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;

	private int precio;
	private int cantidad;
	private int descuento;
	private int total;

}
