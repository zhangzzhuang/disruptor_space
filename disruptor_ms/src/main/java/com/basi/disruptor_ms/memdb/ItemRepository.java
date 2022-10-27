package com.basi.disruptor_ms.memdb;

/**
 * 商品内存数据库
 */
public interface ItemRepository {

  void put(Item item);

  Item get(Long id);

}
