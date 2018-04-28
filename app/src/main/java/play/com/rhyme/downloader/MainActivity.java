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

import play.com.rhyme.downloader.utils.M3U8Util;
import play.com.rhyme.downloader.utils.PermissionManager;
import play.com.rhyme.downloader.utils.ThreadPoolUtil;

public class MainActivity extends AppCompatActivity {
    private static final int PEMISSION_CODE=1;
    private EditText url;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void begindownload(View view) {
        if(PermissionManager.newInstance(this)
                .requestPermission(PEMISSION_CODE,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            submit();
        }
    }
    private void initView() {
        url = (EditText) findViewById(R.id.url);
        url.setText("https://cdn.letv-cdn.com/20180420/3z7GWDbV/index.m3u8");
    }
    private void submit() {
        // validate
        final String urlString = url.getText().toString().trim();
        if (TextUtils.isEmpty(urlString)) {
            Toast.makeText(this, "下载地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        ThreadPoolUtil.newInstance().singleExcutor.execute(new Runnable() {
            @Override
            public void run() {
                M3U8Util m3U8Util=new M3U8Util();
                m3U8Util.RequestM3U8(urlString);
//                HttpRequest httpRequest = new HttpRequest();
//                String info=httpRequest.RequestGet(urlString);
//                if (info!=null&&!TextUtils.isEmpty(info)){
//                    String seccond=httpRequest.SecondAddress(info);
//                    if (!TextUtils.isEmpty(seccond)){
//                        String secondInfo=httpRequest.RequestGet(seccond);
//                    }
//                }
            }
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
}
