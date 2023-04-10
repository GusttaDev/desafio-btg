package br.com.desafio.btgpactual.services.impl;

import br.com.desafio.btgpactual.exception.OrderException;
import br.com.desafio.btgpactual.model.Order;
import br.com.desafio.btgpactual.repositories.OrderRepository;
import br.com.desafio.btgpactual.request.OrderRequest;
import br.com.desafio.btgpactual.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final Validator validator;

    public OrderServiceImpl(OrderRepository orderRepository, Validator validator) {
        this.orderRepository = orderRepository;
        this.validator = validator;
    }

    @Override
    public void create(OrderRequest orderRequest) {

        validateRequest(orderRequest);

        Order order = buildOrder(orderRequest);

        orderRepository.save(order);
    }

    private void validateRequest(OrderRequest orderRequest) {
        StringBuilder sb = new StringBuilder();

        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<OrderRequest> violation : violations) {
                sb.append(violation.getMessage());
            }
            LOGGER.error("Error validating request ", sb);
            throw new OrderException(sb.toString());
        }
    }

    private static Order buildOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderCode(orderRequest.getOrderCode());
        order.setCustomerCode(orderRequest.getCustomerCode());

        orderRequest.getItems().forEach(order::addItem);
        return order;
    }

    @Override
    public BigDecimal searchTotalOrderValue(Long orderNumber) {

        Order order = findOrderOrThrowException(orderNumber);
        return order.calculateTotalValue();
    }

    @Override
    public List<Order> getAllOrdersByCustomer(Long customerCode) {
        return findOrderByCustomerOrThrowException(customerCode);
    }

    @Override
    public Long quantityOrderByCustomer(Long customerCode) {
        return findCountByCustomerCodeOrThrowException(customerCode);
    }

    private Long findCountByCustomerCodeOrThrowException(Long customerCode) {
        return orderRepository.countByCustomerCode(customerCode).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource "+ customerCode+ " not found!"));
    }

    private List<Order> findOrderByCustomerOrThrowException(Long customerCode) {
        return orderRepository.findByCustomerCode(customerCode).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource "+ customerCode+ " not found!"));
    }

    private Order findOrderOrThrowException(Long orderNumber) {
        return orderRepository.findByOrderCode(orderNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource "+ orderNumber+ " not found!"));
    }


}
