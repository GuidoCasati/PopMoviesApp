package guidocasati.popmoviesapp.utilities;

/*
  Created by Guido Casati on 03/03/2018.
  Inspired by Udacity's Sunshine project
 */


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie db servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_MOVIEDB_URL = "http://api.themoviedb.org/3/movie/";

    // query parameters
    private final static String API_KEY = "api_key";
    private final static String LANG = "language";
    private final static String PAGE = "PAGE";
    // --Commented out by Inspection (10/03/2018 15:07):final static String TOP_RATED = "top_rated";
    // --Commented out by Inspection (10/03/2018 15:07):final static String POPULAR = "popular";

    /* Page to return */
    private static final String pageNumber = "1";
    private static final String apiKey = "YOUR API KEY HERE";

    /**
     * Builds the URL used to query the movie db server.
     *
     * @return The URL to use to query the movie db server.
     */
    public static URL buildUrl(String query, String path) {
        Uri builtUri = Uri.parse(BASE_MOVIEDB_URL);
        switch (query) {
            case "popular":
            case "top_rated":
                builtUri = builtUri.buildUpon()
                        .appendPath(query)
                        .appendQueryParameter(API_KEY, apiKey)
                        .appendQueryParameter(PAGE, pageNumber)
                        .build();
                break;
            case "reviews":
                builtUri = builtUri.buildUpon()
                        .appendPath(path)
                        .appendPath(query)
                        .appendQueryParameter(API_KEY, apiKey)
                        .appendQueryParameter(LANG, "en-US")
                        .appendQueryParameter(PAGE, pageNumber)
                        .build();
                break;
            case "videos":
                builtUri = builtUri.buildUpon()
                        .appendPath(path)
                        .appendPath(query)
                        .appendQueryParameter(API_KEY, apiKey)
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method has been taken from Udacity's Sunshine App.
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e)
        {
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }
}