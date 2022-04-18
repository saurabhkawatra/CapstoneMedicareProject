package com.Medicare.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	JavaMailSender mailSender;
	
	public String sendSimpleTextEmail(String receiverEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(receiverEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
		return "Email Sent Successfully";
	}

}
