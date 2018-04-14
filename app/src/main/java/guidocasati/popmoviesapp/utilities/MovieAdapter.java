package guidocasati.popmoviesapp.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import guidocasati.popmoviesapp.data.Movie;
import guidocasati.popmoviesapp.R;

/**
 * Created by guido on 03/03/2018.
 * some code has been reused from Udacity's Sunshine app
 */


/**
 * {@link MovieAdapter} adapt a list of movies to a {@link android.support.v7.widget.RecyclerView}
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovieData;

    //base poster path to be used in the picasso call
    private static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w500/";

    /**
     * An on-click handler defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieAdapterOnClickHandler mClickHandler;

    /** getters and setters
     *
      * @return Movie Data object
     */
    public Movie[] getmMovieData() {
        return mMovieData;
    }

    public void setmMovieData(Movie[] mMovieData) {
        this.mMovieData = mMovieData;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieClicked);
    }

    /**
     * Creates a MovieAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         * * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movieClicked = mMovieData[adapterPosition];
            mClickHandler.onClick(movieClicked);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  use this viewType integer to provide a different layout
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie movieItem = mMovieData[position];
        Picasso.with(movieAdapterViewHolder.itemView.getContext()).load(BASE_POSTER_PATH + movieItem.getPosterPath())
                .into(movieAdapterViewHolder.mPosterImageView);

    }

    /**
     * This method simply returns the number of items to display
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    /**
     * This method is used to set the movie data on a MovieAdapter
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(Movie[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}