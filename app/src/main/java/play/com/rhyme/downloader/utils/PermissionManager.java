package play.com.rhyme.downloader.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;


/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/4/28.
 * 描述: [权限管理].
 */
public class PermissionManager {
    private static PermissionManager fragment ;
    private Activity context;
    public PermissionManager(Activity context){
        this.context=context;
    }
    public static PermissionManager newInstance(Activity context) {
        if (fragment==null){
            synchronized (PermissionManager.class){
                if (fragment==null){
                    fragment = new PermissionManager(context);
                }
            }
        }
        return fragment;
    }
    public boolean requestPermission(int code,String permission){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context,new String[]{permission},code);
                return false;
            }
        }
        return true;

    }
}
