package guidocasati.popmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import guidocasati.popmoviesapp.data.Movie;
import guidocasati.popmoviesapp.data.PopMovieContract;
import guidocasati.popmoviesapp.utilities.MovieAdapter;
import guidocasati.popmoviesapp.utilities.MovieJsonUtils;
import guidocasati.popmoviesapp.utilities.NetworkUtils;


/**
 * Created by guidocasati on 05/03/2018.
 * some code from Udacity's Sunshine has been reused
 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    // --Commented out by Inspection (10/03/2018 15:06):private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay,mNoFavsMessageDisplay;
    private ProgressBar mLoadingIndicator;
    /*private SQLiteDatabase mDB;
    private Cursor cursor;*/

    private String TAG = MainActivity.class.getName();

    //bool to check if favourites option was selected
    boolean isStarred = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*        // DB handling
                PopMovieDbHelper dbHelper = new PopMovieDbHelper(this);
                mDB = dbHelper.getWritableDatabase();
                // store the result in a Cursor variable
                cursor = getStarredMovies();*/

        // referencing the RecyclerView
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // This TextView is used to display errors and will be hidden if there are no errors
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mNoFavsMessageDisplay = findViewById(R.id.tv_no_favs_available_display);

        // Grid Layout manager to arrange recycled views in a grid on the UI
        //StaggeredGridLayoutManager gridLayoutManager =  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        // Attach the layout manager to the recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // MovieAdapter will link the movie details with the views displaying the data
        mMovieAdapter = new MovieAdapter(this);

        // Attaching the adapter to the RecyclerView
        mRecyclerView.setAdapter(mMovieAdapter);


        // The ProgressBar that will indicate to the user that we are loading data. It will be hidden when no data is loading.
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // after setting up, loading movie data into the views
        loadMovieData("popular");
    }

    /*private Cursor getStarredMovies() {
            return mDB.query(PopMovieContract.StarredMovieEntry.TABLE_NAME, null,
                    null, null, null, null, null);
        }*/


    /* This method will get the sorting order and use a background thread to retrieve data*/
    private void loadMovieData(String sort) {
        showMovieDataView();
        new FetchMovieTask().execute(sort);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: is starred menu option " + isStarred);
        super.onResume();
        if (isStarred == true)
        {
           LoadStarredMovies();
        }

    }

    /**
     * This method is overridden by the MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movieClicked movie that was clicked
     */
    @Override
    public void onClick(Movie movieClicked) {
        //context
        Context context = this;
        //detail activity
        Class destinationClass = DetailActivity.class;
        //intent
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        //movie item to the detail activity
        ArrayList<String> details_al = new ArrayList<>();
        details_al.add(movieClicked.getTitle());
        details_al.add(movieClicked.getPlot());
        details_al.add(movieClicked.getRating());
        details_al.add(movieClicked.getReleaseDate());
        details_al.add(movieClicked.getPosterPath());
        details_al.add(movieClicked.getId());

        //intent to start detail activity
        intentToStartDetailActivity.putStringArrayListExtra(Intent.EXTRA_TEXT, details_al);

        //attach trailers
        HashMap<String, String> trailers = movieClicked.getTrailerUrls();
        intentToStartDetailActivity.putExtra("trailers map", trailers);

        //attach reviews
        HashMap<String, String> reviews = movieClicked.getReviews();
        intentToStartDetailActivity.putExtra("reviews", reviews);

        //add extra boolean
        intentToStartDetailActivity.putExtra("is starred", isStarred);

        //intentToStartDetailActivity.putStringArrayListExtra(Intent.EXTRA_TEXT, trailers_al.addAll(Arrays.asList(movieClicked.getTrailerUrls())));
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     */
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mNoFavsMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie recycler View.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie recycler View.
     */
    private void showNoStarredMoviesMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoFavsMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This class will override the AsyncTask
     * class for multi-threading purposes
     */
    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            // handling the sort order
            String sort = "popular";
            if (params.length != 0) {
                sort = params[0];
            }

            // building url
            URL movieRequestURL = NetworkUtils.buildUrl(sort, null);

            try {
                //get json response
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                //get movie data from response
                Movie[] moviesData = MovieJsonUtils.getMovieStringsFromJson(jsonResponse);

                List<Movie> movies = new ArrayList<>();
                if (moviesData != null) {
                    for (Movie movie :
                            moviesData) {
                        // get ID
                        String sMovieID = movie.getId();

                        // send request for trailers URL
                        URL videoRequestURL = NetworkUtils.buildUrl("videos", sMovieID);
                        // get response for trailers
                        String jsonVideosResponse = NetworkUtils.getResponseFromHttpUrl(videoRequestURL);
                        //get trailers into a HashMap
                        HashMap<String, String> trailersMap = MovieJsonUtils.getTrailersURLsFromJson(jsonVideosResponse);
                        //add to movie object
                        movie.setTrailerUrls(trailersMap);

                        // send request for reviews URL
                        URL reviewsRequestURL = NetworkUtils.buildUrl("reviews", sMovieID);
                        // get response for trailers
                        String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestURL);
                        //get reviews into a HashMap and add to movie object
                        if (jsonReviewsResponse != null){
                            HashMap<String, String> reviewsMap = MovieJsonUtils.getReviewsURLsFromJson(jsonReviewsResponse);
                            movie.setReviews(reviewsMap);
                        }
                        // add to list
                        movies.add(movie);
                    }
                }
                return movies.toArray(new Movie[movies.size()]);
                //return  moviesData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] moviesData) {
            //loaded data, hiding loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                // displaying data
                showMovieDataView();
                mMovieAdapter.setMovieData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }

