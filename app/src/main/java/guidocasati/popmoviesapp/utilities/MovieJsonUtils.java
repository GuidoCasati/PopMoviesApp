package guidocasati.popmoviesapp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import guidocasati.popmoviesapp.data.Movie;

/**
 * Created by guido casati on 03/03/2018.
 */

public final class MovieJsonUtils {

    /**
     * This method parses JSON from a web response and returns a Movie object
     * @param moviesJsonString JSON response from server
     * @return Array of Strings describing movie data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getMovieStringsFromJson(Context context, String moviesJsonString)
            throws JSONException {

        // data are in the "results" array
        final String MOVIES_LIST = "results";

        final String MOVIE_TITLE = "title";
        final String MOVIE_RATING = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String PLOT = "overview";
        final String RELEASE_DATE = "release_date";

        final String OWM_MESSAGE_CODE = "cod";

        /* String array to hold details for each movie */
        Movie[] parsedMovieData = null;

        JSONObject moviesJSON = new JSONObject(moviesJsonString);

        /* Is there an error? */
        if (moviesJSON.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJSON.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray resultsArray = moviesJSON.getJSONArray(MOVIES_LIST);

        parsedMovieData = new Movie[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the Movie */
            JSONObject movie = resultsArray.getJSONObject(i);

            String title = movie.getString(MOVIE_TITLE);
            String vote_average = movie.getString(MOVIE_RATING);
            String poster_path = movie.getString(POSTER_PATH);
            String plot = movie.getString(PLOT);
            String releaseDate = movie.getString(RELEASE_DATE);

            parsedMovieData[i] = new Movie(title,poster_path,plot,vote_average,releaseDate);
        }
        return parsedMovieData;
    }
}