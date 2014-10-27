package cn.com.incito.driver.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import cn.com.incito.driver.dao.OrdersSign;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ORDERS_SIGN.
*/
public class OrdersSignDao extends AbstractDao<OrdersSign, Long> {

    public static final String TABLENAME = "ORDERS_SIGN";

    /**
     * Properties of entity OrdersSign.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Myorder = new Property(1, String.class, "myorder", false, "myorder");
        public final static Property Goods = new Property(2, String.class, "goods", false, "goods");
        public final static Property Consigne = new Property(3, String.class, "consigne", false, "consigne");
        public final static Property Shipper = new Property(4, String.class, "shipper", false, "shipper");
        public final static Property Agent = new Property(5, String.class, "agent", false, "agent");
        public final static Property Createtime = new Property(6, Long.class, "createtime", false, "createtime");
    };


    public OrdersSignDao(DaoConfig config) {
        super(config);
    }
    
    public OrdersSignDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ORDERS_SIGN' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'myorder' TEXT," + // 1: myorder
                "'goods' TEXT," + // 2: goods
                "'consigne' TEXT," + // 3: consigne
                "'shipper' TEXT," + // 4: shipper
                "'agent' TEXT," + // 5: agent
                "'createtime' INTEGER);"); // 6: createtime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ORDERS_SIGN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrdersSign entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String myorder = entity.getMyorder();
        if (myorder != null) {
            stmt.bindString(2, myorder);
        }
 
        String goods = entity.getGoods();
        if (goods != null) {
            stmt.bindString(3, goods);
        }
 
        String consigne = entity.getConsigne();
        if (consigne != null) {
            stmt.bindString(4, consigne);
        }
 
        String shipper = entity.getShipper();
        if (shipper != null) {
            stmt.bindString(5, shipper);
        }
 
        String agent = entity.getAgent();
        if (agent != null) {
            stmt.bindString(6, agent);
        }
 
        Long createtime = entity.getCreatetime();
        if (createtime != null) {
            stmt.bindLong(7, createtime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public OrdersSign readEntity(Cursor cursor, int offset) {
        OrdersSign entity = new OrdersSign( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // myorder
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // goods
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // consigne
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shipper
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // agent
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // createtime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrdersSign entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMyorder(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setGoods(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setConsigne(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShipper(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAgent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCreatetime(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(OrdersSign entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(OrdersSign entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
