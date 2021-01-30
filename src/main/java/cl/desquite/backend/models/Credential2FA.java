package cl.desquite.backend.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credential2FA implements Serializable {

    private static final long serialVersionUID = -5969150006702323643L;

    private String usuario;
    private String code;

    public String getUsuario() {
        if (this.usuario == null) {
            return "";
        }
        return this.usuario.trim().toLowerCase();
    }
}
