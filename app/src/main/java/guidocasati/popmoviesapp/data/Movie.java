package guidocasati.popmoviesapp.data;

import java.util.HashMap;

/**
 * Created by guidocasati on 04/03/2018.
 */

public class Movie {

    private String title;
    private String posterPath;
    private String plot;
    private String rating;
    private String releaseDate;
    private String id;
    private HashMap<String,String> trailerUrls;
    private HashMap<String,String> reviews;


    public Movie(String title, String posterPath, String plot, String rating, String releaseDate, String id) {
        this.title = title;
        this.posterPath = posterPath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id=id;
    }

    public Movie(String title, String posterPath, String plot, String rating, String releaseDate, String id, HashMap<String,String> trailerUrls,  HashMap<String,String> reviews) {
        this.title = title;
        this.posterPath = posterPath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id=id;
        this.trailerUrls=trailerUrls;
        this.reviews = reviews;
    }

    public HashMap<String, String> getReviews() {
        return reviews;
    }

    public void setReviews(HashMap<String, String> reviews) {
        this.reviews = reviews;
    }

    public HashMap<String,String> getTrailerUrls() {
        return trailerUrls;
    }

    public void setTrailerUrls(HashMap<String,String> trailerUrls) {
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

}
