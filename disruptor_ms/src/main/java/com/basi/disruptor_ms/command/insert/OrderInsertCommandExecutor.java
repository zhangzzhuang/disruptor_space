package com.basi.disruptor_ms.command.insert;

import com.basi.disruptor_ms.command.CommandExecutor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderInsertCommandExecutor implements CommandExecutor<OrderInsertCommandBuffer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInsertCommandExecutor.class);

    private static final String SQL = "INSERT INTO ITEM_ORDER(ITEM_ID, USER_ID) VALUES (?, ?)";

    private JdbcTemplate jdbcTemplate;

    public OrderInsertCommandExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void execute(OrderInsertCommandBuffer commandBuffer) {

        List<OrderInsertCommand> commands = commandBuffer.get();
        if (CollectionUtils.isEmpty(commands)) {
            return;
        }

        List<Object[]> args = commands.stream().map(cmd -> new Object[]{cmd.getItemId(), cmd.getUserId()}).collect(toList());

        try {
            jdbcTemplate.batchUpdate(SQL, args);
            commands.forEach(cmd -> LOGGER.info("Executed Insert: {}", cmd.toString()));
        } catch (Exception e) {
            commands.forEach(cmd -> LOGGER.error("Executed Failed : {}", cmd));
            LOGGER.error("Executed Order Insert Failed : {}", ExceptionUtils.getStackTrace(e));
        }

    }
}
