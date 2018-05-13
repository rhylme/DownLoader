package play.com.rhyme.downloader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import play.com.rhyme.downloader.entity.M3U8;

/**
 * 作 者 : rhyme
 * 邮 箱 : rhymelph@qq.com
 * Q  Q : 708959817
 * 时 间 : 2018/5/13
 * 介 绍 :
 **/
public class DataDao {

    private static DataDao dataDao;

    private static final String ID="id";
    private static final String ADDRESS="address";
    private static final String PROGRESS="progress";
    private static final String MAX_PROGRESS="max_progress";
    private static final String COMPLETE="complete";
    private static final String PATH="path";
    private static final String M3U8="m3u8";

    public DataDao(Context context) {
        MyOpenHelper openHelper = new MyOpenHelper(context);
        DatabaseManager.initialize(openHelper);
    }

    public static void getInstance(Context context) {
        if (dataDao == null) {
            synchronized (DataDao.class) {
                if (dataDao == null) {
                    dataDao = new DataDao(context);
                }
            }
        }
    }
    public static DataDao getInstance(){
        if (dataDao==null){
            throw new IllegalArgumentException("please getInstance the dataDao");
        }
        return dataDao;
    }

    public class MyOpenHelper extends SQLiteOpenHelper {
        private static final int DATA_VERSION = 1;
        private static final String DATA_NAME = "MyData.db";



        public static final String CREATE_TABLE="create table if not exists "+M3U8+"(" +
                ID+" chat(32) primary key," +
                ADDRESS+" text," +
                PROGRESS+" integer," +
                MAX_PROGRESS+" integer," +
                COMPLETE+" integer," +
                PATH+" text)";
        public MyOpenHelper(Context context) {
            super(context, DATA_NAME, null, DATA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO: 2018/5/13 upgrade
        }
    }

    /**
     * 添加一条记录
     */
    public void insertOne(M3U8 m3U8){
        DatabaseManager.getInstance().startTrancation(db ->{
            ContentValues cv=new ContentValues();
            cv.put(ID,m3U8.getId());
            cv.put(ADDRESS,m3U8.getAddress());
            cv.put(MAX_PROGRESS,m3U8.getMax_progress());
            cv.put(PROGRESS,m3U8.getProgress());
            cv.put(COMPLETE,m3U8.getComlete());
            cv.put(PATH,m3U8.getPath());
            db.insert(M3U8,null,cv);
        });
    }

    /**
     * 更新
     */
    public void updateOne(M3U8 m3U8){
        DatabaseManager.getInstance().startTrancation(db -> {
            ContentValues cv=new ContentValues();
            cv.put(ID,m3U8.getId());
            cv.put(ADDRESS,m3U8.getAddress());
            cv.put(MAX_PROGRESS,m3U8.getMax_progress());
            cv.put(PROGRESS,m3U8.getProgress());
            cv.put(COMPLETE,m3U8.getComlete());
            cv.put(PATH,m3U8.getPath());
            db.update(M3U8,cv,"id=?",new String[]{m3U8.getId()});

        });
    }

    /**
     * 通过地址查询
     */
    public M3U8 selectByName(String address){
        M3U8 m3U8=new M3U8();
        DatabaseManager.getInstance().startTrancation(db -> {
            Cursor cursor=db.rawQuery("select * from "+M3U8+" where "+ADDRESS+"='"+address+"'",null);
            if (cursor.moveToNext()){
                m3U8.setId(cursor.getString(cursor.getColumnIndex(ID)));
                m3U8.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                m3U8.setComlete(cursor.getInt(cursor.getColumnIndex(COMPLETE)));
                m3U8.setMax_progress(cursor.getInt(cursor.getColumnIndex(MAX_PROGRESS)));
                m3U8.setProgress(cursor.getInt(cursor.getColumnIndex(PROGRESS)));
                m3U8.setPath(cursor.getString(cursor.getColumnIndex(PATH)));
            }else {
                m3U8.setProgress(-1);
            }
            cursor.close();
        });
        return m3U8;
    }

    /**
     * 删除一个
     */
    public void delete(String id){
        DatabaseManager.getInstance().startTrancation(db -> {
            db.delete(M3U8,"id=?",new String[]{id});
        });
    }

}
