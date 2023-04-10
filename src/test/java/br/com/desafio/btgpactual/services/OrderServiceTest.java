package br.com.desafio.btgpactual.services;

import br.com.desafio.btgpactual.exception.OrderException;
import br.com.desafio.btgpactual.model.Item;
import br.com.desafio.btgpactual.model.Order;
import br.com.desafio.btgpactual.repositories.OrderRepository;
import br.com.desafio.btgpactual.request.OrderRequest;
import br.com.desafio.btgpactual.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testCreateOrder() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderCode(1L);
        orderRequest.setCustomerCode(2L);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Product 1", 2, new BigDecimal("10.50")));
        items.add(new Item(2L, "Product 2", 3, new BigDecimal("20.00")));
        orderRequest.getItems().addAll(items);

        orderService.create(orderRequest);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void testCreateOrderWithValidationError() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderCode(null);
        orderRequest.setCustomerCode(2L);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Product 1", 2, new BigDecimal("10.50")));
        items.add(new Item(2L, "Product 2", 3, new BigDecimal("20.00")));
        orderRequest.getItems().addAll(items);

        ConstraintViolation<OrderRequest> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Order code cannot be null");
        Set<ConstraintViolation<OrderRequest>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(orderRequest)).thenReturn(violations);

        assertThrows(OrderException.class, () -> orderService.create(orderRequest));
    }


    @Test
    void testSearchTotalOrderValue_Success() {
        // given
        Long orderNumber = 123L;
        BigDecimal expectedTotalValue = new BigDecimal("100.0");

        Item item1 = new Item(1L, "Produto 1", 5, new BigDecimal("10.0"));
        Item item2 = new Item(2L, "Produto 2", 5, new BigDecimal("10.0"));
        Order order = new Order(123L, 456L);
        order.addItem(item1);
        order.addItem(item2);

        when(orderRepository.findByOrderCode(orderNumber)).thenReturn(Optional.of(order));

        BigDecimal totalValue = orderService.searchTotalOrderValue(orderNumber);

        verify(orderRepository, times(1)).findByOrderCode(orderNumber);
        assertEquals(expectedTotalValue, totalValue);
    }

    @Test
    void testSearchTotalOrderValue_ResponseStatusException() {
        Long orderNumber = 123L;

        when(orderRepository.findByOrderCode(orderNumber)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> orderService.searchTotalOrderValue(orderNumber));

        verify(orderRepository, times(1)).findByOrderCode(orderNumber);
    }

    @Test
    void testQuantityOrderByCustomer_Success() {
        Long customerCode = 123L;
        Long expectedQuantity = 10L;

        Mockito.when(orderRepository.countByCustomerCode(customerCode)).thenReturn(Optional.of(expectedQuantity));

        Long actualQuantity = orderService.quantityOrderByCustomer(customerCode);

        Assertions.assertEquals(expectedQuantity, actualQuantity);
        Mockito.verify(orderRepository, Mockito.times(1)).countByCustomerCode(customerCode);
    }

    @Test
    void testQuantityOrderByCustomer_ResponseStatusException() {
        Long customerCode = 123L;

        Mockito.when(orderRepository.countByCustomerCode(customerCode)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> orderService.quantityOrderByCustomer(customerCode));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals("Resource " + customerCode + " not found!", exception.getReason());
        Mockito.verify(orderRepository, Mockito.times(1)).countByCustomerCode(customerCode);
    }

    @Test
    void testGetAllOrdersByCustomer() {
        Long customerCode = 456L;

        Order order1 = new Order();
        order1.setId(123L);
        order1.setOrderCode(123L);
        order1.setCustomerCode(customerCode);

        Order order2 = new Order();
        order2.setId(456L);
        order2.setOrderCode(456L);
        order2.setCustomerCode(customerCode);

        List<Order> expectedOrders = Arrays.asList(order1, order2);

        when(orderRepository.findByCustomerCode(customerCode)).thenReturn(Optional.of(expectedOrders));

        List<Order> orders = orderService.getAllOrdersByCustomer(customerCode);

        assertEquals(expectedOrders, orders);
        verify(orderRepository, times(1)).findByCustomerCode(customerCode);
    }

    @Test
    void testGetAllOrdersByCustomerThrowsException() {
        Long customerCode = 123L;
        when(orderRepository.findByCustomerCode(customerCode)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.getAllOrdersByCustomer(customerCode);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Resource " + customerCode + " not found!", exception.getReason());
    }
}
