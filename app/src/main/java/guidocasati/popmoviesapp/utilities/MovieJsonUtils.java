package guidocasati.popmoviesapp.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;

import guidocasati.popmoviesapp.data.Movie;

/**
 * Created by guido casati on 03/03/2018.
 */

public final class MovieJsonUtils {

    final static String OWM_MESSAGE_CODE = "cod";

    /**
     * This method parses JSON from a web response and returns a Movie object
     *
     * @param moviesJsonString JSON response from server
     * @return Array of Strings describing movie data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getMovieStringsFromJson(String moviesJsonString)
            throws JSONException {

        // data are in the "results" array
        final String MOVIES_LIST = "results";

        //fields in results array
        final String MOVIE_TITLE = "title";
        final String MOVIE_RATING = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String MOVIE_ID = "id";
        final String PLOT = "overview";
        final String RELEASE_DATE = "release_date";


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
            String id = movie.getString(MOVIE_ID);


            parsedMovieData[i] = new Movie(title, poster_path, plot, vote_average, releaseDate, id, null, null);
        }
        return parsedMovieData;
    }

    public static HashMap<String, String> getTrailersURLsFromJson(String trailersJsonString)
            throws JSONException {

        HashMap<String, String> trailersMap = new HashMap<>();

        // data are in the "results" array
        final String TRAILERS_LIST = "results";

        //fields in results array
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        final String TRAILER_SITE = "site";

        ///* String list to hold details for each movie */
        //List<String> parsedTrailersData = new ArrayList<>();

        JSONObject moviesJSON = new JSONObject(trailersJsonString);

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

        JSONArray resultsArray = moviesJSON.getJSONArray(TRAILERS_LIST);

        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the Movie */
            JSONObject trailer = resultsArray.getJSONObject(i);


            String key = trailer.getString(TRAILER_KEY);
            String name = trailer.getString(TRAILER_NAME);
            String site = trailer.getString(TRAILER_SITE);

            if (site.equalsIgnoreCase("YouTube"))
                //store in hashmap
                trailersMap.put(key, name);

            //parsedTrailersData.add(key);
        }
        return trailersMap;
    }

    public static HashMap<String, String> getReviewsURLsFromJson(String reviewsJsonString)
            throws JSONException {

        HashMap<String, String> reviewsMap = new HashMap<>();

        // data are in the "results" array
        final String REVIEWS_LIST = "results";

        //fields in results array
        final String REVIEWS_ID = "id";
        final String REVIEWS_AUTHOR = "author";
        final String REVIEWS_CONTENT = "content";

        JSONObject moviesJSON = new JSONObject(reviewsJsonString);

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

        JSONArray resultsArray = moviesJSON.getJSONArray(REVIEWS_LIST);

        for (int i = 0; i < resultsArray.length(); i++) {

            /* Get the JSON object representing the Movie */
            JSONObject reviewsJSON = resultsArray.getJSONObject(i);

            String id = reviewsJSON.getString(REVIEWS_ID);
            String sAuthor = reviewsJSON.getString(REVIEWS_AUTHOR);
            String sContent = reviewsJSON.getString(REVIEWS_CONTENT);

            //store in hashmap
            reviewsMap.put(sAuthor, sContent);
        }
        return reviewsMap;
    }
}