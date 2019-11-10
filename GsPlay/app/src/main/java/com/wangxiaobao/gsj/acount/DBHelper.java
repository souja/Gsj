package com.wangxiaobao.gsj.acount;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.user.VerifyCode;

public class DBHelper extends OrmLiteSqliteOpenHelper {
    public static final String TABLE_NAME = "waiter.db";
    private static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper instance;

    /**
     * 用来存放Dao的地图
     */
    private Map<String, Dao> daoList = new HashMap<>();

    private DBHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {

        LogTool.i(TAG, "onCreate");
        try {
            TableUtils.createTable(connectionSource, VerifyCode.class);
        } catch (SQLException e) {
            e.printStackTrace();
            LogTool.i(TAG, "database onCreate error", e);
        }

    }


    public synchronized Dao getDao(Class clazz) {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (!daoList.containsKey(className)) {
            try {
                dao = super.getDao(clazz);
                daoList.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
                LogTool.E(TAG, "getDao error:", e, false);
            }
        } else {
            dao = daoList.get(className);
        }
        return dao;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        LogTool.i(TAG, "onUpgrade oldVersion："+oldVersion+"  newVersion:"+newVersion);
        if (newVersion==2){
            try {
                TableUtils.createTable(connectionSource, VerifyCode.class);
            } catch (Exception e) {
                e.printStackTrace();
                LogTool.i(TAG, "database onUpgrade error", e);
            }
        }

    }

//    public void deleteAll(){
//        try {
//            getTableDao().queryRaw("delete from tb_table_ifo");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null)
                    instance = new DBHelper(context);
            }
        }
        return instance;
    }



    public Dao<VerifyCode, String> getVerifyCode() {
        return getDao(VerifyCode.class);
    }

    @Override
    public void close() {
        super.close();
        for (String key : daoList.keySet()) {
            Dao dao = daoList.get(key);
            daoList.remove(dao);
        }
        instance = null;
    }
}
