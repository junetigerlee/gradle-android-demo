package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Agent {
	public static void addEntity(Schema schema) {
		Entity entity = schema.addEntity(Agent.class.getSimpleName());
		entity.addIdProperty().autoincrement();
		entity.addStringProperty("mId").columnName("mId");// 货代id
		entity.addStringProperty("agentno").columnName("agentno");
		entity.addStringProperty("tel").columnName("tel");
		entity.addStringProperty("company").columnName("company");
		entity.addStringProperty("address").columnName("address");
		entity.addStringProperty("name").columnName("name");
		entity.addStringProperty("location").columnName("location");
		entity.addStringProperty("photo").columnName("photo");
		entity.addStringProperty("status").columnName("status");
        entity.addStringProperty("agentLevel").columnName("agentLevel");
		entity.addLongProperty("createtime").columnName("createtime");
	}
}
