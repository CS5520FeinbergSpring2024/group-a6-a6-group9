package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class ListResponse implements Parcelable {
    private final Pagination pagination;
    private final String resourceType;
    private final Resource[] resources;

    public ListResponse(Pagination pagination, String resourceType, Resource[] resources) {
        this.pagination = pagination;
        this.resourceType = resourceType;
        this.resources = resources;
    }

    protected ListResponse(Parcel in) {
        pagination = in.readTypedObject(Pagination.CREATOR);
        resourceType = in.readString();
        if (resourceType.equals(Artwork.class.getSimpleName())) {
            resources = in.createTypedArray(Artwork.CREATOR);
        } else {
            resources = null;
        }
    }

    public static final Creator<ListResponse> CREATOR = new Creator<ListResponse>() {
        @Override
        public ListResponse createFromParcel(Parcel in) {
            return new ListResponse(in);
        }

        @Override
        public ListResponse[] newArray(int size) {
            return new ListResponse[size];
        }
    };

    public Pagination getPagination() {
        return pagination;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Resource[] getResources() {
        return resources;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "pagination: {%s}, resources: {%s}", pagination, Arrays.toString(resources));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedObject(pagination, flags);
        dest.writeString(resourceType);
        dest.writeTypedArray(resources, flags);
    }
}
