package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Pagination implements Parcelable {
    private final int total;
    private final int totalPages;
    private final int currentPage;

    public Pagination(int total, int totalPages, int currentPage) {
        this.total = total;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    protected Pagination(Parcel in) {
        total = in.readInt();
        totalPages = in.readInt();
        currentPage = in.readInt();
    }

    public static final Creator<Pagination> CREATOR = new Creator<Pagination>() {
        @Override
        public Pagination createFromParcel(Parcel in) {
            return new Pagination(in);
        }

        @Override
        public Pagination[] newArray(int size) {
            return new Pagination[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "total: %d, totalPages: %d, currentPage: %d", total, totalPages, currentPage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(totalPages);
        dest.writeInt(currentPage);
    }
}
