package cl.desquite.backend.models;

import lombok.Data;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.entities.UsuarioToken;

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
                this.mensajeBtn = "crear clave";
                this.cuerpoEmail = "<p style=\"font-size: 15px; margin-bottom: 0px; font-weight: bold;\">"
                                + this.nombreUsuario.toUpperCase() + ", </p>";
                this.cuerpoEmail += "<p style=\"margin-top: 5px;\"> Para crear tu clave y acceder a " + nombreSistema
                                + " debes hacer clic en el siguiente botón.</p>";
                this.footerEmail = "<p> <b>Recuerda que tienes máximo 24 horas para crear tu clave</b>, posterior a este tiempo expirará y deberás solicitar una nueva. </p>";
        }

        public void setEmailForResetPassword(String urlBaseSistemaFront, Usuario usuario, UsuarioToken usuarioToken) {
                this.sendTo = usuario.getEmail();
                this.nombreUsuario = usuario.getNombreCompleto();

                this.titulo = "RECUPERA TU CLAVE";
                this.subject = "Recuperación de clave";
                this.enlace = urlBaseSistemaFront + "cambiar-password/" + usuarioToken.getToken();
                this.mensajeBtn = "recuperar clave";
                this.cuerpoEmail = "<p style=\"font-size: 15px; margin-bottom: 0px; font-weight: bold;\">"
                                + this.nombreUsuario.toUpperCase() + ", </p>";
                this.cuerpoEmail += "<p style=\"margin-top: 5px;\"> Para resetear tu clave y acceder a " + nombreSistema
                                + " debes hacer clic en el siguiente botón.</p>";
                this.footerEmail = "<p> <b>Recuerda que tienes máximo 24 horas para cambiar tu clave</b>, posterior a este tiempo expirará y deberás solicitar una nueva. </p>";
        }

        public void setEmailForUnlockUser(String urlBaseSistemaFront, Usuario usuario, UsuarioToken usuarioToken) {
                this.sendTo = usuario.getEmail();
                this.nombreUsuario = usuario.getNombreCompleto();

                this.titulo = "Se ha bloqueado su cuenta";
                this.subject = "Bloqueo de seguridad";
                this.enlace = urlBaseSistemaFront + "desbloquear/" + usuarioToken.getToken();
                this.mensajeBtn = "Desbloquear!";
                this.cuerpoEmail = "<p style=\"font-size: 15px; margin-bottom: 0px; font-weight: bold;\">"
                                + this.nombreUsuario.toUpperCase() + ", </p>";
                this.cuerpoEmail += "<p style=\"margin-top: 5px;\"> Para crear tu desbloquear tu cuenta y acceder a "
                                + nombreSistema + " debes hacer clic en el siguiente botón.</p>";
                this.footerEmail = "<p> <b>Recuerda que tienes máximo 24 horas para desbloquear tu cuenta</b>, posterior a este tiempo expirará y deberás solicitar directamente al administrador del sistema. </p>";
        }
}
