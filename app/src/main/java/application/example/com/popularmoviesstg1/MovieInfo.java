package application.example.com.popularmoviesstg1;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import application.example.com.popularmoviesstg1.Adapter.CustomAdapter;
import application.example.com.popularmoviesstg1.Data.MovieContract;
import application.example.com.popularmoviesstg1.Model.GridMovieItem;
import application.example.com.popularmoviesstg1.Model.Reviews;
import application.example.com.popularmoviesstg1.Model.Trailer;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieInfo extends AppCompatActivity implements CustomAdapter.CustomAdapterOnClickHandler
        , CustomAdapter.ReviewsAdapterOnClickHandler {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String TAG = MovieInfo.class.getSimpleName();
    @BindView(R.id.tv_title)
    TextView originalTitle;
    @BindView(R.id.tv_overview)
    TextView overview;
    @BindView(R.id.tv_rating)
    TextView voteRating;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.iv_poster)
    ImageView posterPath;
    @BindView(R.id.imageButton_star)
    ImageButton star;
    private CustomAdapter customAdapter;
    @BindView(R.id.recyclerview_trailer)
    RecyclerView mRecyclerView;
    Trailer trailers;
    ArrayList<Trailer> trailerArrayList;
    Reviews revie;
    ArrayList<Reviews> reviewsArrayList;
    ArrayList<Object> objectArrayList;
    Cursor mData;

    private boolean isFavorite = false;
    private boolean idIdInDatabase = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        trailerArrayList = new ArrayList<>();
        reviewsArrayList = new ArrayList<>();
        objectArrayList = new ArrayList<>();
        customAdapter = new CustomAdapter(this, trailerArrayList, reviewsArrayList, this, objectArrayList);


        mRecyclerView.setAdapter(customAdapter);
        FetchTrailerTask task = new FetchTrailerTask();
        task.execute();

        FetchReviewsTask task2 = new FetchReviewsTask();
        task2.execute();
        isNetworkAvailable(getApplicationContext());
        FetchQueryOfDatabase task3 = new FetchQueryOfDatabase();
        task3.execute();
        star.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                changeFavViewColored(v);
            }
        });


