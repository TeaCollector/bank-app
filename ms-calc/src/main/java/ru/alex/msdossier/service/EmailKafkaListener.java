package ru.alex.msdossier.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.alex.msdossier.dto.EmailMessage;
import ru.alex.msdossier.dto.constant.Theme;


@Log4j2
@Service
@RequiredArgsConstructor
public class EmailKafkaListener {

    private final JavaMailSender mailSender;

    @Value("${ms-deal.address}")
    private String msDealAddress;

    @Async
    @KafkaListener(topics = { "FINISH_REGISTRATION" }, groupId = "email-sender")
    public void sendEmailFinishRegistration(EmailMessage message) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.address());
        mailMessage.setSubject(Theme.FINISH_REGISTRATION.name());
        mailMessage.setText("Ваша заявка предварительно одобрена, завершите оформление");

        mailSender.send(mailMessage);

        log.info("Message with theme: {}, was sent to: {}", message.theme(), message.address());
    }

    @Async
    @KafkaListener(topics = "CREATE_DOCUMENT", groupId = "email-sender")
    public void sendEmailCreateDocument(EmailMessage message) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.address());
        mailMessage.setSubject(Theme.CREATE_DOCUMENT.name());
        mailMessage.setText("<a href=\"http://%s/deal/document/%s/send\">Ссылка на формирование документов</a>"
            .formatted(msDealAddress, message.statementId()));

        mailSender.send(mailMessage);

        log.info("Message with theme: {}, was sent to: {}", message.theme(), message.address());
    }

    @Async
    @KafkaListener(topics = "SEND_DOCUMENT", groupId = "email-sender")
    public void sendEmailSendDocument(EmailMessage message) throws MessagingException, IOException {
        var mimeMessage = mailSender.createMimeMessage();
        var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(message.address());
        mimeMessageHelper.setSubject(Theme.FINISH_REGISTRATION.name());
        mimeMessageHelper.setText(("Документы для ознакомления находятся в письме." +
                                   "\nЕсли вы согласны, пожалуйста перейдите по это ссылке: " +
                                   "<a href=\"http://%s/deal/document/%s/sign\">подписать документы</a>").formatted(msDealAddress, message.statementId()));
        mimeMessageHelper.addAttachment("agreement.txt",
            new ByteArrayResource(Files.readAllBytes(Path.of("ms-dossier/src/main/resources/agreement.txt"))));

        mailSender.send(mimeMessage);

        log.info("Message with theme: {}, was sent to: {}", message.theme(), message.address());
    }

    @Async
    @KafkaListener(topics = "SEND_SES", groupId = "email-sender")
    public void sendEmailSesCode(EmailMessage message) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.address());
        mailMessage.setSubject(Theme.SEND_SES.name());
        mailMessage.setText(("Для подтверждения подписания документов отправьте этот код: %d\n" +
                             "<a href=\"http://%s/deal/document/%s/code\">На этот адрес</a>").formatted(message.sesCode(), msDealAddress, message.statementId()));

        mailSender.send(mailMessage);

        log.info("Message with theme: {}, was sent to: {}", message.theme(), message.address());
    }

    @Async
    @KafkaListener(topics = "CREDIT_ISSUED", groupId = "email-sender")
    public void sendEmailCreditIssued(EmailMessage message) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.address());
        mailMessage.setSubject(Theme.STATEMENT_DENIED.name());
        mailMessage.setText("Поздравляю вы взяли вредит!!!");

        mailSender.send(mailMessage);

        log.info("Message with theme: {}, was sent to: {}", message.theme(), message.address());
    }

    @Async
    @KafkaListener(topics = "STATEMENT_DENIED", groupId = "email-sender")
    public void sendEmailStatementDenied(EmailMessage message) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.address());
        mailMessage.setSubject(Theme.STATEMENT_DENIED.name());
        mailMessage.setText("Ваша заявка отклонена");

        mailSender.send(mailMessage);

        log.info("Message with theme: {}, was sent to: {}", message.theme(), message.address());
    }
}
