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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import play.com.rhyme.downloader.data.DataDao;
import play.com.rhyme.downloader.entity.M3U8;

public class M3U8Manager {
    public static final String TAG="M3U8Manager";
    private  URL cturl;
    private int i=1;
    private String downLoad_address;
    private M3U8 m3U8;
    private DataDao dataDao;
    private String name;

    public M3U8Manager(String name,String downLoad_address){
        this.name=name;
        this.downLoad_address=downLoad_address;
        dataDao=DataDao.getInstance();
        m3U8=dataDao.selectByName(downLoad_address);
    }

    public void Reuqest(){
        if (m3U8.getProgress()!=-1&&m3U8.getComlete()==1){
            if(FileUtil.checkFile(m3U8.getPath())){
                Log.d(TAG, "已经下载");
                return;
            }else {
                m3U8.setProgress(0);
                Log.d(TAG, "重新下载");
            }
        }
        String info = "";
        while (!info.contains(".ts")){
                if (!info.equals("")) {
                    Log.d(TAG, "寻找另外一个.m3u8");
                    if (info.contains("http")){
                        downLoad_address=info.substring(info.indexOf("http"));
                    }else {
                        downLoad_address=cturl.getProtocol()+"://"+cturl.getHost()+info.substring(info.indexOf("/"));
                    }
                }
            BufferedReader reader = null;
            InputStream is = null;
            try {
                cturl = new URL(downLoad_address);
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
//                host=cturl.getHost();
//                protocol =cturl.getProtocol();
//                Log.d("a", info);
//                Log.d("b","主机名："+cturl.getHost());
//                Log.d("c","协议名："+cturl.getProtocol());
//                Log.d("c","端口号："+cturl.getPort());
//                Log.d("c","路径："+cturl.getPath());
//                Log.d("c","资源名："+cturl.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null){
                        is.close();
                    }
                    if (reader!=null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "获取完成,开始解析ts文件地址.....");
        List<String> tsList=GetTs(info);
        if (m3U8.getProgress()==-1){//第一次下载
            Log.d("a","一共有"+tsList.size()+"个ts文件");
            String id=UUID.randomUUID().toString().replaceAll("-","");
            m3U8.setProgress(1);
            m3U8.setMax_progress(tsList.size());
            m3U8.setAddress(downLoad_address);
            m3U8.setPath(name+".mp4");
            m3U8.setId(id);
            dataDao.insertOne(m3U8);
            i=0;
        }else {//之前有下载过
            i=m3U8.getProgress();
            Log.d(TAG, "继续之前的下载,已下载到第"+i+"个文件");
        }
        int size=tsList.size();

        for (int j=i;j<size;j++){
            Download_Ts(j,m3U8.getId(),tsList.get(j));
        }


    }
    //获取ts文件
    private List<String> GetTs(String info){
        String[] addresses=info.split(",");
        List<String> formatAddresses=new ArrayList<>();

        for (String item:addresses){
            if (item.startsWith("/")){
                formatAddresses.add(cturl.getProtocol()+"://"+cturl.getHost()+item.substring(0,item.lastIndexOf("#")));
            }else {
                formatAddresses.add(item.substring(0,item.lastIndexOf("#")));
            }
        }
        return formatAddresses;
    }


    /**
     * 下载 ts文件
     * @param uuid uuid随机生成
     * @param ts_address 下载地址
     */
    private void Download_Ts(Integer position,String uuid,String ts_address){
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            URL cturl = new URL(ts_address);
            HttpURLConnection urlConnection = (HttpURLConnection) cturl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(80000);
            urlConnection.setReadTimeout(80000);
            is = urlConnection.getInputStream();
            File file =new FileUtil().createm3u8File(uuid+"/"+position+".ts");
            fos=new FileOutputStream(file);
            int len=0;
            byte[] buffer=new byte[1024];
            while ((len=is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
            fos.flush();
            Log.d("a","下载完成"+position+"个文件");
            m3U8.setProgress(position);
            if (position==m3U8.getMax_progress()-1){
                boolean ismerge=FileUtil.mergeFiles(m3U8.getId(),m3U8.getPath());
                if (ismerge){
                    Log.d(TAG,"下载成功");
                }else {
                    Log.d(TAG, "下载失败");
                }
                m3U8.setComlete(1);
            }
            dataDao.updateOne(m3U8);

        } catch (IOException e) {
            Log.d(TAG, "下载失败");
            e.printStackTrace();
        } finally {
            try {
                if (is!=null){
                    is.close();
                }
                if (fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
