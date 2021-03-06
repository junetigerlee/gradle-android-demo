package cn.com.incito.driver.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import cn.com.incito.driver.dao.GoodsAvailable;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GOODS_AVAILABLE.
*/
public class GoodsAvailableDao extends AbstractDao<GoodsAvailable, Long> {

    public static final String TABLENAME = "GOODS_AVAILABLE";

    /**
     * Properties of entity GoodsAvailable.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MId = new Property(1, String.class, "mId", false, "mId");
        public final static Property Originalprovince = new Property(2, String.class, "originalprovince", false, "originalprovince");
        public final static Property Originalcity = new Property(3, String.class, "originalcity", false, "originalcity");
        public final static Property Originalregion = new Property(4, String.class, "originalregion", false, "originalregion");
        public final static Property Receiptprovince = new Property(5, String.class, "receiptprovince", false, "receiptprovince");
        public final static Property Receiptcity = new Property(6, String.class, "receiptcity", false, "receiptcity");
        public final static Property Receiptregion = new Property(7, String.class, "receiptregion", false, "receiptregion");
        public final static Property Goodsname = new Property(8, String.class, "goodsname", false, "goodsname");
        public final static Property Goodstype = new Property(9, String.class, "goodstype", false, "goodstype");
        public final static Property Infofare = new Property(10, String.class, "infofare", false, "infofare");
        public final static Property Fare = new Property(11, String.class, "fare", false, "fare");
        public final static Property Weight = new Property(12, String.class, "weight", false, "weight");
        public final static Property Volume = new Property(13, String.class, "volume", false, "volume");
        public final static Property Count = new Property(14, String.class, "count", false, "count");
        public final static Property Carlength = new Property(15, String.class, "carlength", false, "carlength");
        public final static Property Generatetime = new Property(16, String.class, "generatetime", false, "generatetime");
        public final static Property Goodstime = new Property(17, String.class, "goodstime", false, "goodstime");
        public final static Property Completetime = new Property(18, String.class, "completetime", false, "completetime");
        public final static Property Goodsid = new Property(19, String.class, "goodsid", false, "goodsid");
        public final static Property Carid = new Property(20, String.class, "carid", false, "carid");
        public final static Property Grabflag = new Property(21, String.class, "grabflag", false, "grabflag");
        public final static Property Grabresult = new Property(22, String.class, "grabresult", false, "grabresult");
        public final static Property Orderid = new Property(23, String.class, "orderid", false, "orderid");
        public final static Property Failurereason = new Property(24, String.class, "failurereason", false, "failurereason");
        public final static Property Createtime = new Property(25, Long.class, "createtime", false, "createtime");
    };


    public GoodsAvailableDao(DaoConfig config) {
        super(config);
    }
    
    public GoodsAvailableDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GOODS_AVAILABLE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'mId' TEXT," + // 1: mId
                "'originalprovince' TEXT," + // 2: originalprovince
                "'originalcity' TEXT," + // 3: originalcity
                "'originalregion' TEXT," + // 4: originalregion
                "'receiptprovince' TEXT," + // 5: receiptprovince
                "'receiptcity' TEXT," + // 6: receiptcity
                "'receiptregion' TEXT," + // 7: receiptregion
                "'goodsname' TEXT," + // 8: goodsname
                "'goodstype' TEXT," + // 9: goodstype
                "'infofare' TEXT," + // 10: infofare
                "'fare' TEXT," + // 11: fare
                "'weight' TEXT," + // 12: weight
                "'volume' TEXT," + // 13: volume
                "'count' TEXT," + // 14: count
                "'carlength' TEXT," + // 15: carlength
                "'generatetime' TEXT," + // 16: generatetime
                "'goodstime' TEXT," + // 17: goodstime
                "'completetime' TEXT," + // 18: completetime
                "'goodsid' TEXT," + // 19: goodsid
                "'carid' TEXT," + // 20: carid
                "'grabflag' TEXT," + // 21: grabflag
                "'grabresult' TEXT," + // 22: grabresult
                "'orderid' TEXT," + // 23: orderid
                "'failurereason' TEXT," + // 24: failurereason
                "'createtime' INTEGER);"); // 25: createtime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GOODS_AVAILABLE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GoodsAvailable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mId = entity.getMId();
        if (mId != null) {
            stmt.bindString(2, mId);
        }
 
        String originalprovince = entity.getOriginalprovince();
        if (originalprovince != null) {
            stmt.bindString(3, originalprovince);
        }
 
        String originalcity = entity.getOriginalcity();
        if (originalcity != null) {
            stmt.bindString(4, originalcity);
        }
 
        String originalregion = entity.getOriginalregion();
        if (originalregion != null) {
            stmt.bindString(5, originalregion);
        }
 
        String receiptprovince = entity.getReceiptprovince();
        if (receiptprovince != null) {
            stmt.bindString(6, receiptprovince);
        }
 
        String receiptcity = entity.getReceiptcity();
        if (receiptcity != null) {
            stmt.bindString(7, receiptcity);
        }
 
        String receiptregion = entity.getReceiptregion();
        if (receiptregion != null) {
            stmt.bindString(8, receiptregion);
        }
 
        String goodsname = entity.getGoodsname();
        if (goodsname != null) {
            stmt.bindString(9, goodsname);
        }
 
        String goodstype = entity.getGoodstype();
        if (goodstype != null) {
            stmt.bindString(10, goodstype);
        }
 
        String infofare = entity.getInfofare();
        if (infofare != null) {
            stmt.bindString(11, infofare);
        }
 
        String fare = entity.getFare();
        if (fare != null) {
            stmt.bindString(12, fare);
        }
 
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(13, weight);
        }
 
        String volume = entity.getVolume();
        if (volume != null) {
            stmt.bindString(14, volume);
        }
 
        String count = entity.getCount();
        if (count != null) {
            stmt.bindString(15, count);
        }
 
        String carlength = entity.getCarlength();
        if (carlength != null) {
            stmt.bindString(16, carlength);
        }
 
        String generatetime = entity.getGeneratetime();
        if (generatetime != null) {
            stmt.bindString(17, generatetime);
        }
 
        String goodstime = entity.getGoodstime();
        if (goodstime != null) {
            stmt.bindString(18, goodstime);
        }
 
        String completetime = entity.getCompletetime();
        if (completetime != null) {
            stmt.bindString(19, completetime);
        }
 
        String goodsid = entity.getGoodsid();
        if (goodsid != null) {
            stmt.bindString(20, goodsid);
        }
 
        String carid = entity.getCarid();
        if (carid != null) {
            stmt.bindString(21, carid);
        }
 
        String grabflag = entity.getGrabflag();
        if (grabflag != null) {
            stmt.bindString(22, grabflag);
        }
 
        String grabresult = entity.getGrabresult();
        if (grabresult != null) {
            stmt.bindString(23, grabresult);
        }
 
        String orderid = entity.getOrderid();
        if (orderid != null) {
            stmt.bindString(24, orderid);
        }
 
        String failurereason = entity.getFailurereason();
        if (failurereason != null) {
            stmt.bindString(25, failurereason);
        }
 
        Long createtime = entity.getCreatetime();
        if (createtime != null) {
            stmt.bindLong(26, createtime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GoodsAvailable readEntity(Cursor cursor, int offset) {
        GoodsAvailable entity = new GoodsAvailable( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // mId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // originalprovince
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // originalcity
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // originalregion
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // receiptprovince
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // receiptcity
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // receiptregion
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // goodsname
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // goodstype
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // infofare
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // fare
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // weight
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // volume
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // count
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // carlength
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // generatetime
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // goodstime
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // completetime
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // goodsid
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // carid
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // grabflag
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // grabresult
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // orderid
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // failurereason
            cursor.isNull(offset + 25) ? null : cursor.getLong(offset + 25) // createtime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GoodsAvailable entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOriginalprovince(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOriginalcity(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setOriginalregion(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setReceiptprovince(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setReceiptcity(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setReceiptregion(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setGoodsname(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGoodstype(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setInfofare(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFare(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setWeight(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setVolume(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setCount(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCarlength(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setGeneratetime(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setGoodstime(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setCompletetime(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setGoodsid(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setCarid(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setGrabflag(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setGrabresult(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setOrderid(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setFailurereason(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setCreatetime(cursor.isNull(offset + 25) ? null : cursor.getLong(offset + 25));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GoodsAvailable entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GoodsAvailable entity) {
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