/*
    // Use an async task to do the data fetch off of the main thread.
    public class StarredFetchTask extends AsyncTask<Void, Void, Cursor> {

        // Invoked on a background thread
        @Override
        protected Cursor doInBackground(Void... params) {
            // Make the query to get the data

            // Get the content resolver
            ContentResolver resolver = getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class
            Cursor cursor = resolver.query(PopMovieContract.CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }


        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            // COMPLETED (2) Initialize anything that you need the cursor for, such as setting up
            // the screen with the first word and setting any other instance variables

            //Set up a bunch of instance variables based off of the data

            // Set the data for MainActivity
            mData = cursor;
            // Get the column index, in the Cursor, of each piece of data
            mDefCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION);
            mWordCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD);

            // Set the initial state
            nextWord();
        }
    }
*/

    /**
     * Inflating menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * This method is handling clicks in the menu and sorting views accordingly
     *
     * @param item clicked menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.by_pop:
                mMovieAdapter.setMovieData(null);
                loadMovieData("popular");
                // set isStarred to false
                isStarred = false;
                break;
            case R.id.by_rating:
                mMovieAdapter.setMovieData(null);
                loadMovieData("top_rated");
                // set isStarred to false
                isStarred = false;
                break;
            case R.id.by_favourite:
                mMovieAdapter.setMovieData(null);
                showMovieDataView();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                LoadStarredMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadStarredMovies() {

        //get new cursor
        Cursor cursor = getContentResolver().query(PopMovieContract.StarredMovieEntry.CONTENT_URI,
                null, null, null, null);

        //starred movies count
        int count = cursor.getCount();
        //Log.d(TAG, "onClick: cursor.getCount() " + count);

        //new array hosting movies to be loaded
        Movie[] favMovies = new Movie[count];
        int index;

        while (cursor.moveToNext()) {

            index = cursor.getColumnIndexOrThrow(PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE);
            String sStarredTitle = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(PopMovieContract.StarredMovieEntry.COLUMN_POSTER_PATH);
            String sPosterPath = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(PopMovieContract.StarredMovieEntry.COLUMN_PLOT);
            String sPlot = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(PopMovieContract.StarredMovieEntry.COLUMN_RATING);
            String sRating = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(PopMovieContract.StarredMovieEntry.COLUMN_RELEASE_DATE);
            String sReleaseDate = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(PopMovieContract.StarredMovieEntry._ID);
            String sID = cursor.getString(index);

            favMovies[cursor.getPosition()] = new Movie(sStarredTitle, sPosterPath, sPlot, sRating, sReleaseDate, sID, null, null);
            //Log.d(TAG, "StarredTitle " + sStarredTitle + " " + sPosterPath);
        }
        //Log.d(TAG, "onOptionsItemSelected: favMovies.length " + favMovies.length);
        //loading movies on UI
        if (favMovies.length > 0) {
            // displaying data
            showMovieDataView();
            mMovieAdapter.setMovieData(favMovies);
        } else {
            showNoStarredMoviesMessage();
        }
        // set isStarred to true
        isStarred = true;
    }
}