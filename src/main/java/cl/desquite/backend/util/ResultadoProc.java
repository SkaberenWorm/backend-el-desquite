package cl.desquite.backend.util;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ResultadoProc<T> implements Serializable {

	private static final long serialVersionUID = 8914295316641368894L;
	private final T salida;
	private final String mensaje;
	private final boolean error;

	private ResultadoProc(Builder<T> builder) {
		this.salida = builder.salida;
		this.mensaje = builder.mensaje;
		this.error = builder.error;
	}

	public static class Builder<B> {
		private B salida;
		private String mensaje;
		private boolean error;

		public ResultadoProc<B> fallo(String mesajeError) {
			this.error = true;
			this.mensaje = mesajeError;
			return this.build();
		}

		public ResultadoProc<B> exitoso(B salida) {
			this.error = false;
			this.salida = salida;
			return this.build();
		}

		public ResultadoProc<B> exitoso(B salida, String mesajeError) {
			this.salida = salida;
			this.mensaje = mesajeError;
			return this.build();
		}

		public ResultadoProc<B> build() {
			ResultadoProc<B> resultadoProc = new ResultadoProc<B>(this);
			return resultadoProc;
		}

	}
}
