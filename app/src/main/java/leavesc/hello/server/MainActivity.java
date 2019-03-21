package leavesc.hello.server;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 作者：leavesC
 * 时间：2019/2/11 20:40
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Server";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static ImageView mImage;
    private static int index = 0;
    private static final int SIZE_PIC = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        mImage = (ImageView) findViewById(R.id.image);
    }

    public static class MyHandler extends Handler {
        WeakReference<Context> weakReference;

        public MyHandler(Context context, Looper looper) {
            super(looper);
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 2:
                    String path = "/sdcard/" + index % SIZE_PIC + ".jpg";
                    File file = new File(path);
                    if (file.exists()) {
                        mImage.setImageURI(Uri.fromFile(file));
                        index++;
                    }
                    Log.e(TAG, "更新图片完毕，时间：" + System.currentTimeMillis());
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * android 动态权限申请
     */
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
