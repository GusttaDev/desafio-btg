package br.com.desafio.btgpactual.repositories;

import br.com.desafio.btgpactual.model.Item;
import br.com.desafio.btgpactual.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
