package guidocasati.popmoviesapp.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import guidocasati.popmoviesapp.R;

/**
 * Created by guido on 30/03/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private Context context;
    private List<String> authorsList;
    private List<String> contentList;
    /**
     * An on-click handler defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final ReviewsAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ReviewsAdapterOnClickHandler {
        void onClick(String trailerURL);
    }


    /**
     * Creates a TrailersAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ReviewsAdapter(Context context, List<String> authors, List<String> reviews, ReviewsAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.authorsList = authors;
        this.contentList = reviews;
        mClickHandler = clickHandler;
    }

    /**
     * Creates a TrailersAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ReviewsAdapter(Context context, Map<String,String> reviewsMap, ReviewsAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.authorsList = new ArrayList<>(reviewsMap.values());
        this.contentList = new ArrayList<>(reviewsMap.keySet());
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  use this viewType integer to provide a different layout
     * @return A new TrailersViewHolder that holds the View for each list item
     */
    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reviews_list_item, viewGroup, false);
        return new ReviewsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, final int position) {
        /*        holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!contentList.equals("NOT AVAILABLE")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(contentList.get(position)));
                            context.startActivity(intent);
                        }
                    }
                });*/
        holder.reviews_tv.setText(authorsList.get(position));
    }

    @Override
    public int getItemCount() {
        return authorsList.size();
    }


    /**
     * Cache of the children views for a trailer item.
     */
    public class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        //@BindView(R.id.trailer_card_view)
        //CardView cardView;
        //@BindView(R.id.trailer_text_view)

        TextView reviews_tv;

        public ReviewsViewHolder(View view) {
            super(view);
            reviews_tv = view.findViewById(R.id.reviews_tv);
            view.setOnClickListener(this);
            //ButterKnife.bind(this, view);
        }

        /**
         * This gets called by the child views during a click.
         * * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailerURL = contentList.get(adapterPosition);
            mClickHandler.onClick(trailerURL);
        }
    }
}