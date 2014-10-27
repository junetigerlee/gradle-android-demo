package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Comment {
    public static void addEntity(Schema schema) {
        Entity entity = schema.addEntity(Comment.class.getSimpleName());
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("status").columnName("status");
        entity.addStringProperty("level").columnName("level");
        entity.addStringProperty("content").columnName("content");
        entity.addLongProperty("createtime").columnName("createtime");
    }
}
