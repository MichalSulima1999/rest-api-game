package com.michael1099.rest_rpg.email;

import com.michael1099.rest_rpg.exceptions.VerificationEmailSendErrorException;
import com.michael1099.rest_rpg.statistics.observer.LevelUpEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Validated
public class EmailService {

    private final JavaMailSender mailSender;

    @EventListener
    @Transactional
    public void sendEmailOnOrderPlacement(LevelUpEvent event) {
        var character = event.getCharacter();
        var user = character.getUser();

        String toAddress = user.getEmail();
        String fromAddress = "server@restrpg.com";
        String senderName = "RPG";
        String subject = "Level UP!";
        String content = "Dear [[name]],<br>"
                + "Your character [[characterName]] has leveled up!<br>"
                + "New level: [[level]]<br>"
                + "Congratulations,<br>"
                + "Rest RPG.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", user.getUsername());
            content = content.replace("[[characterName]]", character.getName());
            content = content.replace("[[level]]", Integer.toString(character.getStatistics().getCurrentLevel()));

            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new VerificationEmailSendErrorException();
        }

        mailSender.send(message);
    }
}
