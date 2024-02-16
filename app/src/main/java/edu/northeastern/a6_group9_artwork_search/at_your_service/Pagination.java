package edu.northeastern.a6_group9_artwork_search.at_your_service;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Pagination {
    private final int total;
    private final int totalPages;
    private final int currentPage;

    public Pagination(int total, int totalPages, int currentPage) {
        this.total = total;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

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
}
