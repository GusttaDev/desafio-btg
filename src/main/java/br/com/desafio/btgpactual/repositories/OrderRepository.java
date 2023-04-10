package br.com.desafio.btgpactual.repositories;

import br.com.desafio.btgpactual.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findByCustomerCode(Long customerCode);
    Optional<Long> countByCustomerCode(Long customerCode);

    Optional<Order> findByOrderCode(Long orderCode);
}
