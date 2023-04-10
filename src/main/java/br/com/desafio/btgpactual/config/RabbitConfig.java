package br.com.desafio.btgpactual.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${queue.consumer}")
    private String consumerQueue;

    @Value("${queue.dlq}")
    private String dqlQueue;
    @Value("${exchange}")
    private String exchange;

    @Value("${exchange-dlx}")
    private String exchangeDlx;


    public Queue createConsumerQueue() {
        return QueueBuilder.durable(consumerQueue)
                .deadLetterExchange(exchangeDlx)
                .build();
    }

    public Queue createDLQQueue() {
        return QueueBuilder.durable(dqlQueue)
                .build();
    }

    public FanoutExchange createExchange() {
        return ExchangeBuilder.fanoutExchange(exchange).build();
    }
    public FanoutExchange createExchangeDlx() {
        return ExchangeBuilder.fanoutExchange(exchangeDlx).build();
    }

    @Bean
    public Declarables declarables() {
        createConsumerQueue();
        createDLQQueue();
        createExchange();
        createExchangeDlx();

        return new Declarables(
                createConsumerQueue(),
                createDLQQueue(),
                createExchange(),
                createExchangeDlx(),
                BindingBuilder.bind(createConsumerQueue()).to(createExchange()),
                BindingBuilder.bind(createDLQQueue()).to(createExchangeDlx())
        );
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> startRabbitAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


}
