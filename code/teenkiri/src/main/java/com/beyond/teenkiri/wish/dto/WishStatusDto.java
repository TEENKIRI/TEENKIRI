package com.beyond.teenkiri.wish.dto;

public class WishStatusDto {

    private boolean isInWishlist;

    public WishStatusDto(boolean isInWishlist) {
        this.isInWishlist = isInWishlist;
    }

    public boolean isInWishlist() {
        return isInWishlist;
    }

    public void setInWishlist(boolean isInWishlist) {
        this.isInWishlist = isInWishlist;
    }
}