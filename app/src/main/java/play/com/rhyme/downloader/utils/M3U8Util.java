package play.com.rhyme.downloader.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/4/28.
 * 描述: [].
 */
public class M3U8Util {
    private String host;//IP地址，域名
    private String protocol;//请求前缀

    /**
     * 请求m3u8地址，同时需要保存这种形式的路径
     * @param url 请求地址
     * @return 返回m3u8内的信息
     */
    public String RequestM3U8(String url) {
        String info = "";
        BufferedReader reader = null;
        InputStream is = null;
        try {
            URL cturl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) cturl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(80000);
            urlConnection.setReadTimeout(80000);
            is = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String len;
            while ((len = reader.readLine()) != null) {
                builder.append(len);
            }
            info = builder.toString();
            host=cturl.getHost();
            protocol =cturl.getProtocol();
            Log.d("a", info);
            Log.d("b","主机名："+cturl.getHost());
            Log.d("c","协议名："+cturl.getProtocol());
            Log.d("c","端口号："+cturl.getPort());
            Log.d("c","路径："+cturl.getPath());
            Log.d("c","资源名："+cturl.getFile());
            File file =new FileUtil().createm3u8File(cturl.getPath());
            FileOutputStream fos=new FileOutputStream(file);
            int lent=0;
            byte[] buffer=new byte[1024];
            while ((lent=is.read(buffer))!=-1){
                fos.write(buffer,0,lent);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (reader!=null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    public String getAddressForM3U8(String info){
        if (info.endsWith(".m3u8")){
            String last=info.substring(info.indexOf("/"));
            Log.d("b",last);
            String enother=protocol+"://"+host+last;
            Log.d("d",enother);
            return enother;
        }
        return "";
    }

}
