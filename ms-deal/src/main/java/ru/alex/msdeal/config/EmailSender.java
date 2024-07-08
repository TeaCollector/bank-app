package ru.alex.msdeal.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.alex.msdeal.dto.EmailMessage;


@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void sendMail(EmailMessage message) {
        kafkaTemplate.send(message.theme().name(), message);
        log.info("Message {} was send to topic {}", message, message.theme().name());
    }
}
