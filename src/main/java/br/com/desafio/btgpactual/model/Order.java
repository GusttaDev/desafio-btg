package br.com.desafio.btgpactual.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderCode;
    private Long customerCode;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order() {
    }

    public Order(Long orderCode, Long customerCode) {
        this.orderCode = orderCode;
        this.customerCode = customerCode;
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public BigDecimal calculateTotalValue() {

        BigDecimal totalValue = BigDecimal.ZERO;

        for (Item item : items) {
            BigDecimal valor = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalValue = totalValue.add(valor);
        }
        return totalValue;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public Long getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Long customerCode) {
        this.customerCode = customerCode;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
