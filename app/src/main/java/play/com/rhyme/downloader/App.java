package play.com.rhyme.downloader;

import android.app.Application;

import play.com.rhyme.downloader.data.DataDao;

/**
 * 作 者 : rhyme
 * 邮 箱 : rhymelph@qq.com
 * Q  Q : 708959817
 * 时 间 : 2018/5/13
 * 介 绍 :
 **/
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataDao.getInstance(this);
    }
}
