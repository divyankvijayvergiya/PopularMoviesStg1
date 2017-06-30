package application.example.com.popularmoviesstg1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import application.example.com.popularmoviesstg1.Adapter.CustomAdapter;
import application.example.com.popularmoviesstg1.Model.GridMovieItem;
import application.example.com.popularmoviesstg1.Model.Reviews;
import application.example.com.popularmoviesstg1.Model.Trailer;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieInfo extends AppCompatActivity implements CustomAdapter.CustomAdapterOnClickHandler
        ,CustomAdapter.ReviewsAdapterOnClickHandler {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
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
    @BindView(R.id.text_favourite)
    TextView tv_favorite;

    private CustomAdapter customAdapter;
    @BindView(R.id.recyclerview_trailer)
     RecyclerView mRecyclerView;
    Trailer trailers;
    ArrayList<Trailer> trailerArrayList;
    Reviews revie;
    ArrayList<Reviews> reviewsArrayList;
    ArrayList<Object> objectArrayList;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        trailerArrayList =new ArrayList<>();
        reviewsArrayList=new ArrayList<>();
        objectArrayList=new ArrayList<>();
        customAdapter=new CustomAdapter(this,trailerArrayList,reviewsArrayList,this,objectArrayList);


        mRecyclerView.setAdapter(customAdapter);
        FetchTrailerTask task=new FetchTrailerTask();
        task.execute();

        FetchReviewsTask task2=new FetchReviewsTask();
        task2.execute();






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

    @Override
    public void onListItemClick(int clickItemIndex) {
       trailers=trailerArrayList.get(clickItemIndex);

        String urlAsString="https://www.youtube.com/watch?v=";
        Uri webPage=Uri.parse(urlAsString.concat(trailers.getKey()));
        Intent intent=new Intent(Intent.ACTION_VIEW,webPage);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }

    }

    @Override
    public void onClick(int click) {
        revie=reviewsArrayList.get(click);
        Intent i=new Intent(this,Content.class);
            i.putExtra("reviews",revie);
        startActivity(i);



    }


    public class FetchTrailerTask extends AsyncTask<String,Void,ArrayList<Trailer>>{

        GridMovieItem item = getIntent().getParcelableExtra("item");
        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {

            Uri.Builder builder=new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                     .appendPath("movie")
                    .appendPath(item.getId().toString())
                    .appendPath("videos")
                    .appendQueryParameter("api_key",BuildConfig.API_KEY).build();
            URL url =null;
            try {
                url=new URL(builder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String Response=NetworkUtils.makeHttpRequest(url);
                ArrayList<Trailer> json=ExtractFromJson(Response);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }





        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerOn) {





            if(trailerOn!=null){
                customAdapter.trailerData(trailerOn);
                trailerArrayList=trailerOn;




            }
            super.onPostExecute(trailerOn);
        }

        public ArrayList<Trailer> ExtractFromJson(String trailorList){

            ArrayList<Trailer> trailersJson=new ArrayList<>();
            try {
                JSONObject root=new JSONObject(trailorList);

                JSONArray resultArray=root.getJSONArray("results");
                for(int i=0;i<resultArray.length();i++){
                    JSONObject jsonObject=resultArray.getJSONObject(i);
                    String mId=jsonObject.getString("id");
                    String name=jsonObject.getString("name");
                    String key=jsonObject.getString("key");
                    String site=jsonObject.getString("site");
                    String size=jsonObject.getString("size");
                    String type=jsonObject.getString("type");

                    Trailer trailer=new Trailer(mId,name,key,site,size,type);
                    trailersJson.add(trailer);







                }
                return trailersJson;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }


    }


    public class FetchReviewsTask extends AsyncTask<String,Void,ArrayList<Reviews>>{
        GridMovieItem item = getIntent().getParcelableExtra("item");

        @Override
        protected ArrayList<Reviews> doInBackground(String... params) {
            Uri.Builder builder=new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(item.getId().toString())
                    .appendPath("reviews")
                    .appendQueryParameter("api_key",BuildConfig.API_KEY).build();
            URL url =null;
            try {
                url=new URL(builder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                String Response=NetworkUtils.makeHttpRequest(url);
                ArrayList<Reviews> json=FetchReviewsFromJson(Response);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }



        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviewses) {
            if(reviewses!=null){
                customAdapter.reviewsData(reviewses);
                reviewsArrayList=reviewses;

            }
            super.onPostExecute(reviewses);
        }

        public ArrayList<Reviews> FetchReviewsFromJson(String reviewsList){
            ArrayList<Reviews> reviewses=new ArrayList<>();
            try {
                JSONObject root=new JSONObject(reviewsList);
                JSONArray resultArray=root.getJSONArray("results");
                for (int i=0;i<resultArray.length();i++){
                   JSONObject jsonObject=resultArray.getJSONObject(i);
                    String idReviews=jsonObject.getString("id");
                    String author=jsonObject.getString("author");
                    String content=jsonObject.getString("content");
                    String url=jsonObject.getString("url");
                    Reviews review=new Reviews(idReviews,author,content,url);
                    reviewses.add(review);

                }
                return reviewses;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
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






