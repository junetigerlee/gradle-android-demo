package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 已抢货源
 * 
 * @author lizhan
 * 
 */
public class GoodsHasAvailable {
	public static void addEntity(Schema schema) {
		Entity entity = schema.addEntity(GoodsHasAvailable.class
				.getSimpleName());
		entity.addIdProperty().autoincrement();
		entity.addStringProperty("mId").columnName("mId");// 推送ID
		entity.addStringProperty("originalprovince").columnName(
				"originalprovince");// 发货地省
		entity.addStringProperty("originalcity").columnName("originalcity");// 发货地城市
		entity.addStringProperty("originalregion").columnName("originalregion");// 订单发货地区
		entity.addStringProperty("receiptprovince").columnName(
				"receiptprovince");// 收货地省
		entity.addStringProperty("receiptcity").columnName("receiptcity");// 收货地城市
		entity.addStringProperty("receiptregion").columnName("receiptregion");// 收货地区
		entity.addStringProperty("goodsname").columnName("goodsname");// 货物名称
		entity.addStringProperty("goodstype").columnName("goodstype");// 货物类型
		entity.addStringProperty("infofare").columnName("infofare");// 订单信息费
		entity.addStringProperty("fare").columnName("fare");// 运价
		entity.addStringProperty("weight").columnName("weight");// 吨位
		entity.addStringProperty("volume").columnName("volume");// 货物体积
		entity.addStringProperty("count").columnName("count");// 货物件数
		entity.addStringProperty("carlength").columnName("carlength");// 车长
		entity.addStringProperty("generatetime").columnName("generatetime");// 发布时间
		entity.addStringProperty("goodstime").columnName("goodstime");//
		entity.addStringProperty("completetime").columnName("completetime");// 抢单时间
		entity.addStringProperty("goodsid").columnName("goodsid");// 货源ID
		entity.addStringProperty("carid").columnName("carid");// 车辆ID
		entity.addStringProperty("grabflag").columnName("grabflag");// 司机是否已抢单，默认0是未抢单，1是已抢单
		entity.addStringProperty("grabresult").columnName("grabresult");// 1审批中
																		// 2抢单成功
																		// 3抢单失败
		entity.addStringProperty("orderid").columnName("orderid");// 抢单成功后对应的订单ID
		entity.addStringProperty("failurereason").columnName("failurereason");// 失败原因

		entity.addLongProperty("createtime").columnName("createtime");
	}
}
