package com.cyanbirds.lljy.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.cyanbirds.lljy.entity.NameList;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NAME_LIST".
*/
public class NameListDao extends AbstractDao<NameList, Long> {

    public static final String TABLENAME = "NAME_LIST";

    /**
     * Properties of entity NameList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SeeTime = new Property(1, long.class, "seeTime", false, "SEE_TIME");
        public final static Property Namelist = new Property(2, String.class, "namelist", false, "NAMELIST");
    }


    public NameListDao(DaoConfig config) {
        super(config);
    }
    
    public NameListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NAME_LIST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SEE_TIME\" INTEGER NOT NULL ," + // 1: seeTime
                "\"NAMELIST\" TEXT NOT NULL );"); // 2: namelist
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NAME_LIST\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NameList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSeeTime());
        stmt.bindString(3, entity.getNamelist());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NameList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSeeTime());
        stmt.bindString(3, entity.getNamelist());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NameList readEntity(Cursor cursor, int offset) {
        NameList entity = new NameList( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // seeTime
            cursor.getString(offset + 2) // namelist
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NameList entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSeeTime(cursor.getLong(offset + 1));
        entity.setNamelist(cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NameList entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NameList entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NameList entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
