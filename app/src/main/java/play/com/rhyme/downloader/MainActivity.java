package play.com.rhyme.downloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import play.com.rhyme.downloader.server.ServerManager;
import play.com.rhyme.downloader.utils.M3U8Manager;
import play.com.rhyme.downloader.utils.PermissionManager;
import play.com.rhyme.downloader.utils.ThreadPoolUtil;

public class MainActivity extends AppCompatActivity {
    private static final int PEMISSION_CODE=1;
    private EditText url;
    private int i=0;
    private ServerManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        manager=new ServerManager();
        manager.start();
    }

    public void begindownload(View view) {
        if(PermissionManager.newInstance(this)
                .requestPermission(PEMISSION_CODE,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            submit();
        }
    }
    private void initView() {
        url = (EditText) findViewById(R.id.url);
        url.setText("http://pl-ali.youku.com/playlist/m3u8?vid=XMjc5NjU2ODgyOA%3D%3D&type=mp4&ups_client_netip=da13cfcb&utid=rfwHE5HI9jMCAXFC4iWFQyqz&ccode=0503&psid=e5cca05b7a56a86f3887b6007329eb0b&duration=9&expire=18000&drm_type=1&drm_device=10&ups_ts=1526207801&onOff=0&encr=0&ups_key=e4bc0c66017756958b57735e846bc333");
    }
    private void submit() {
        // validate
        final String urlString = url.getText().toString().trim();
        if (TextUtils.isEmpty(urlString)) {
            Toast.makeText(this, "下载地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        ThreadPoolUtil.newInstance().singleExcutor.execute(() -> {
            M3U8Manager m3U8Manager = new M3U8Manager("测试", urlString);
            m3U8Manager.Reuqest();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean success=true;
        switch (requestCode){
            case PEMISSION_CODE:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!= PackageManager.PERMISSION_GRANTED){
                            success=false;
                        }
                    }
                }
                break;
        }
        if (success){
            submit();
        }else {
            Toast.makeText(this,"你已经拒绝权限，请手动开启，否则后果很严重哦！",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.stop();
    }
}
