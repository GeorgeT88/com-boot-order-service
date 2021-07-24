package com.boot.order.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.boot.services.model.Email;
import com.boot.services.model.Order;
import com.boot.services.model.User;

@Service
public class EmailService {

	private static final String ORDER_EMAIL_TEMPLATE = "/templates/order-email-template.vm";

	@Autowired
	JavaMailSender emailSender;

	@Autowired
	VelocityEngine velocityEngine;

	public void sendOrderEmail(Order order) throws MessagingException {

		Email email = new Email();
		User user = order.getUser();

		email.setEmailFrom("springStore@noreply.com");
		email.setEmailTo(user.getEmail());
		email.setEmailSubject("SpringStore confirmation Email");
		
		

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("uuid", order.getUuid());
		model.put("products", order.getProductList());
		model.put("total", order.getTotal());
		model.put("adress", order.getUser().getDeliveryAddress());
		email.setModel(model);

		MimeMessage mimeMessage = emailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		mimeMessageHelper.setSubject(email.getEmailSubject());
		mimeMessageHelper.setFrom(email.getEmailFrom());
		mimeMessageHelper.setTo(email.getEmailTo());
		email.setEmailContent(getContentFromTemplate(email.getModel(), ORDER_EMAIL_TEMPLATE));
		mimeMessageHelper.setText(email.getEmailContent(), true);

		emailSender.send(mimeMessageHelper.getMimeMessage());

	}

	public String getContentFromTemplate(Map<String, Object> model, String templatePath) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, model));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

}
