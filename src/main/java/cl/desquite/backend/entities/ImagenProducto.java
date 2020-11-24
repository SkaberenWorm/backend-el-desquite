package cl.desquite.backend.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "imagenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenProducto implements Serializable {

	private static final long serialVersionUID = 5339201097303043913L;

	@Id
	private String id;

	private String extension;
	private boolean princial;

	@Transient
	private String imagen;

	public String getImagen() {
		return this.id + "." + this.extension;
	}

	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;
}
