package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class OrdersAll {
    public static void addEntity(Schema schema) {
        Entity entity = schema.addEntity(OrdersAll.class.getSimpleName());
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("myorder").columnName("myorder");// 订单
        entity.addStringProperty("goods").columnName("goods");// 货源
        entity.addStringProperty("consigne").columnName("consigne");// 收货人
        entity.addStringProperty("shipper").columnName("shipper");// 发货人
        entity.addStringProperty("agent").columnName("agent");// 货代

        // entity.addStringProperty("mId").columnName("mId");
        // entity.addStringProperty("originalprovince").columnName(
        // "originalprovince");// 订单发货地省
        // entity.addStringProperty("originalcity").columnName("originalcity");//
        // 订单发货地城市
        // entity.addStringProperty("originalregion").columnName("originalregion");//
        // 订单发货地区
        // entity.addStringProperty("receiptprovince").columnName(
        // "receiptprovince");// 订单收货地省
        // entity.addStringProperty("receiptcity").columnName("receiptcity");//
        // 订单收货地城市
        // entity.addStringProperty("receiptregion").columnName("receiptregion");//
        // 订单收货地区
        //
        // entity.addStringProperty("orderno").columnName("orderno");// 订单编号
        // entity.addStringProperty("generatetime").columnName("generatetime");//
        // 订单时间
        // entity.addStringProperty("status").columnName("status");// 订单状态
        //
        // entity.addStringProperty("infofare").columnName("infofare");// 订单信息费
        // entity.addStringProperty("declaredvalue").columnName("declaredvalue");//
        // 声明价值
        // entity.addStringProperty("fare").columnName("fare");// 运价
        // entity.addStringProperty("weight").columnName("weight");// 吨位
        // entity.addStringProperty("goodstype").columnName("goodstype");// 货物类型
        // entity.addStringProperty("volume").columnName("volume");// 货物体积
        // entity.addStringProperty("goodsno").columnName("goodsno");// 货源编号
        // entity.addStringProperty("goodsname").columnName("goodsname");// 货物名称
        // entity.addStringProperty("count").columnName("count");// 货物件数
        // entity.addStringProperty("day").columnName("day");// 有效期天数
        // entity.addStringProperty("memo").columnName("memo");// 描述
        // entity.addStringProperty("collectgs").columnName("collectgs");//
        // 0：收藏，1：未收藏
        // entity.addStringProperty("agentno").columnName("agentno");// 货代编号
        // entity.addStringProperty("company").columnName("company");// 货代公司
        // entity.addStringProperty("address").columnName("address");// 货代地址
        // entity.addStringProperty("name").columnName("name");// 货代姓名
        // entity.addStringProperty("tel").columnName("tel");// 货代联系方式
        // entity.addStringProperty("photo").columnName("photo");// 活体图片
        // entity.addStringProperty("starImg").columnName("starImg");// 货代星级
        //
        // entity.addStringProperty("sid").columnName("sid");// 发货人id
        // entity.addIntProperty("sname").columnName("sname");// 发货人姓名
        // entity.addStringProperty("stel").columnName("stel");// 发货人电话
        // entity.addStringProperty("scompany").columnName("scompany");// 发货人公司
        // entity.addStringProperty("saddress").columnName("saddress");// 发货人地址
        // entity.addStringProperty("rid").columnName("rid");// 收货人地址
        // entity.addStringProperty("rname").columnName("rname");// 收货人姓名
        // entity.addStringProperty("rtel").columnName("rtel");// 收货人电话
        // entity.addStringProperty("rcompany").columnName("rcompany");// 收货人公司
        // entity.addStringProperty("raddress").columnName("raddress");// 收货人地址
        entity.addLongProperty("createtime").columnName("createtime");
    }
}
