package com.drago.craze.models;

import android.graphics.drawable.Drawable;

import com.drago.craze.FilterDialogFragment;

public class Filter {
    private FilterDialogFragment.sort sorting;
    private FilterDialogFragment.order ordering;
    private boolean selectedUser;
    private boolean selectedSystem;

    public Filter(FilterDialogFragment.sort sorting, FilterDialogFragment.order ordering, boolean selectedUser, boolean selectedSystem) {
        this.sorting = sorting;
        this.ordering = ordering;
        this.selectedUser = selectedUser;
        this.selectedSystem = selectedSystem;
    }

    public FilterDialogFragment.sort getSorting() {
        return sorting;
    }

    public void setSorting(FilterDialogFragment.sort sorting) {
        this.sorting = sorting;
    }

    public FilterDialogFragment.order getOrdering() {
        return ordering;
    }

    public void setOrdering(FilterDialogFragment.order ordering) {
        this.ordering = ordering;
    }

    public boolean isSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(boolean selectedUser) {
        this.selectedUser = selectedUser;
    }

    public boolean isSelectedSystem() {
        return selectedSystem;
    }

    public void setSelectedSystem(boolean selectedSystem) {
        this.selectedSystem = selectedSystem;
    }
}
