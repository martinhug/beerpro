package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyBeerFromRating implements MyBeer {
    private Rating rating;
    private Beer beer;

    public MyBeerFromRating(Rating rating, Beer beer) {
        this.rating = rating;
        this.beer = beer;
    }

    @Override
    public FridgeItem getFridgeiten() {
        return null;
    }

    @Override
    public void setFridgeItem(FridgeItem fridgeItem) {

    }

    @Override
    public String getBeerId() {
        return rating.getBeerId();
    }

    @Override
    public Date getDate() {
        return rating.getCreationDate();
    }

    public Rating getRating() {
        return this.rating;
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MyBeerFromRating)) return false;
        final MyBeerFromRating other = (MyBeerFromRating) o;
        if (!other.canEqual(this)) return false;
        final Object this$rating = this.getRating();
        final Object other$rating = other.getRating();
        if (this$rating == null ? other$rating != null : !this$rating.equals(other$rating))
            return false;
        final Object this$beer = this.getBeer();
        final Object other$beer = other.getBeer();
        return this$beer == null ? other$beer == null : this$beer.equals(other$beer);
    }

    private boolean canEqual(final Object other) {
        return other instanceof MyBeerFromRating;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $rating = this.getRating();
        result = result * PRIME + ($rating == null ? 43 : $rating.hashCode());
        final Object $beer = this.getBeer();
        result = result * PRIME + ($beer == null ? 43 : $beer.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "MyBeerFromRating(rating=" + this.getRating() + ", beer=" + this.getBeer() + ")";
    }
}
