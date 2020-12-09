package cl.desquite.backend.entities;

import lombok.Data;

@Data
public class EmailData {
    private final String nombreSistema = "El Desquite";

    private String titulo;
    private String sendTo;
    private String nombreUsuario;
    private String subject;
    private String enlace;
    private String mensajeBtn;
    private String cuerpoEmail;
    private String footerEmail;

    public EmailData() {
        this.titulo = "";
        this.sendTo = "";
        this.nombreUsuario = "";
        this.subject = "";
        this.enlace = "";
        this.mensajeBtn = "";
        this.cuerpoEmail = "";
        this.footerEmail = "";
    }

    public void setEmailForNewPassword(String urlBaseSistemaFront, Usuario usuario, UsuarioToken usuarioToken) {
        this.sendTo = usuario.getEmail();
        this.nombreUsuario = usuario.getNombreCompleto();

        this.titulo = "CREA TU CLAVE";
        this.subject = "Crea tu clave";
        this.enlace = urlBaseSistemaFront + "crear-password/" + usuarioToken.getToken();
        this.mensajeBtn = "llevame!";
        this.cuerpoEmail = "<p mb-1>" + this.nombreUsuario + ", <p>";
        this.cuerpoEmail += "<p mb-1> Para crear tu Clave y acceder a " + nombreSistema
                + " debes hacer clic en el siguiente botón.</p>";
        this.footerEmail = "<p mb-1> <b>Recuerda que tienes máximo 24 horas para crear tu Clave</b>, posterior a este tiempo expirará y deberás solicitar una nueva. </p>";
    }

    public void setEmailForResetPassword(String urlBaseSistemaFront, Usuario usuario, UsuarioToken usuarioToken) {
        this.sendTo = usuario.getEmail();
        this.nombreUsuario = usuario.getNombreCompleto();

        this.titulo = "RECUPERA TU CLAVE";
        this.subject = "Recuperación de clave";
        this.enlace = urlBaseSistemaFront + "cambiar-password/" + usuarioToken.getToken();
        this.mensajeBtn = "llevame!";
        this.cuerpoEmail = "<p mb-1>" + this.nombreUsuario + ", <p>";
        this.cuerpoEmail += "<p mb-1> Para resetear tu Clave y acceder a " + nombreSistema
                + " debes hacer clic en el siguiente botón.<p>";
        this.footerEmail = "<p mb-1> <b>Recuerda que tienes máximo 24 horas para cambiar tu Clave</b>, posterior a este tiempo expirará y deberás solicitar una nueva. </p>";
    }

    public void setEmailForUnlockUser(String urlBaseSistemaFront, Usuario usuario, UsuarioToken usuarioToken) {
        this.sendTo = usuario.getEmail();
        this.nombreUsuario = usuario.getNombreCompleto();

        this.titulo = "Se ha bloqueado su cuenta";
        this.subject = "Bloqueo de seguridad";
        this.enlace = urlBaseSistemaFront + "desbloquear/" + usuarioToken.getToken();
        this.mensajeBtn = "Desbloquear!";
        this.cuerpoEmail = "<p mb-1>" + this.nombreUsuario + ", <p>";
        this.cuerpoEmail += "<p mb-1> Para crear tu desbloquear tu cuenta y acceder a " + nombreSistema
                + " debes hacer clic en el siguiente botón.</p>";
        this.footerEmail = "<p mb-1> <b>Recuerda que tienes máximo 24 horas para desbloquear tu cuenta</b>, posterior a este tiempo expirará y deberás solicitar directamente al administrador del sistema. </p>";
    }
}
