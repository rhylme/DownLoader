package play.com.rhyme.downloader.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/4/28.
 * 描述: [].
 */
public class FileUtil {
    private static final String parentPath="Downm3u8";
    private File parentFile;

    public FileUtil(){
        parentFile= new File(Environment.getExternalStorageDirectory(),parentPath);
        if (!parentFile.exists()){
            parentFile.mkdir();
        }
    }

    /**
     * 创建ts文件
     */

    public File createm3u8File(String path){
        File file=new File(parentFile,path.substring(0,path.lastIndexOf("/")));
        if (!file.exists()){
            file.mkdirs();
        }
        File child=new File(file,path.substring(path.lastIndexOf("/")+1,path.length()));

        if (!child.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return child;

    }

    /**
     * 检查文件是否存在
     */
    public static boolean checkFile(String resultPath){
        File parFile=new File(Environment.getExternalStorageDirectory(),parentPath);
        File resultFile=new File(parFile,resultPath);
        return resultFile.exists();
    }

    /**
     * 合并文件
     */
    public static boolean mergeFiles(String uuid, String resultPath) {
        File parFile=new File(Environment.getExternalStorageDirectory(),parentPath);
        File resultFile=new File(parFile,resultPath);
        if (!resultFile.exists()){
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File  listParFile=new File(parFile,uuid);
        if (!listParFile.exists()){
            return false;
        }
        File[] fpaths=listParFile.listFiles();
        if (fpaths == null || fpaths.length < 1) {
            return false;
        }
        if (fpaths.length == 1) {
            return fpaths[0].renameTo(resultFile);
        }
        int length=fpaths.length;

        for (int i = 0; i < length; i++) {
            if (!fpaths[i].exists() || !fpaths[i].isFile()) {
                return false;
            }
        }
        try {
            FileOutputStream fs = new FileOutputStream(resultFile, true);
            FileChannel resultFileChannel = fs.getChannel();
            FileInputStream tfs;
            for (int i = 0; i <length; i++) {
                tfs = new FileInputStream(fpaths[i]);
                FileChannel blk = tfs.getChannel();
                resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                tfs.close();
                blk.close();
            }
            fs.close();
            resultFileChannel.close();
            listParFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
