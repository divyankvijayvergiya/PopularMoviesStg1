package application.example.com.popularmoviesstg1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieInfo extends AppCompatActivity {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    @BindView(R.id.tv_title)
    TextView originalTitle;
    @BindView(R.id.tv_overview)
    TextView overview;
    @BindView(R.id.tv_rating)
    TextView voteRating;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;

    @BindView(R.id.tv_trailer)
    TextView trailer;
    @BindView(R.id.iv_poster)
    ImageView posterPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);


//// Fetching data from a parcelable object passed from MainActivity
        GridMovieItem item = getIntent().getParcelableExtra("item");


        if (item != null) {
            Picasso.with(this).load(BASE_URL.concat(item.getPosterPath())).fit().into(posterPath);

            originalTitle.setText(getString(R.string.Original_title).concat(item.getOriginalTitle()));
            overview.setText(getString(R.string.overview).concat(item.getOverview()));
            voteRating.setText(getString(R.string.ratings).concat(item.getVoteAverage()));
            releaseDate.setText(getString(R.string.release_date).concat(item.getReleaseDate()));
        }

    }

}




