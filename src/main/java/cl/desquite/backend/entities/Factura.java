package cl.desquite.backend.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "facturas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura implements Serializable {

	private static final long serialVersionUID = 86337049629527795L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date fecha;
	private int total;

	@ManyToOne
	@JoinColumn(name = "usuario_vendedor_id")
	private Usuario vendedor;

	@ManyToOne
	@JoinColumn(name = "medio_pago_id")
	private MedioPago medioPago;
}
