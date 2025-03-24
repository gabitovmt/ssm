package org.example.ssm55.command;

import lombok.RequiredArgsConstructor;
import org.example.ssm55.Persist;
import org.example.ssm55.domain.Event;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import javax.sql.DataSource;

@Command
@RequiredArgsConstructor
public class PersistCommands {
    private final DataSource dataSource;
    private final Persist persist;

    @Command(command = "db init", description = "Инициализация базы данных")
    public String initDb() {
        var populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("schema.sql"),
                new ClassPathResource("data.sql")
        );
        populator.execute(dataSource);

        return "Database initialized";
    }

    @Command(command = "persist db", description = "Список заказов")
    public String listDbEntries() {
        return persist.listDbEntries();
    }

    @Command(command = "persist process", description = "Запустить исполнение заказа")
    public void process(@Option(longNames = {"", "id"}, description = "Идентификатор заказа") int orderId) {
        persist.change(orderId, Event.PROCESS);
    }

    @Command(command = "persist send", description = "Отправить заказ")
    public void send(@Option(longNames = {"", "id"}, description = "Идентификатор заказа") int orderId) {
        persist.change(orderId, Event.SEND);
    }

    @Command(command = "persist deliver", description = "Доставить заказ")
    public void deliver(@Option(longNames = {"", "id"}, description = "Идентификатор заказа") int orderId) {
        persist.change(orderId, Event.DELIVER);
    }
}
