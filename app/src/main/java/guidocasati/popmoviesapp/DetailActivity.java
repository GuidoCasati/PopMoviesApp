package guidocasati.popmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by guidocasati on 05/03/2018.
 * some code from Udacity's Sunshine has been reused
 */


public class DetailActivity extends AppCompatActivity {

    //public static final String EXTRA_POSITION = "extra_position";
    //private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView poster_iv = findViewById(R.id.image_iv);
        ImageView poster_iv_full = findViewById(R.id.image_iv_full);

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

        ArrayList<String> intent_extra = intent.getStringArrayListExtra(intent.EXTRA_TEXT);

        if (intent_extra.size() == 5) {
            populateUI(intent_extra);
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w500/" + intent_extra.get(4))
                    .into(poster_iv);

            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w500/" + intent_extra.get(4))
                    .into(poster_iv_full);

            setTitle(intent_extra.get(0));
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(ArrayList<String> details_al) {

        TextView tv_title = findViewById(R.id.title_tv);
        TextView tv_rating = findViewById(R.id.rating_tv);
        TextView tv_release_date = findViewById(R.id.release_date_tv);
        TextView tv_plot = findViewById(R.id.plot_tv);

        String sRating = details_al.get(2);
        String sPlot = details_al.get(1);
        String sReleaseDate = details_al.get(3);
        String sTitle = details_al.get(0);

        tv_rating.setText(sRating);
        tv_plot.setText(sPlot);
        tv_title.setText(sTitle);
        tv_release_date.setText(sReleaseDate);
    }
}
