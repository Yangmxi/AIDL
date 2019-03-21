package leavesc.hello.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import leavesc.hello.server.Book;
import leavesc.hello.server.BookController;
import leavesc.hello.server.Page;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final String TAG = "Client";

    private BookController bookController;

    private boolean connected;

    private List<Book> bookList;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookController = BookController.Stub.asInterface(service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getBookList:
                    if (connected) {
                        try {
                            bookList = bookController.getBookList();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        log();
                    }
                    break;
                case R.id.btn_addBook_inOut:
                    if (connected) {
                        Book book = new Book("这是一本新书 InOut");
                        try {
                            Page page1 = new Page(333333);
                            Page page2 = new Page(4444);
                            List<Page> pageList = new ArrayList<>();
                            pageList.add(page1);
                            pageList.add(page2);
                            bookController.addBookInOut(book, pageList);
                            Log.e(TAG, "向服务器以InOut方式添加了一本新书");
                            if (book.getPage() != null) {
                                Log.e(TAG, "page :" + book.getPage().get(0));
                            }
                            Log.e(TAG, "新书名：" + book.getName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_addBook_in:
                    if (connected) {
                       /* Book book = new Book("这是一本新书 In");
                        try {
                            bookController.addBookIn(book);
                            Log.e(TAG, "向服务器以In方式添加了一本新书");
                            Log.e(TAG, "新书名：" + book.getName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }*/
                        /*byte[] array = new byte[0];
                        try {
                            array = image2Bytes("/sdcard/1.jpg");
                        } catch (Exception e) {
                            Log.e(TAG, "图片转换数组异常: " + e.toString());
                        }
                        try {
                            bookController.addBookArray(array);
                            Log.e(TAG, "向服务器以In方式发送数组");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }*/
/*                        try
                        {
                            ParcelFileDescriptor[] pfd = ParcelFileDescriptor.createPipe();
                            ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(pfd[1]);
                            ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(pfd[0]);
//                            new WriteFileTask(filePath, autoCloseOutputStream).execute();
                            bookController.addParcelFileDescriptor(pfd[0]);

                            byte[] array = new byte[0];
                            try {
                                array = image2Bytes("/sdcard/2.jpg");
                            } catch (Exception e) {
                                Log.e(TAG, "图片转换数组异常: " + e.toString());
                            }
                            autoCloseOutputStream.write(array);
                            int len = -99;
//                            len = autoCloseInputStream.read();
                            Log.e(TAG, "管道读的字节长度: " + len);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        catch (RemoteException e)
                        {
                            e.printStackTrace();
                        }*/

                        try {
                            Log.e(TAG, "客户端的进程号 ：" + android.os.Process.myPid());
                            Log.e(TAG, "从客户端向服务端发送信息...");
                            bookController.sendInfo("current time = " + System.currentTimeMillis());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_addBook_out:
                    if (connected) {
                        Book book = new Book("这是一本新书 Out");
                        try {
                            bookController.addBookOut(book);
                            Log.e(TAG, "向服务器以Out方式添加了一本新书");
                            Log.e(TAG, "新书名：" + book.getName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        findViewById(R.id.btn_getBookList).setOnClickListener(clickListener);
        findViewById(R.id.btn_addBook_inOut).setOnClickListener(clickListener);
        findViewById(R.id.btn_addBook_in).setOnClickListener(clickListener);
        findViewById(R.id.btn_addBook_out).setOnClickListener(clickListener);
        bindService();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(serviceConnection);
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setPackage("leavesc.hello.server");
        intent.setAction("leavesc.hello.server.action");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void log() {
        for (Book book : bookList) {
            Log.e(TAG, book.toString());
        }
    }

    private static byte[] image2Bytes(String imgSrc) throws Exception {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes = new byte[fin.available()];
        //将文件内容写入字节数组，提供测试的case
        fin.read(bytes);
        fin.close();
        return bytes;
    }
}
