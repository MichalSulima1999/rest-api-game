package com.michael1099.rest_rpg.email;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.VerificationEmailSendErrorException;
import com.michael1099.rest_rpg.statistics.observer.LevelUpObserver;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.UnsupportedEncodingException;

// Tydzień 6 - Observer
// Ta klasa implementuje LevelUpObserver i posiada logikę, która pozwala na wysyłkę maila po osiągnięciu poziomu przez postać.
// Klasa character ma listę LevelUpObserver i odpowiednie metody do dodawania i usuwania observerów.
// W klasie Character w metodzie earnXp jest wywoływany observer w momencie, gdy postać osiągnie nowy poziom.
@Service
@RequiredArgsConstructor
@Validated
public class EmailService implements LevelUpObserver {

    private final JavaMailSender mailSender;

    @Transactional
    @Override
    public void onLevelUp(Character character) {
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
// Koniec Tydzień 6 - Observer
