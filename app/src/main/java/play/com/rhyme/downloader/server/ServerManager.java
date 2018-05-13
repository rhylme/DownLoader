package play.com.rhyme.downloader.server;

import android.os.Environment;
import android.util.Log;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.StorageWebsite;
import com.yanzhenjie.andserver.website.WebSite;

import java.util.concurrent.TimeUnit;

/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/4/29.
 * 描述: [].
 */

public class ServerManager {
    private Server server;

    public ServerManager(){
        WebSite webSite=new StorageWebsite(Environment.getExternalStorageDirectory().getPath()+"/Downm3u8");
        server=AndServer.serverBuilder()
                .port(8080)
                .timeout(30,TimeUnit.SECONDS)
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        Log.d("tag","服务器启动:"+server.getInetAddress());
                    }

                    @Override
                    public void onStopped() {
                        Log.d("tag","服务器关闭");
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Log.d("tag","服务器出错");
                    }
                })
                .website(webSite)
                .build();
    }
    public void start(){
        server.startup();
    }

    public void stop(){
        if (server.isRunning()){
            server.shutdown();
        }
    }
}
