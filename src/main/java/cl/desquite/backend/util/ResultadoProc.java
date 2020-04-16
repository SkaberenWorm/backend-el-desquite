package cl.desquite.backend.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Entidad que contiene los datos para la salida de la petición
 * 
 * @author Ismael Cuevas
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ResultadoProc<T> {
	private boolean error;
	private String mensaje;
	private T resultado;

	public ResultadoProc() {
		this.error = false;
		this.resultado = null;
		this.mensaje = null;
	}

}
