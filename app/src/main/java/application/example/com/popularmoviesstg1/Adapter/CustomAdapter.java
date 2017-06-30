package application.example.com.popularmoviesstg1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import application.example.com.popularmoviesstg1.R;
import application.example.com.popularmoviesstg1.Model.Reviews;
import application.example.com.popularmoviesstg1.Model.Trailer;

/**
 * Created by Dell on 26-06-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String YOUTUBE_IMG_URL = "https://img.youtube.com/vi/";
    String finalUrl =  "/0.jpg";
    private static final String TAG = CustomAdapter.class.getSimpleName();
    public final int REVIEWS_ID = 1;
    public final int TRAILOR_ID = 0;
    Context context;

    private ArrayList<Object> objects;
    private ArrayList<Trailer> trailer;
    private ArrayList<Reviews> rev;
    public CustomAdapterOnClickHandler mOnClickListener;
    public ReviewsAdapterOnClickHandler mOnClick;


    public interface CustomAdapterOnClickHandler {
        void onListItemClick(int clickItemIndex);
    }

    public interface ReviewsAdapterOnClickHandler {
        void onClick(int click);
    }


    public CustomAdapter(CustomAdapterOnClickHandler mOnClickListener, ArrayList<Trailer> trailer
            , ArrayList<Reviews> rev, ReviewsAdapterOnClickHandler onClick, ArrayList<Object> objects) {

        this.objects = objects;
        this.trailer = trailer;
        this.mOnClickListener = mOnClickListener;
        this.rev = rev;
        this.mOnClick = onClick;


    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tv_trailer;
        public final ImageView icon;


        public TrailerViewHolder(View itemView) {
            super(itemView);
            tv_trailer = (TextView) itemView.findViewById(R.id.trailer_name_view);
            icon = (ImageView) itemView.findViewById(R.id.iv);


            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tv_author;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition() - trailer.size();
            mOnClick.onClick(clickedPosition);


        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position < trailer.size()) {
            return TRAILOR_ID;
        } else {
            return REVIEWS_ID;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        switch (viewType) {
            case TRAILOR_ID: {
                int layoutForTrailer = R.layout.activity_trailer;
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                boolean shouldAttachToParentImmediately = false;
                View view = layoutInflater.inflate(layoutForTrailer, parent, shouldAttachToParentImmediately);
                return new TrailerViewHolder(view);

            }
            case REVIEWS_ID: {
                int layoutForReviews = R.layout.activity_reviews;
                LayoutInflater layoutInflator = LayoutInflater.from(context);
                View view = layoutInflator.inflate(layoutForReviews, parent, false);
                return new ReviewsViewHolder(view);

            }


        }

        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case TRAILOR_ID:
                Trailer trailorList = trailer.get(position);

                ((TrailerViewHolder) holder).tv_trailer.setText(trailorList.getType());
                Picasso.with(context).load(YOUTUBE_IMG_URL.concat(trailorList.getKey()).concat(finalUrl)).fit().into(((TrailerViewHolder) holder).icon);

                break;
            case REVIEWS_ID:
                Reviews reviewList = rev.get(position - trailer.size());
                ((ReviewsViewHolder) holder).tv_author.setText("Click for Review: ".concat(reviewList.getAuthor()));

                break;
            default:
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                break;


        }


    }


    @Override
    public int getItemCount() {


        return trailer.size() + rev.size();
    }

    public void trailerData(ArrayList<Trailer> trailorss) {
        trailer = trailorss;
        notifyDataSetChanged();
    }

    public void reviewsData(ArrayList<Reviews> reviewss) {
        rev = reviewss;
        notifyDataSetChanged();
    }


}
