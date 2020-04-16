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
@Table(name = "imagenes")
@Data
@NoArgsConstructor
public class ImagenProducto implements Serializable {

	private static final long serialVersionUID = 5339201097303043913L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String extension;
	private boolean princial;

	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;
}
