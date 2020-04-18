package curso.api.rest.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class ServiceEnviaEmail {
	
	private String userName = "enviarEmailRecuperacao@gmail.com";
	private String senha = "teste123*";

	public void enviarEmail(String assunto, String emailDestino, String mensagem) throws MessagingException {
		
		//propriedades para envio de email
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true"); //autorização
		properties.put("mail.smtp.starttls", "true"); //autenticacao
		properties.put("mail.smtp.host", "smtp.gmail.com"); //servidor google
		properties.put("mail.smtp.port", "465"); //porta servidor
		properties.put("mail.smtp.socketFactory.port", "465"); //expecifica porta socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //classe de conexão socket
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, senha);
			}
		});
		
		Address[] toUser = InternetAddress.parse(emailDestino);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName)); //quem esta enviando
		message.setRecipients(Message.RecipientType.TO, toUser); // para quem vai o e-mail
		message.setSubject(assunto); //assunto e-mail
		message.setText(mensagem); //conteudo
		
		Transport.send(message);
	}
	
	
}
