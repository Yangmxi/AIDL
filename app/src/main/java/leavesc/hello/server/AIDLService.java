package leavesc.hello.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {

    private final String TAG = "Server";

    private List<Book> bookList;

    public AIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bookList = new ArrayList<>();
        initData();
    }

    private void initData() {
        Page page = new Page(222222);
        List<Page> pageList = new ArrayList<>();
        pageList.add(page);
        Book book = new Book("人月神话", pageList);
        bookList.add(book);
    }

    private final BookController.Stub stub = new BookController.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBookInOut(Book book, List<Page> pageList) throws RemoteException {
            if (book != null) {
                if (pageList == null) {
                    Log.e(TAG, "get page is null");
                } else {
                    Log.e(TAG, "page :" + pageList.get(0));
                }
                book.setName("服务器改了新书的名字 InOut");
                book.setPage(pageList);
                Log.e(TAG, "************ add book inout 之后 book.getPage :" + book.getPage().get(0));
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 InOut");
            }
        }

        @Override
        public void addBookIn(Book book) throws RemoteException {
            if (book != null) {
                book.setName("服务器改了新书的名字 In");
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 In");
            }
        }

        @Override
        public void addBookArray(byte[] array) {
            if (array != null) {
                Log.e(TAG, "Image array length : " + array.length);
                try {
                    buff2Image(array, "/sdcard/Download/test.jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "接收到了一个空对象 In");
            }
        }

        @Override
        public void sendInfo(String msg) {
            Log.e(TAG, "服务端的进程号 ：" + android.os.Process.myPid());
            if (msg != null) {
                Log.e(TAG, "收到来自客户端的信息 : " + msg);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("msg", msg);
                message.setData(bundle);
                Log.e(TAG, "发送msg到MainActivity 线程中更新UI...");
                new MainActivity.MyHandler(AIDLService.this, getMainLooper()).sendMessage(message);
            } else {
                Log.e(TAG, "客户端传来的信息为null");
            }
        }

        @Override
        public void addParcelFileDescriptor(ParcelFileDescriptor pfd) {
            if (pfd != null) {
                // 通过管道的read端包装输入流
                ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream =
                        new ParcelFileDescriptor.AutoCloseInputStream(pfd);

                try {
                    SystemClock.sleep(2000);
                    byte[] buf = new byte[15];
                    Log.e(TAG, "接收到了一个空对象 pfd" + autoCloseInputStream.read(buf));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "接收到了一个空对象 pfd");
            }
        }

        @Override
        public void addBookOut(Book book) throws RemoteException {
            if (book != null) {
                Log.e(TAG, "客户端传来的书的名字：" + book.getName());
                book.setName("服务器改了新书的名字 Out");
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 Out");
            }
        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private static void buff2Image(byte[] b, String tagSrc) throws Exception {
        FileOutputStream fout = new FileOutputStream(tagSrc);
        //将字节写入文件
        fout.write(b);
        fout.close();
    }
}
