package play.com.rhyme.downloader.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/4/28.
 * 描述: [].
 */
public class FileUtil {
    private String parentPath="Downm3u8";
    private File parentFile;

    public FileUtil(){
        parentFile= new File(Environment.getExternalStorageDirectory(),parentPath);
        if (!parentFile.exists()){
            parentFile.mkdir();
        }
    }

    public File createm3u8File(String path){

        File file=new File(parentFile,path.substring(0,path.lastIndexOf("/")));
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;

    }
}
