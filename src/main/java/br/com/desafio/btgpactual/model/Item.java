package br.com.desafio.btgpactual.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty(value = "produto")
    private String product;
    @JsonProperty(value = "quantidade")
    private int quantity;
    @JsonProperty(value = "preco")
    private BigDecimal price;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Item() {
    }

    public Item(Long id, String product, int quantity, BigDecimal price) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
