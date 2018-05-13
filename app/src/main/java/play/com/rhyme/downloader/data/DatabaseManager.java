package play.com.rhyme.downloader.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者: rhyme
 * 邮箱: rhymelph@qq.com
 * Q Q: 708959817
 * 时间: 2018/5/13
 * 介绍:
 **/
public class DatabaseManager {
    public static final String TAG="DataBaseManager";

    private AtomicInteger nOpenCounter=new AtomicInteger();

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mySQLDBHelper;
    private SQLiteDatabase mDatabase;

    /**
     * 初始化
     */
    public static synchronized void initialize(SQLiteOpenHelper mySQLDBHelper){
        if (instance==null){
            synchronized (DatabaseManager.class){
                if (instance==null){
                    instance=new DatabaseManager();
                    DatabaseManager.mySQLDBHelper =mySQLDBHelper;
                }
            }
        }
    }

    /**
     * 获取到管理
     */
    public static synchronized DatabaseManager getInstance(){
        if (instance==null){
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initialize(..) method first.");
        }
        return instance;
    }

    /**
     * 获取数据库的引用
     */
    public synchronized SQLiteDatabase openDataBase(){
        if (nOpenCounter.incrementAndGet() ==1){
//            LogUtil.ShowLog(TAG,"打开数据库");
            mDatabase=mySQLDBHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDataBase(){
        if (nOpenCounter.decrementAndGet()==0){
//            LogUtil.ShowLog(TAG,"关闭数据库");
            mDatabase.close();
        }
    }

    public  void startTrancation(Trancation trancation){
        SQLiteDatabase db=openDataBase();
        db.beginTransaction();
        trancation.start(db);
        db.setTransactionSuccessful();
        db.endTransaction();
        closeDataBase();
    }
    public interface Trancation{
        void start(SQLiteDatabase db);
    }
}
