package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 退款的订单模型
 * 
 * @author qiujiaheng
 * 
 *         2014年3月4日
 */
public class OrdersReimburse {
    public static void addEntity(Schema schema) {
        Entity entity = schema.addEntity(OrdersReimburse.class.getSimpleName());
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("myorder").columnName("myorder");// 订单
        entity.addStringProperty("goods").columnName("goods");// 货源
        entity.addStringProperty("consigne").columnName("consigne");// 收货人
        entity.addStringProperty("shipper").columnName("shipper");// 发货人
        entity.addStringProperty("agent").columnName("agent");// 货代
        entity.addLongProperty("createtime").columnName("createtime");
    }
}
