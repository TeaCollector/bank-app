package ru.alex.msdossier.config;

import java.util.HashMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.alex.msdossier.dto.EmailMessage;


@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, EmailMessage> consumerFactory() {
        var consumerProperties = new HashMap<String, Object>();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "email-sender");
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumerProperties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        var emailMessageJsonDeserializer = new JsonDeserializer<>(EmailMessage.class);

        return new DefaultKafkaConsumerFactory<>(
            consumerProperties,
            new StringDeserializer(),
            emailMessageJsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailMessage> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailMessage>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
