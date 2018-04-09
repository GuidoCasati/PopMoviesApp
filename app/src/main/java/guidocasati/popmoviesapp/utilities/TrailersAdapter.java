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

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private Context context;
    private List<String> trailerList;
    private List<String> trailerUrlList;
    /**
     * An on-click handler defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final TrailerAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerURL);
    }


    /**
     * Creates a TrailersAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public TrailersAdapter(Context context, List<String> trailerList, List<String> trailerUrlList, TrailerAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.trailerList = trailerList;
        this.trailerUrlList = trailerUrlList;
        mClickHandler = clickHandler;
    }

    /**
     * Creates a TrailersAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public TrailersAdapter(Context context, Map<String,String> trailerMap, TrailerAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.trailerList = new ArrayList<>(trailerMap.values());
        this.trailerUrlList = new ArrayList<>(trailerMap.keySet());
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
    public TrailersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, viewGroup, false);
        return new TrailersViewHolder(view);
    }



    @Override
    public void onBindViewHolder(TrailersViewHolder holder, final int position) {
        /*        holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!trailerUrlList.equals("NOT AVAILABLE")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(trailerUrlList.get(position)));
                            context.startActivity(intent);
                        }
                    }
                });*/
        holder.trailerText.setText(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


    /**
     * Cache of the children views for a trailer item.
     */
    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        //@BindView(R.id.trailer_card_view)
        //CardView cardView;
        //@BindView(R.id.trailer_text_view)

        TextView trailerText;

        public TrailersViewHolder(View view) {
            super(view);
            trailerText = view.findViewById(R.id.trailer_tv);
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
            String trailerURL = trailerUrlList.get(adapterPosition);
            mClickHandler.onClick(trailerURL);
        }
    }
}