package guidocasati.popmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import guidocasati.popmoviesapp.data.PopMovieContract;
import guidocasati.popmoviesapp.utilities.ReviewsAdapter;
import guidocasati.popmoviesapp.utilities.TrailersAdapter;

/**
 * Created by guidocasati on 05/03/2018.
 * some code from Udacity's Sunshine has been reused
 */


public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerAdapterOnClickHandler, ReviewsAdapter.ReviewsAdapterOnClickHandler{

    //public static final String EXTRA_POSITION = "extra_position";
    //private static final int DEFAULT_POSITION = -1;
    String TAG = DetailActivity.class.getName();

    private String sTitle, sPosterPath, sRating, sPlot, sReleaseDate, sID;
    private ImageView star_iv;

    private boolean isStarred;
    /*    private Cursor cursor;
            private int count;
            private SQLiteDatabase mDB;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);

        ImageView poster_iv = findViewById(R.id.image_iv);
        ImageView poster_iv_full = findViewById(R.id.image_iv_full);
        // ImageView starred movie
        star_iv = findViewById(R.id.Starred);

        //intent handling
        Intent intent = getIntent();
        if (intent == null) {
            Log.e("intent", "onCreate: fail");
            closeOnError();
        }

        /*int position = intent.getStringArrayListExtra(intent.EXTRA_TEXT);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            Log.e("intent", "EXTRA_POSITION not found in intent");
            closeOnError();
            return;
        }*/

        // extra info for movie detail activity
        ArrayList<String> intent_extra = intent.getStringArrayListExtra(intent != null ? intent.EXTRA_TEXT : null);

        if (intent_extra.size() == 6) {
            // retrieving fields from the intent
            sTitle = intent_extra.get(0);
            sPlot = intent_extra.get(1);
            sRating = intent_extra.get(2);
            sReleaseDate = intent_extra.get(3);
            sID = intent_extra.get(5);
            // get poster and fill imageview
            sPosterPath = intent_extra.get(4);
            populateUI(intent_extra);
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w500/" + sPosterPath)
                    .into(poster_iv);

            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w500/" + sPosterPath)
                    .into(poster_iv_full);

            setTitle(sTitle);
        }

        // filling trailers list recycler view
        RecyclerView trailerRecyclerView = findViewById(R.id.recyclerviewTrailers);
        RecyclerView reviewsRecyclerView = findViewById(R.id.recyclerviewReviews);

        //get serializable extra trailers
        Map<String, String> trailersMap = (Map<String, String>) getIntent().getSerializableExtra(intent != null ? "trailers map" : null);
        //Log.d(TAG, "onCreate: trailers_al " + trailersMap);
        if (trailersMap != null) {
            TrailersAdapter trailersAdapter = new TrailersAdapter(this, trailersMap, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            trailerRecyclerView.setLayoutManager(linearLayoutManager);
            trailerRecyclerView.setAdapter(trailersAdapter);
        }

        //get extra reviews
        Map<String, String> reviews = (Map<String, String>) getIntent().getSerializableExtra(intent != null ? "reviews" : null);
        //Log.d(TAG, "onCreate: trailers_al " + reviews);
        if (reviews != null) {
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, reviews, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            reviewsRecyclerView.setLayoutManager(linearLayoutManager);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        }


        //checking if favourite menu option was selected
        boolean isStarred = (boolean) getIntent().getSerializableExtra(intent != null ? "is starred" : null);

        /*// DB handling : legacy
        PopMovieDbHelper dbHelper = new PopMovieDbHelper(this);
        mDB = dbHelper.getWritableDatabase();
        cursor = getStarredMovies(sTitle);
        count = cursor.getCount();
        Log.e(TAG, "onCreate: count cursor" + count);

        StarMovie(count, star_iv);*/

        //retrieving starred movies
        //get new cursor
        Cursor cursor = getContentResolver().query(PopMovieContract.StarredMovieEntry.CONTENT_URI,
                null, PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE + " = ?", new String[]{sTitle}, null);
        //count
        int count = cursor.getCount();
        //manage star imageView
        StarMovie(count, star_iv);
        cursor.close();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get new cursor
                Cursor cursor = getContentResolver().query(PopMovieContract.StarredMovieEntry.CONTENT_URI,
                        null, PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE + " = ?", new String[]{sTitle}, null);
                //count
                int count = cursor.getCount();

                //db handling
                if (count == 0) {
                    // Create new empty ContentValues object
                    ContentValues contentValues = new ContentValues();
                    // Put the movie title into the ContentValues
                    contentValues.put(PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE, sTitle);
                    contentValues.put(PopMovieContract.StarredMovieEntry.COLUMN_POSTER_PATH, sPosterPath);
                    contentValues.put(PopMovieContract.StarredMovieEntry.COLUMN_RATING, sRating);
                    contentValues.put(PopMovieContract.StarredMovieEntry.COLUMN_RELEASE_DATE, sReleaseDate);
                    contentValues.put(PopMovieContract.StarredMovieEntry.COLUMN_PLOT, sPlot);
                    contentValues.put(PopMovieContract.StarredMovieEntry._ID, sID);

                    // Insert the content values via a ContentResolver
                    Uri uri = getContentResolver().insert(PopMovieContract.StarredMovieEntry.CONTENT_URI, contentValues);
                    Log.d(TAG, "onClick: uri " + uri.toString());

                    //display information
                    Snackbar.make(view, "Movie starred!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //star movie
                    StarMovie(1, star_iv);
                } else {
                    //remove from DB
                    getContentResolver().delete(PopMovieContract.StarredMovieEntry.CONTENT_URI,
                            PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE + "=?", new String[]{sTitle});

                    // display information
                    Snackbar.make(view, "Movie unstarred!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //hide IV
                    StarMovie(0, star_iv);

                }

                /*if (count == 0)
                {
                    //insert into DB
                    mDB.execSQL("INSERT INTO " + PopMovieContract.StarredMovieEntry.TABLE_NAME + "("
                            + PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE + ") "+ "VALUES (\""
                            + sTitle + "\");");

                    //display information
                    Snackbar.make(view, "Movie starred!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    *//*ContentValues cv = new ContentValues();
                    cv.put(PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE,sTitle);
                    mDB.insert(PopMovieContract.StarredMovieEntry.TABLE_NAME,null,cv);*//*
                }
                else{
                    // display information
                    Snackbar.make(view, "Movie unstarred!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //delete entry from db
                    mDB.execSQL("DELETE FROM " + PopMovieContract.StarredMovieEntry.TABLE_NAME + " WHERE "
                            + PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE + " = \"" + sTitle + "\";");
                }

                //close cursor
                cursor.close();
                //get new cursor
                cursor = getStarredMovies(sTitle);
                //updated count
                count = cursor.getCount();
                StarMovie(count, star_iv);*/
                //close cursor
                cursor.close();
            }
        });
    }

    private void StarMovie(int count, ImageView star) {
        if (count == 1)
            star.setVisibility(View.VISIBLE);
        else
            star.setVisibility(View.INVISIBLE);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    /*private Cursor getStarredMovies(String movieTitle) {
        return mDB.query(PopMovieContract.StarredMovieEntry.TABLE_NAME, new String[] {PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE},
                PopMovieContract.StarredMovieEntry.COLUMN_MOVIE_TITLE + "=?",new String[] {movieTitle},
                null, null, null);
    }*/

    private void populateUI(ArrayList<String> details_al) {

        TextView tv_title = findViewById(R.id.title_tv);
        TextView tv_rating = findViewById(R.id.rating_tv);
        TextView tv_release_date = findViewById(R.id.release_date_tv);
        TextView tv_plot = findViewById(R.id.plot_tv);

        String sRating = details_al.get(2);
        String sPlot = details_al.get(1);
        String sReleaseDate = details_al.get(3);
        String sTitle = details_al.get(0);

        tv_rating.setText(sRating + "/10");
        tv_plot.setText(sPlot);
        tv_title.setText(sTitle);
        tv_release_date.setText(sReleaseDate);
    }

    @Override
    public void onClick(String trailerURL) {
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + trailerURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.createChooser(intent, "title");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
