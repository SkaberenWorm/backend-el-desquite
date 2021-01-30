package cl.desquite.backend.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import cl.desquite.backend.models.EmailData;

public interface IEmailService {

	@Async
	CompletableFuture<Boolean> sendEmail(EmailData emailData);

	Boolean sendEmailNoAsync(EmailData emailData);

}
