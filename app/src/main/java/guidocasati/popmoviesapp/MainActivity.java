package guidocasati.popmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import guidocasati.popmoviesapp.data.Movie;
import guidocasati.popmoviesapp.utilities.MovieAdapter;
import guidocasati.popmoviesapp.utilities.MovieJsonUtils;
import guidocasati.popmoviesapp.utilities.NetworkUtils;

/**
 * Created by guidocasati on 05/03/2018.
 * some code from Udacity's Sunshine has been reused
 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // referencing the RecyclerView
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // This TextView is used to display errors and will be hidden if there are no errors
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        // Grid Layout manager to arrange recycled views in a grid on the UI
        //StaggeredGridLayoutManager gridLayoutManager =  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(this, 2);

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

    /* This method will get the sorting order and use a background thread to retrieve data*/
    private void loadMovieData(String sort) {
        showMovieDataView();
        new FetchMovieTask().execute(sort);
    }

    /**
     * This method is overridden by the MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movieClicked movie that was clicked
     */
    @Override
    public void onClick(Movie movieClicked) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        ArrayList<String> details_al = new ArrayList<>();
        details_al.add(movieClicked.getTitle());
        details_al.add(movieClicked.getPlot());
        details_al.add(movieClicked.getRating());
        details_al.add(movieClicked.getReleaseDate());
        details_al.add(movieClicked.getPosterPath());
        intentToStartDetailActivity.putStringArrayListExtra(Intent.EXTRA_TEXT, details_al);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     */
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie View.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
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
            URL movieRequestURL = NetworkUtils.buildUrl(sort);

            try {
                //get json response
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                //get movie data from response
                Movie[] movieData = MovieJsonUtils.getMovieStringsFromJson(MainActivity.this, jsonResponse);
                return movieData;
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

    /**
     * Inflating menu
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
     * @param item clicked menu itam
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.by_pop) {
            mMovieAdapter.setMovieData(null);
            loadMovieData("popular");
            return true;
        }
        if (id == R.id.by_rating) {
            mMovieAdapter.setMovieData(null);
            loadMovieData("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}