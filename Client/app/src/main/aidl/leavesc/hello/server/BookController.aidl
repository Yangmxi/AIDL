package leavesc.hello.server;

import leavesc.hello.server.Book;
import leavesc.hello.server.Page;

interface BookController {

    List<Book> getBookList();

    void addBookInOut(inout Book book, inout List<Page> page);

    void addBookIn(in Book book);

    void addBookArray(in byte[] array);

    void addParcelFileDescriptor(in ParcelFileDescriptor pfd);

    void addBookOut(out Book book);

    void sendInfo(String msg);
}
