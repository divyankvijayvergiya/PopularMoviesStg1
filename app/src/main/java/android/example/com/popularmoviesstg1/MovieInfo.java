package android.example.com.popularmoviesstg1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieInfo extends AppCompatActivity {
    private static final String BASE_URL="http://image.tmdb.org/t/p/w185";

  private   TextView original_title,overview,vote_raing,release_date;
  private   ImageView poster_path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


//// Fetching data from a parcelable object passed from MainActivity
        GridMovieItem item=(GridMovieItem) getIntent().getParcelableExtra("item");


        poster_path= (ImageView) findViewById(R.id.iv_poster);

        original_title= (TextView) findViewById(R.id.tv_title);
        overview= (TextView) findViewById(R.id.tv_overview);
        vote_raing= (TextView) findViewById(R.id.tv_rating);
        release_date= (TextView) findViewById(R.id.tv_release_date);


    if (item != null) {
        Picasso.with(this).load(BASE_URL.concat(item.getPoster_path())).into(poster_path);

        original_title.setText(getString(R.string.Original_title).concat( item.getOriginal_title()));
        overview.setText( getString(R.string.overview ).concat( item.getOverview()));
        vote_raing.setText(getString(R.string.ratings) .concat( item.getVote_average()));
        release_date.setText(getString(R.string.release_date) .concat( item.getRelease_date()));
    }

}

        }




