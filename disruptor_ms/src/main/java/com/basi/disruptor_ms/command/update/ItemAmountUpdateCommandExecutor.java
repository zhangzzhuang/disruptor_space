package com.basi.disruptor_ms.command.update;

import com.basi.disruptor_ms.command.CommandExecutor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ItemAmountUpdateCommandExecutor implements CommandExecutor<ItemAmountUpdateCommandBuffer> {

    private static final Logger logger = LoggerFactory.getLogger(ItemAmountUpdateCommandExecutor.class);

    private static final String SQL = "UPDATE ITEM SET AMOUNT = ? WHERE ID = ?";

    private JdbcTemplate jdbcTemplate;

    public ItemAmountUpdateCommandExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void execute(ItemAmountUpdateCommandBuffer commandBuffer) {

        List<ItemAmountUpdateCommand> commands = commandBuffer.get();
        if (CollectionUtils.isEmpty(commands)) {
            return;
        }

        List<Object[]> args = commands.stream().map(cmd -> new Object[]{cmd.getAmount(), cmd.getItemId()}).collect(Collectors.toList());
        try {
            jdbcTemplate.batchUpdate(SQL, args);
            commands.forEach(cmd -> logger.info("Executed Update : {}", cmd));
        } catch (Exception e) {
            commands.forEach(cmd -> logger.error("Executed Failed Update : {}", cmd));
            logger.error("Executed Item Amount Update Failed : {}", ExceptionUtils.getStackTrace(e));
        }


    }
}
