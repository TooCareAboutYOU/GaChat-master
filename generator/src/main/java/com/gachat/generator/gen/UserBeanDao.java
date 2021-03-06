package com.gachat.generator.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gachat.generator.model.UserBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_BEAN".
*/
public class UserBeanDao extends AbstractDao<UserBean, Long> {

    public static final String TABLENAME = "USER_BEAN";

    /**
     * Properties of entity UserBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IsLogin = new Property(1, boolean.class, "isLogin", false, "IS_LOGIN");
        public final static Property Token = new Property(2, String.class, "token", false, "TOKEN");
        public final static Property Username = new Property(3, String.class, "username", false, "USERNAME");
        public final static Property Gender = new Property(4, String.class, "gender", false, "GENDER");
        public final static Property Diamond = new Property(5, int.class, "diamond", false, "DIAMOND");
        public final static Property Uid = new Property(6, int.class, "uid", false, "UID");
        public final static Property Age = new Property(7, int.class, "age", false, "AGE");
        public final static Property Character = new Property(8, int.class, "character", false, "CHARACTER");
        public final static Property Rank = new Property(9, String.class, "rank", false, "RANK");
        public final static Property Claw_doll_time = new Property(10, int.class, "claw_doll_time", false, "CLAW_DOLL_TIME");
        public final static Property Gift = new Property(11, int.class, "gift", false, "GIFT");
    }


    public UserBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"IS_LOGIN\" INTEGER NOT NULL ," + // 1: isLogin
                "\"TOKEN\" TEXT," + // 2: token
                "\"USERNAME\" TEXT," + // 3: username
                "\"GENDER\" TEXT," + // 4: gender
                "\"DIAMOND\" INTEGER NOT NULL ," + // 5: diamond
                "\"UID\" INTEGER NOT NULL ," + // 6: uid
                "\"AGE\" INTEGER NOT NULL ," + // 7: age
                "\"CHARACTER\" INTEGER NOT NULL ," + // 8: character
                "\"RANK\" TEXT," + // 9: rank
                "\"CLAW_DOLL_TIME\" INTEGER NOT NULL ," + // 10: claw_doll_time
                "\"GIFT\" INTEGER NOT NULL );"); // 11: gift
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsLogin() ? 1L: 0L);
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(3, token);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(4, username);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(5, gender);
        }
        stmt.bindLong(6, entity.getDiamond());
        stmt.bindLong(7, entity.getUid());
        stmt.bindLong(8, entity.getAge());
        stmt.bindLong(9, entity.getCharacter());
 
        String rank = entity.getRank();
        if (rank != null) {
            stmt.bindString(10, rank);
        }
        stmt.bindLong(11, entity.getClaw_doll_time());
        stmt.bindLong(12, entity.getGift());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsLogin() ? 1L: 0L);
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(3, token);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(4, username);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(5, gender);
        }
        stmt.bindLong(6, entity.getDiamond());
        stmt.bindLong(7, entity.getUid());
        stmt.bindLong(8, entity.getAge());
        stmt.bindLong(9, entity.getCharacter());
 
        String rank = entity.getRank();
        if (rank != null) {
            stmt.bindString(10, rank);
        }
        stmt.bindLong(11, entity.getClaw_doll_time());
        stmt.bindLong(12, entity.getGift());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserBean readEntity(Cursor cursor, int offset) {
        UserBean entity = new UserBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1) != 0, // isLogin
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // token
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // username
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // gender
            cursor.getInt(offset + 5), // diamond
            cursor.getInt(offset + 6), // uid
            cursor.getInt(offset + 7), // age
            cursor.getInt(offset + 8), // character
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // rank
            cursor.getInt(offset + 10), // claw_doll_time
            cursor.getInt(offset + 11) // gift
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIsLogin(cursor.getShort(offset + 1) != 0);
        entity.setToken(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUsername(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGender(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDiamond(cursor.getInt(offset + 5));
        entity.setUid(cursor.getInt(offset + 6));
        entity.setAge(cursor.getInt(offset + 7));
        entity.setCharacter(cursor.getInt(offset + 8));
        entity.setRank(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setClaw_doll_time(cursor.getInt(offset + 10));
        entity.setGift(cursor.getInt(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
