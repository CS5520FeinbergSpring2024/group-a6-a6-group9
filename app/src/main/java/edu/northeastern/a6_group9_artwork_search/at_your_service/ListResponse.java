package edu.northeastern.a6_group9_artwork_search.at_your_service;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class ListResponse {
    private final Pagination pagination;
    private final Resource[] resources;

    public ListResponse(Pagination pagination, Resource[] resources) {
        this.pagination = pagination;
        this.resources = resources;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public Resource[] getResources() {
        return resources;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "pagination: {%s}, resources: {%s}", pagination, Arrays.toString(resources));
    }
}
