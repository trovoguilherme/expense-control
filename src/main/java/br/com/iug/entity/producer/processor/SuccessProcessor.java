package br.com.iug.entity.producer.processor;


import br.com.iug.entity.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuccessProcessor {

    private final KafkaProducer kafkaProducer;

    @Value("${topic.name}")
    private String topic;

    public void process(String value) {
        kafkaProducer.produce(topic, value);
    }

}
