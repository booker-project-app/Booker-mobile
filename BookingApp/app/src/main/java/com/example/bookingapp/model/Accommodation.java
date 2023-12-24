package com.example.bookingapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Accommodation implements Parcelable {
    private Long id;
    private String title;
    private String description;
    private String address;
    private ArrayList<Amenity> amenities;
    private ArrayList<Integer> images;
    private boolean favourite;
    private int totalPrice;
    private int pricePerDay;
    private float rating;
    private boolean approved;

    public Accommodation(Long id, String title, String description, ArrayList<Integer> images, boolean favourite,
                         int totalPrice, int  pricePerDay, float rating, String address, ArrayList<Amenity> amenities, boolean approved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images;
        this.favourite = favourite;
        this.totalPrice = totalPrice;
        this.pricePerDay = pricePerDay;
        this.rating = rating;
        this.address = address;
        this.amenities = amenities;
        this.approved = approved;
    }

    public Accommodation() {
    }

    protected Accommodation(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        images = in.readArrayList(Integer.class.getClassLoader());
        favourite = in.readInt() == 1;
        totalPrice = in.readInt();
        pricePerDay = in.readInt();
        rating = in.readFloat();
        address = in.readString();
        amenities = in.readArrayList(Amenity.class.getClassLoader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            approved = in.readBoolean();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> image) {
        this.images = images;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }


    public boolean getFavorite() {
        return favourite;
    }

    public void setFavourite(boolean isFavorite) {
        this.favourite = isFavorite;
    }


    public float getRating() {
        return rating;
    }

    public void setImage(float rating) {
        this.rating = rating;
    }

    public boolean getApproved(){
        return approved;
    }

    public void setApproved(boolean approved){
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "AccommodationListing{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + images + '\'' +
                ", is favourite for user='" + favourite + '\'' +
                ", total price='" + totalPrice + '\'' +
                ", price per day='" + pricePerDay + '\'' +
                ", rating='" + rating + '\'' +
                ", approved='" + approved + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeList(images);
        dest.writeInt(favourite ? 1 : 0);
        dest.writeInt(totalPrice);
        dest.writeInt(pricePerDay);
        dest.writeFloat(rating);
        dest.writeString(address);
        dest.writeList(amenities);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(approved);
        }
    }

    public static final Creator<AccommodationListing> CREATOR = new Creator<AccommodationListing>() {
        @Override
        public AccommodationListing createFromParcel(Parcel in) {
            return new AccommodationListing(in);
        }

        @Override
        public AccommodationListing[] newArray(int size) {
            return new AccommodationListing[size];
        }
    };

}
