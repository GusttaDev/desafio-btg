package br.com.desafio.btgpactual.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    @Test
    void testCalculateTotalValue() {
        Item item1 = new Item();
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("10.0"));

        Item item2 = new Item();
        item2.setQuantity(1);
        item2.setPrice(new BigDecimal("20.0"));

        Order order = new Order();
        order.addItem(item1);
        order.addItem(item2);

        BigDecimal expectedValue = new BigDecimal("40.0");

        assertEquals(expectedValue, order.calculateTotalValue());
    }
}
