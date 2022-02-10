package amner.android.com.movies.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author amner
 */

public class Movie implements Parcelable {

    protected Movie(Parcel in) {
        movieId = in.readString();
        title = in.readString();
        poster = in.readString();
        releaseYear = in.readString();
        rating = in.readString();
        overview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(releaseYear);
        dest.writeString(rating);
        dest.writeString(overview);
    }

    public enum PREFERENCE {
        WISH, LIKED, IGNORED
    }

    private String movieId;
    private PREFERENCE moviePreference;
    private String title;
    private String poster;
    private String releaseYear;
    private String overview;
    private String rating;

    public String getmTitle() {
        return title;
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    public void setRating(String rating) {
        this.rating = rating;
    }

    public Movie() {
    }

    public Movie(String mId, String mTitle) {
        this.movieId = mId;
        this.title = mTitle;
        this.moviePreference = PREFERENCE.IGNORED;
    }

    public String getMovieId() {
        return movieId;
    }

    public PREFERENCE getMoviePreference() {
        return moviePreference;
    }

    public void setMoviePreference(PREFERENCE moviePreference) {
        this.moviePreference = moviePreference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return movieId.equals(movie.movieId);
    }

    @Override
    public int hashCode() {
        return movieId.hashCode();
    }
}
