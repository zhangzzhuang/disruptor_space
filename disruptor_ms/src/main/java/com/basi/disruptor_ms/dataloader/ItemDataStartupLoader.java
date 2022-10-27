package com.basi.disruptor_ms.dataloader;

import com.basi.disruptor_ms.memdb.Item;
import com.basi.disruptor_ms.memdb.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemDataStartupLoader extends DataStartupLoader {

    private static final Logger logger = LoggerFactory.getLogger(ItemDataStartupLoader.class);

    @Autowired
    private ItemRepository itemRepository;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void doLoad() {

        logger.info("doLoad---------");
        List<Item> items = jdbcTemplate.query("select id, amount from item", (rs, rowNum) -> new Item(rs.getLong(1), rs.getInt(2)));
        items.stream().forEach(item -> {
            itemRepository.put(item);
            logger.info("Load Item from database: {}", item.toString());
        });
    }

    /**
     * 如果一个对象实现了SmartLifecycle然后令其getPhase()方法返回了Integer.MIN_VALUE的话，就会让该对象最早启动，而最晚销毁。
     * 显然，如果getPhase()方法返回了Integer.MAX_VALUE就说明了该对象会最晚启动，而最早销毁。
     * 默认值为 Ordered.LOWEST_PRECEDENCE(Integer.MAX_VALUE)
     *
     * @return
     */
    @Override
    public int getPhase() {

        logger.info("ItemDataStartupLoader ---------");
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
