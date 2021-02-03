package com.bardisammar.elsalamcity.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.bardisammar.elsalamcity.cart.roomdatabase.DateConverter;

@Entity(tableName = "products")
@TypeConverters({DateConverter.class})
public class Pro_Of_Product implements Parcelable {

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "descShort")
    private String descShort;

    @ColumnInfo(name = "description")
    private  String description;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "nameOfMarket")
    private String nameOfMarket;

    @ColumnInfo(name = "phoneOfMarket")
    private String phoneOfMarket;

    @ColumnInfo(name = "locationOfMarket")
    private String locationOfMarket;


    @ColumnInfo(name = "image")
    private String image;

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "deleivary")
    private boolean deleivary;


    protected Pro_Of_Product(Parcel in) {
        name = in.readString();
        descShort = in.readString();
        description = in.readString();
        price = in.readString();
        nameOfMarket = in.readString();
        phoneOfMarket = in.readString();
        locationOfMarket = in.readString();
        image = in.readString();
        uid = in.readInt();
        deleivary = in.readByte() != 0;
    }

    public static final Creator<Pro_Of_Product> CREATOR = new Creator<Pro_Of_Product>() {
        @Override
        public Pro_Of_Product createFromParcel(Parcel in) {
            return new Pro_Of_Product(in);
        }

        @Override
        public Pro_Of_Product[] newArray(int size) {
            return new Pro_Of_Product[size];
        }
    };

    public String getLocationOfMarket() {
        return locationOfMarket;
    }

    public void setLocationOfMarket(String locationOfMarket) {
        this.locationOfMarket = locationOfMarket;
    }

    public String getPhoneOfMarket() {
        return phoneOfMarket;
    }

    public void setPhoneOfMarket(String phoneOfMarket) {
        this.phoneOfMarket = phoneOfMarket;
    }

    public  @Ignore Pro_Of_Product(String name, String descShort, String description, String price,
                                   String nameOfMarket, String phoneOfMarket, String locationOfMarket, String image,boolean deleivary) {
        this.name = name;
        this.descShort = descShort;
        this.description = description;
        this.price = price;
        this.nameOfMarket = nameOfMarket;
        this.phoneOfMarket=phoneOfMarket;
        this.locationOfMarket =locationOfMarket ;
        this.image = image;
        this.deleivary=deleivary;

    }

    public  @Ignore Pro_Of_Product(String name, String descShort, String description, String price, String nameOfMarket, String image) {
        this.name = name;
        this.descShort = descShort;
        this.description = description;
        this.price = price;
        this.nameOfMarket = nameOfMarket;
        this.image = image;
    }

    public Pro_Of_Product() {
    }

    public boolean isDeleivary() {
        return deleivary;
    }

    public void setDeleivary(boolean deleivary) {
        this.deleivary = deleivary;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }





    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescShort() {
        return descShort;
    }

    public void setDescShort(String descShort) {
        this.descShort = descShort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNameOfMarket() {
        return nameOfMarket;
    }

    public void setNameOfMarket(String nameOfMarket) {
        this.nameOfMarket = nameOfMarket;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(descShort);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(nameOfMarket);
        dest.writeString(phoneOfMarket);
        dest.writeString(locationOfMarket);
        dest.writeString(image);
        dest.writeInt(uid);
        dest.writeByte((byte) (deleivary ? 1 : 0));
    }
}
