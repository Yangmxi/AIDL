package leavesc.hello.server;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Book implements Parcelable {

    private String name;
    private List<Page> page;

    public Book() {

    }

    public List<Page> getPage() {
        return page;
    }

    public void setPage(List<Page> page) {
        this.page = page;
    }

    public Book(String name, List<Page> page) {
        this.name = name;
        this.page = page;
    }


    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String res;
        if (page == null) {
            res = "book name：" + name + " Page is null";
        } else {
            res = "book name：" + name + " Page :" + page.get(0);
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
//        dest.writeParcelable(page, flags);
        dest.writeTypedList(page);
    }

    public void readFromParcel(Parcel dest) {
        name = dest.readString();
        List<Page> pageTmp = new ArrayList<>();
        dest.readTypedList(pageTmp, Page.CREATOR);
        this.page = pageTmp;
    }

    protected Book(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

}
