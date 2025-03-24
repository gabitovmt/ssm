package org.example.ssm55;

import org.example.ssm55.domain.Event;
import org.example.ssm55.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Persist {
    private final PersistStateMachineHandler handler;
    private final JdbcTemplate jdbcTemplate;

    public Persist(PersistStateMachineHandler handler, JdbcTemplate jdbcTemplate) {
        this.handler = handler;
        this.jdbcTemplate = jdbcTemplate;
        handler.addPersistStateChangeListener(new LocalPersistStateChangeListener(jdbcTemplate));
    }

    public String listDbEntries() {
        List<Order> orders = jdbcTemplate.query(
                "select id, state from orders",
                (rs, rowNum) -> new Order(rs.getInt("id"), rs.getString("state"))
        );

        return orders.stream()
                .map(Objects::toString)
                .collect(Collectors.joining("\n"));
    }

    public void change(int orderId, Event event) {
        Order o = jdbcTemplate.queryForObject(
                "select id, state from orders where id = ?",
                (rs, rowNum) -> new Order(rs.getInt("id"), rs.getString("state")),
                orderId
        );
        Objects.requireNonNull(o, "Order is null");

        handler.handleEventWithStateReactively(
                MessageBuilder
                        .withPayload(event.toString())
                        .setHeader("order", orderId)
                        .build(),
                o.getState()
        ).subscribe();
    }
}
