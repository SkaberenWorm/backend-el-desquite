package cl.desquite.backend.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface IEmailService {

	@Async
	CompletableFuture<Boolean> sendEmail(String titulo, String sendTo, String nombreUsuario, String subject,
			String mensaje, String enlace, String mensajeBtn);

	Boolean sendEmailNoAsync(String titulo, String sendTo, String nombreUsuario, String subject, String mensaje,
			String enlace, String mensajeBtn);

}
