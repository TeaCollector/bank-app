package ru.alex.msdeal.config;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.alex.msdeal.dto.EmailMessage;
import ru.alex.msdeal.entity.constant.Theme;


@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, EmailMessage> producerFactory() {
        var kafkaProperties = new HashMap<String, Object>();

        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(kafkaProperties);
    }

    @Bean
    public KafkaTemplate<String, EmailMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin.NewTopics createKafkaTopics() {
        var newTopics = new ArrayList<NewTopic>();

        for (Theme theme : Theme.values()) {
            newTopics.add(new NewTopic(theme.name(), 1, (short) 3));
        }

        return new KafkaAdmin.NewTopics(newTopics.toArray(NewTopic[]::new));
    }
}

