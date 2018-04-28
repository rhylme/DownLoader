package play.com.rhyme.downloader.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/4/28.
 * 描述: [].
 */
public class ThreadPoolUtil {
    private static ThreadPoolUtil fragment=null;
    public Executor poolexcutor;
    public Executor mainExecutor;
    public Executor singleExcutor;
    public ThreadPoolUtil(){
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        int CORE_POOL_SIZE = CPU_COUNT + 1;
        int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
        int MAX_POOL_QUEUE = 128;
        long KEEP_ALIVE = 1;
        poolexcutor= new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>(MAX_POOL_QUEUE));
        mainExecutor=new MainThreadExecutor();
        singleExcutor=new SingleThreadExecutor();
    }
    public static ThreadPoolUtil newInstance() {
        if (fragment==null){
            synchronized (ThreadPoolUtil.class){
                fragment=new ThreadPoolUtil();
            }
        }
        return fragment;
    }
    private class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler =new  Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
    private class SingleThreadExecutor implements Executor {
        private Executor singleExcutor = Executors.newSingleThreadExecutor();
        @Override
        public void execute(@NonNull Runnable command) {
            singleExcutor.execute(command);
        }
    }
}
