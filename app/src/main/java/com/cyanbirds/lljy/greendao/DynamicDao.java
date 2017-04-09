package com.cyanbirds.lljy.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.cyanbirds.lljy.entity.Dynamic;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DYNAMIC".
*/
public class DynamicDao extends AbstractDao<Dynamic, Long> {

    public static final String TABLENAME = "DYNAMIC";

    /**
     * Properties of entity Dynamic.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NickName = new Property(1, String.class, "nickName", false, "NICK_NAME");
        public final static Property FaceUrl = new Property(2, String.class, "faceUrl", false, "FACE_URL");
        public final static Property CreateTime = new Property(3, String.class, "createTime", false, "CREATE_TIME");
        public final static Property Content = new Property(4, String.class, "content", false, "CONTENT");
        public final static Property IsDelete = new Property(5, boolean.class, "isDelete", false, "IS_DELETE");
        public final static Property Videos = new Property(6, String.class, "videos", false, "VIDEOS");
        public final static Property Pictures = new Property(7, String.class, "pictures", false, "PICTURES");
    }


    public DynamicDao(DaoConfig config) {
        super(config);
    }
    
    public DynamicDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DYNAMIC\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NICK_NAME\" TEXT," + // 1: nickName
                "\"FACE_URL\" TEXT," + // 2: faceUrl
                "\"CREATE_TIME\" TEXT," + // 3: createTime
                "\"CONTENT\" TEXT," + // 4: content
                "\"IS_DELETE\" INTEGER NOT NULL ," + // 5: isDelete
                "\"VIDEOS\" TEXT," + // 6: videos
                "\"PICTURES\" TEXT);"); // 7: pictures
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DYNAMIC\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Dynamic entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(2, nickName);
        }
 
        String faceUrl = entity.getFaceUrl();
        if (faceUrl != null) {
            stmt.bindString(3, faceUrl);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(4, createTime);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(5, content);
        }
        stmt.bindLong(6, entity.getIsDelete() ? 1L: 0L);
 
        String videos = entity.getVideos();
        if (videos != null) {
            stmt.bindString(7, videos);
        }
 
        String pictures = entity.getPictures();
        if (pictures != null) {
            stmt.bindString(8, pictures);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Dynamic entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(2, nickName);
        }
 
        String faceUrl = entity.getFaceUrl();
        if (faceUrl != null) {
            stmt.bindString(3, faceUrl);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(4, createTime);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(5, content);
        }
        stmt.bindLong(6, entity.getIsDelete() ? 1L: 0L);
 
        String videos = entity.getVideos();
        if (videos != null) {
            stmt.bindString(7, videos);
        }
 
        String pictures = entity.getPictures();
        if (pictures != null) {
            stmt.bindString(8, pictures);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Dynamic readEntity(Cursor cursor, int offset) {
        Dynamic entity = new Dynamic( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nickName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // faceUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // createTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // content
            cursor.getShort(offset + 5) != 0, // isDelete
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // videos
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // pictures
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Dynamic entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNickName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFaceUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCreateTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContent(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsDelete(cursor.getShort(offset + 5) != 0);
        entity.setVideos(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPictures(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Dynamic entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Dynamic entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Dynamic entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
