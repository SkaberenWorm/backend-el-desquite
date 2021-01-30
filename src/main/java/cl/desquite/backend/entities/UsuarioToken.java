package cl.desquite.backend.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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

@Data
@Table(name = "usuario_tokens")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioToken implements Serializable {

    private static final long serialVersionUID = 7854261105457341634L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;
    private Date fechaCaducidad;
    private boolean activo;
    private String tipo;

    @ManyToOne()
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public UsuarioToken(Usuario usuario) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        this.fechaCaducidad = date;
        this.token = UUID.randomUUID().toString();
        this.activo = true;
        this.usuario = usuario;
    }

    public boolean isForNewPassword() {
        return this.tipo.toUpperCase().trim().equals("NEW_PASSWORD");
    }

    public void setForNewPassword() {
        this.tipo = "NEW_PASSWORD";
    }

    public boolean isForResetPassword() {
        return this.tipo.toUpperCase().trim().equals("RESET_PASSWORD");
    }

    public void setForResetPassword() {
        this.tipo = "RESET_PASSWORD";
    }

    public boolean isForUnlockUser() {
        return this.tipo.toUpperCase().trim().equals("UNLOCK_USUARIO");
    }

    public void setForUnlockedUser() {
        this.tipo = "UNLOCK_USUARIO";
    }

}