//// Fetching data from a parcelable object passed from MainActivity
        GridMovieItem item = getIntent().getParcelableExtra("item");


        if (item != null) {
            Picasso.with(this).load(BASE_URL.concat(item.getPosterPath())).fit().into(posterPath);

            originalTitle.setText(item.getOriginalTitle());
            overview.setText(getString(R.string.overview).concat(item.getOverview()));
            voteRating.setText(getString(R.string.ratings).concat(item.getVoteAverage()));
            releaseDate.setText(item.getReleaseDate());


        }

    }

    @Override
    public void onListItemClick(int clickItemIndex) {
        trailers = trailerArrayList.get(clickItemIndex);

        String urlAsString = "https://www.youtube.com/watch?v=";
        Uri webPage = Uri.parse(urlAsString.concat(trailers.getKey()));
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onClick(int click) {
        revie = reviewsArrayList.get(click);
        Intent i = new Intent(this, Content.class);
        i.putExtra("reviews", revie);
        startActivity(i);


    }


    public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        GridMovieItem item = getIntent().getParcelableExtra("item");
        ProgressDialog dialog = new ProgressDialog(MovieInfo.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(item.getId().toString())
                    .appendPath("videos")
                    .appendQueryParameter("api_key", BuildConfig.API_KEY).build();
            URL url = null;
            try {
                url = new URL(builder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String Response = NetworkUtils.makeHttpRequest(url);
                ArrayList<Trailer> json = ExtractFromJson(Response);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerOn) {
            dialog.dismiss();


            if (trailerOn != null) {
                customAdapter.trailerData(trailerOn);
                trailerArrayList = trailerOn;


            }
            super.onPostExecute(trailerOn);
        }

        public ArrayList<Trailer> ExtractFromJson(String trailorList) {

            ArrayList<Trailer> trailersJson = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(trailorList);

                JSONArray resultArray = root.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    String mId = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String key = jsonObject.getString("key");
                    String site = jsonObject.getString("site");
                    String size = jsonObject.getString("size");
                    String type = jsonObject.getString("type");

                    Trailer trailer = new Trailer(mId, name, key, site, size, type);
                    trailersJson.add(trailer);


                }
                return trailersJson;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }


    }


    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Reviews>> {
        ProgressDialog dialog = new ProgressDialog(MovieInfo.this);

        GridMovieItem item = getIntent().getParcelableExtra("item");

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Reviews> doInBackground(String... params) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(item.getId().toString())
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", BuildConfig.API_KEY).build();
            URL url = null;
            try {
                url = new URL(builder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                String Response = NetworkUtils.makeHttpRequest(url);
                ArrayList<Reviews> json = FetchReviewsFromJson(Response);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviewses) {
            dialog.dismiss();
            if (reviewses != null) {
                customAdapter.reviewsData(reviewses);
                reviewsArrayList = reviewses;

            }
            super.onPostExecute(reviewses);
        }

        public ArrayList<Reviews> FetchReviewsFromJson(String reviewsList) {
            ArrayList<Reviews> reviewses = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(reviewsList);
                JSONArray resultArray = root.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    String idReviews = jsonObject.getString("id");
                    String author = jsonObject.getString("author");
                    String content = jsonObject.getString("content");
                    String url = jsonObject.getString("url");
                    Reviews review = new Reviews(idReviews, author, content, url);
                    reviewses.add(review);

                }
                return reviewses;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeFavViewColored(View v) {
        if (isFavorite) {
            borderFav();
            deleteFav();
            Toast.makeText(getApplicationContext(), "Movie Un-Favorited", Toast.LENGTH_SHORT).show();
        } else if (!isFavorite) {
            colorFav();
            makeFav();
            Toast.makeText(getApplicationContext(), "Movie Favorited", Toast.LENGTH_SHORT).show();


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void colorFav() {
        isFavorite = true;
        idIdInDatabase = true;
        star.setColorFilter(getColor(R.color.Golden));


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void borderFav() {
        isFavorite = false;
        idIdInDatabase = true;
        star.setColorFilter(getColor(R.color.Grey));


    }

    private void makeFav() {
        if (originalTitle == null || releaseDate == null ||
                voteRating == null || overview == null) {
            Log.e(TAG, "Empty TextViews");
            finish();
            return;
        }
        GridMovieItem item = getIntent().getParcelableExtra("item");
        String imageString = item.getPosterPath();

        String favoriteTitle = item.getOriginalTitle();
        String favoriteMovieId = item.getId().toString();
        String favoriteDate = item.getReleaseDate();
        String favoriteRating = item.getVoteAverage();
        String favoriteOverview = item.getOverview();


        long id = 0;
        if (favoriteMovieId != null) {
            id = Long.parseLong(favoriteMovieId);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, id);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_TITLE, favoriteTitle);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, favoriteDate);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_RATING, favoriteRating);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_OVERVIEW, favoriteOverview);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER, imageString);
        try {
            Uri newUri = getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI,
                    contentValues);
            Log.v(TAG, "Uri: " + newUri.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error performing insert task", e);
        }
    }

    private void deleteFav() {
        ContentResolver resolver = getContentResolver();
        String selection = MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
        GridMovieItem item = getIntent().getParcelableExtra("item");
        String favoriteId = item.getId().toString();
        long id = Long.parseLong(favoriteId);
        Log.v(TAG, "Movie id to b deleted");
        Uri uri = MovieContract.FavoriteEntry.builtFavoriteUri(id);

        String[] args = new String[]{
                String.valueOf(ContentUris.parseId(uri))

        };
        try {
            int rowsDeleted = resolver.delete(MovieContract.FavoriteEntry.CONTENT_URI,
                    selection,
                    args);
            if (rowsDeleted == -1) {
                Log.e(TAG, "Error deleting a row from database");

            } else {
                Log.v(TAG, "Row Deleted");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error performing delete task", e);

        }

    }

    private NetworkInfo isNetworkAvailable(Context context) {
        ConnectivityManager conn =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return conn.getActiveNetworkInfo();
    }


    private class FetchQueryOfDatabase extends AsyncTask<Void, Void, Cursor> {

        GridMovieItem item = getIntent().getParcelableExtra("item");


        @Override
        protected Cursor doInBackground(Void... params) {
            ContentResolver resolver = getContentResolver();

            String[] projection = {
                    MovieContract.FavoriteEntry.COLUMN_MOVIE_ID
            };
            String selection = MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
            Uri uri = MovieContract.FavoriteEntry.builtFavoriteUri(item.getId());
            String[] args = new String[]{
                    String.valueOf(ContentUris.parseId(uri))

            };
            Cursor cursor = null;
            try {
                cursor = resolver.query(
                        MovieContract.FavoriteEntry.CONTENT_URI,
                        projection,
                        selection,
                        args,
                        null
                );
            } catch (Exception e) {
                Log.e("Can't query database", e.toString());

            }


            return cursor;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mData = cursor;
            if (null == mData) {
                Log.e(TAG, "Cursor is not working");

            } else if (cursor.getCount() < 1) {
                borderFav();
                Log.v(TAG, "Movie ID not inside DATABASE");
            } else if (mData.moveToFirst()) {
                for (int j = 0; j < mData.getCount(); j++) {
                    if (mData.getCount() > 0) {
                        colorFav();
                    } else {
                        borderFav();
                    }
                    Log.v(TAG, "This movie is in your DATABASE.");
                }
                mData.close();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}






