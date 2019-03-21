package leavesc.hello.server;

import android.os.Parcel;
import android.os.Parcelable;

public class Page implements Parcelable {

    private int index;

    public Page(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Page index：" + index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
    }

    public void readFromParcel(Parcel dest) {
        index = dest.readInt();
    }

    protected Page(Parcel in) {
        this.index = in.readInt();
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

}
