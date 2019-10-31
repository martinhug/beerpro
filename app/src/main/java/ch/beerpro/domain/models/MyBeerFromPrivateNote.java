package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyBeerFromPrivateNote implements MyBeer {
    private String privateNote;
    private Beer beer;

    public MyBeerFromPrivateNote(String privateNote, Beer beer) {
        this.privateNote = privateNote;
        this.beer = beer;
    }


    @Override
    public String getBeerId() {
        return beer.getId();
    }

    @Override
    public Date getDate() {
        return new Date();
    }

    public String getPrivateNote() {
        return this.privateNote;
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MyBeerFromPrivateNote)) return false;
        final MyBeerFromPrivateNote other = (MyBeerFromPrivateNote) o;
        if (!other.canEqual(this)) return false;
        final Object this$rating = this.getPrivateNote();
        final Object other$rating = other.getPrivateNote();
        if (this$rating == null ? other$rating != null : !this$rating.equals(other$rating))
            return false;
        final Object this$beer = this.getBeer();
        final Object other$beer = other.getBeer();
        return this$beer == null ? other$beer == null : this$beer.equals(other$beer);
    }

    private boolean canEqual(final Object other) {
        return other instanceof MyBeerFromPrivateNote;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $rating = this.getPrivateNote();
        result = result * PRIME + ($rating == null ? 43 : $rating.hashCode());
        final Object $beer = this.getBeer();
        result = result * PRIME + ($beer == null ? 43 : $beer.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "MyBeerFromPrice(rating=" + this.getPrivateNote() + ", beer=" + this.getBeer() + ")";
    }
}
