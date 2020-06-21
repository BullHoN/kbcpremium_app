package com.avit.kbcpremium.ui.booking;


import android.os.Parcel;
import android.os.Parcelable;

public class SelectedItem implements Parcelable {

    private String name;
    private String option;
    private String price;

    public SelectedItem(String name, String option) {
        this.name = name;
        this.option = option;
    }

    protected SelectedItem(Parcel in) {
        name = in.readString();
        option = in.readString();
        price = in.readString();
    }

    public static final Creator<SelectedItem> CREATOR = new Creator<SelectedItem>() {
        @Override
        public SelectedItem createFromParcel(Parcel in) {
            return new SelectedItem(in);
        }

        @Override
        public SelectedItem[] newArray(int size) {
            return new SelectedItem[size];
        }
    };

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", option='" + option + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(option);
        dest.writeString(price);
    }
}
