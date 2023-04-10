package br.com.desafio.btgpactual.consumer;

import br.com.desafio.btgpactual.exception.OrderException;
import br.com.desafio.btgpactual.request.OrderRequest;
import br.com.desafio.btgpactual.services.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;
@Component
public class ConsumerQueue {

    private static final Logger LOGGER = Logger.getLogger(ConsumerQueue.class.getName());
    private final OrderService orderService;

    public ConsumerQueue(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${queue.consumer}")
    public void receiveMessage(@Payload OrderRequest orderRequest) {

        try {
            LOGGER.info("Message received: "+ orderRequest);
            orderService.create(orderRequest);
        } catch (Exception e) {
            throw new OrderException(e.getMessage());
        }
    }
}
