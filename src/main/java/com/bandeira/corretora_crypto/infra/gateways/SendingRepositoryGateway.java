package com.bandeira.corretora_crypto.infra.gateways;


import com.bandeira.corretora_crypto.application.gateways.SendingEmailsGateway;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import com.bandeira.corretora_crypto.infra.util.RandomString;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;


public class SendingRepositoryGateway implements SendingEmailsGateway {

    private final JavaMailSender emailSender;

    public SendingRepositoryGateway(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String setFrom = "";
    public String senderName = "";


    public String sendEmailToValidateEmail(UserEntity user)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        var code = RandomString.generateRandomString(6);

        helper.setFrom(setFrom, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject("Seu código de verificação: ...");
        helper.setText(code);

        emailSender.send(message);

        return code;
    }
}
