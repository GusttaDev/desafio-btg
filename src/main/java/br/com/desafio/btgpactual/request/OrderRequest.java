package br.com.desafio.btgpactual.request;

import br.com.desafio.btgpactual.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "Campo 'orderCode' deve ser informado.")
    @JsonProperty(value = "codigoPedido")
    private Long orderCode;
    @NotNull(message = "Campo 'customerCode' deve ser informado.")
    @JsonProperty(value = "codigoCliente")
    private Long customerCode;
    @NotNull(message = "Campo 'items' deve ser informado.")
    @JsonProperty(value = "itens")
    private List<Item> items = new ArrayList<>();

    public OrderRequest() {
    }

    public OrderRequest(Long orderCode, Long customerCode, List<Item> items) {
        this.orderCode = orderCode;
        this.customerCode = customerCode;
        this.items = items;
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
}