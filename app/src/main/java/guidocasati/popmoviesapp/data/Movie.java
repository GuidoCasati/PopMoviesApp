package guidocasati.popmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by guidocasati on 04/03/2018.
 */

public class Movie implements Parcelable {

    // parcelable
    private int mParcelData;

    //movie object
    private String title;
    private String posterPath;
    private String plot;
    private String rating;
    private String releaseDate;
    private String id;
    private HashMap<String, String> trailerUrls;
    private HashMap<String, String> reviews;


    public Movie(String title, String posterPath, String plot, String rating, String releaseDate, String id) {
        this.title = title;
        this.posterPath = posterPath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public Movie(String title, String posterPath, String plot, String rating, String releaseDate, String id, HashMap<String, String> trailerUrls, HashMap<String, String> reviews) {
        this.title = title;
        this.posterPath = posterPath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
        this.trailerUrls = trailerUrls;
        this.reviews = reviews;
    }

    /* Constructor used to recreate Movie object from parcel */
    private Movie(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.posterPath = source.readString();
        this.plot = source.readString();
        this.rating = source.readString();
        this.releaseDate = source.readString();
    }

    public HashMap<String, String> getReviews() {
        return reviews;
    }

    public void setReviews(HashMap<String, String> reviews) {
        this.reviews = reviews;
    }

    public HashMap<String, String> getTrailerUrls() {
        return trailerUrls;
    }

    public void setTrailerUrls(HashMap<String, String> trailerUrls) {
        this.trailerUrls = trailerUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.plot);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
