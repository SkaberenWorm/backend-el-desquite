package cl.desquite.backend.utils;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ResultadoProc<T> implements Serializable {

    private static final long serialVersionUID = 798285194512418864L;
    private final T resultado;
    private final String mensaje;
    private final boolean error;

    private ResultadoProc(Builder<T> builder) {
        this.resultado = builder.resultado;
        this.mensaje = builder.mensaje;
        this.error = builder.error;
    }

    public static class Builder<B> {
        private B resultado;
        private String mensaje;
        private boolean error;

        public ResultadoProc<B> fallo(String mesajeError) {
            this.error = true;
            this.mensaje = mesajeError;
            return this.build();
        }

        public ResultadoProc<B> exitoso(B resultado) {
            this.error = false;
            this.resultado = resultado;
            return this.build();
        }

        public ResultadoProc<B> exitoso(B resultado, String mesajeError) {
            this.resultado = resultado;
            this.mensaje = mesajeError;
            return this.build();
        }

        public ResultadoProc<B> build() {
            ResultadoProc<B> resultadoProc = new ResultadoProc<B>(this);
            return resultadoProc;
        }

    }
}