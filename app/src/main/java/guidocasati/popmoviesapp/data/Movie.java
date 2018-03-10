package guidocasati.popmoviesapp.data;

/**
 * Created by guidocasati on 04/03/2018.
 */

public class Movie {

    private String title;
    private String posterPath;
    private String plot;
    private String rating;
    private String releaseDate;

    public Movie(String title, String posterPath, String plot, String rating, String releaseDate) {
        this.title = title;
        this.posterPath = posterPath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
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


}
