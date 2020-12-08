package cl.desquite.backend.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.services.IEmailService;
import cl.desquite.backend.utils.Util;

@CommonsLog
@Service
public class EmailService implements IEmailService {

	private JavaMailSender mailSender;

	@Value(value = "classpath:template_email/template.html")
	private Resource templateEmail;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Value("${production}")
	private boolean production;

	@Async
	@Override
	public CompletableFuture<Boolean> sendEmail(String titulo, String sendTo, String nombreUsuario, String subject,
			String mensaje, String enlace, String mensajeBtn) {

		StringBuilder out = new StringBuilder();
		String line;
		final String email = sendTo;
		final String[] emails = new String[] { "ismael.c.26a@gmail.com" };
		try {

			String template = "";
			BufferedReader reader3 = new BufferedReader(new InputStreamReader(templateEmail.getInputStream()));
			line = "";
			out = new StringBuilder();
			while ((line = reader3.readLine()) != null) {
				out.append(line);
			}
			template = out.toString().replace("_title_", titulo.trim());
			template = template.replace("_cuerpocorreo_", mensaje.trim());
			template = template.replace("_enlace_", enlace.trim());
			template = template.replace("_mesaje_btn_", mensajeBtn.trim());
			template = template.replace("_date_", new Date().toString());

			final String txtemail = template;
			mailSender.send(new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws MessagingException {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					message.setFrom("El Desquite <no-reply@skaberen.cl>");
					message.setTo(email);
					if (!production) {
						message.setCc(emails);
					}
					message.setSubject(subject);
					message.setText(txtemail, true);
				}
			});

			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			Util.printError("sendEmail(\"" + titulo + "\", \"" + sendTo + "\", \"" + nombreUsuario + "\", \"" + subject
					+ "\", \"" + mensaje + "\", \"" + enlace + "\", \"" + mensajeBtn + "\")", e);
			return CompletableFuture.completedFuture(false);
		}
	}

	@Override
	public Boolean sendEmailNoAsync(String titulo, String sendTo, String nombreUsuario, String subject, String mensaje,
			String enlace, String mensajeBtn) {

		StringBuilder out = new StringBuilder();
		String line;
		final String email = sendTo;
		final String[] emails = new String[] { "ismael.c.26a@gmail.com" };
		try {

			String template = "";
			BufferedReader reader3 = new BufferedReader(new InputStreamReader(templateEmail.getInputStream()));
			line = "";
			out = new StringBuilder();
			while ((line = reader3.readLine()) != null) {
				out.append(line);
			}
			template = out.toString().replace("_title_", titulo.trim());
			template = template.replace("_cuerpocorreo_", mensaje.trim());
			template = template.replace("_enlace_", enlace.trim());
			template = template.replace("_mesaje_btn_", mensajeBtn.trim());
			template = template.replace("_date_", new Date().toString());

			final String txtemail = template;
			mailSender.send(new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws MessagingException {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					message.setFrom("El Desquite <no-reply@skaberen.cl>");
					message.setTo(email);
					if (!production) {
						message.setCc(emails);
					}
					message.setSubject(subject);
					message.setText(txtemail, true);
				}
			});
			return true;
		} catch (Exception e) {
			Util.printError("sendEmailV2NoAsync(\"" + titulo + "\", \"" + sendTo + "\", \"" + nombreUsuario + "\", \""
					+ subject + "\", \"" + mensaje + "\", \"" + enlace + "\", \"" + mensajeBtn + "\")", e);
			return false;
		}
	}

}
