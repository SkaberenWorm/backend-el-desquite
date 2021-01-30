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

import cl.desquite.backend.models.EmailData;
import cl.desquite.backend.services.IEmailService;
import cl.desquite.backend.utils.Util;

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
	public CompletableFuture<Boolean> sendEmail(EmailData emailData) {

		StringBuilder out = new StringBuilder();
		String line;
		final String email = emailData.getSendTo();
		final String[] emails = new String[] { "ismael.c.26a@gmail.com" };
		try {

			String template = "";
			BufferedReader reader3 = new BufferedReader(new InputStreamReader(templateEmail.getInputStream()));
			line = "";
			out = new StringBuilder();
			while ((line = reader3.readLine()) != null) {
				out.append(line);
			}
			template = out.toString().replace("_title_", emailData.getTitulo().trim());
			template = template.replace("_cuerpocorreo_", emailData.getCuerpoEmail().trim());
			template = template.replace("_enlace_", emailData.getEnlace().trim());
			template = template.replace("_mesaje_btn_", emailData.getMensajeBtn().trim());
			template = template.replace("_footercorreo_", emailData.getFooterEmail());
			template = template.replace("_date_", new Date().toString());

			final String txtemail = template;
			mailSender.send(new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws MessagingException {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					message.setFrom("El Desquite <no-reply@skaberen.cl>");
					message.setTo(email);
					if (!production && !email.equals("ismael.c.26a@gmail.com")) {
						message.setBcc(emails);
					}
					message.setSubject(emailData.getSubject());
					message.setText(txtemail, true);
				}
			});

			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			Util.printError("sendEmail(" + emailData.toString() + ")", e);
			return CompletableFuture.completedFuture(false);
		}
	}

	@Override
	public Boolean sendEmailNoAsync(EmailData emailData) {

		StringBuilder out = new StringBuilder();
		String line;
		final String email = emailData.getSendTo();
		final String[] emails = new String[] { "ismael.c.26a@gmail.com" };
		try {

			String template = "";
			BufferedReader reader3 = new BufferedReader(new InputStreamReader(templateEmail.getInputStream()));
			line = "";
			out = new StringBuilder();
			while ((line = reader3.readLine()) != null) {
				out.append(line);
			}

			template = out.toString().replace("_title_", emailData.getTitulo().trim());
			template = template.replace("_cuerpocorreo_", emailData.getCuerpoEmail().trim());
			template = template.replace("_enlace_", emailData.getEnlace().trim());
			template = template.replace("_mesaje_btn_", emailData.getMensajeBtn().trim());
			template = template.replace("_footercorreo_", emailData.getFooterEmail());
			template = template.replace("_date_", new Date().toString());

			final String txtemail = template;
			mailSender.send(new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws MessagingException {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					message.setFrom("El Desquite <no-reply@skaberen.cl>");
					message.setTo(email);
					if (!production && !email.equals("ismael.c.26a@gmail.com")) {
						message.setBcc(emails);
					}
					message.setSubject(emailData.getSubject());
					message.setText(txtemail, true);
				}
			});
			return true;
		} catch (Exception e) {
			Util.printError("sendEmailNoAsync(" + emailData.toString() + ")", e);
			return false;
		}
	}

}
