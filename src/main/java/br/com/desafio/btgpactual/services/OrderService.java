package br.com.desafio.btgpactual.services;

import br.com.desafio.btgpactual.model.Order;
import br.com.desafio.btgpactual.request.OrderRequest;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    BigDecimal searchTotalOrderValue(Long orderNumber);
    List<Order> getAllOrdersByCustomer(Long customerCode);
    Long quantityOrderByCustomer(Long customerCode);
    void create(OrderRequest orderRequest);
}
