package application.example.com.popularmoviesstg1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import application.example.com.popularmoviesstg1.Adapter.ImageAdapter;
import application.example.com.popularmoviesstg1.Data.MovieContract;
import application.example.com.popularmoviesstg1.Model.GridMovieItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private ImageAdapter mAdapter;
    private ArrayList<GridMovieItem> movieList;
    private static final int LOADER_ID = 2;
    private static final int Fav_LOADER_ID = 3;



    private final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=",
            TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
 public static final String API_KEY = BuildConfig.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {

            MovieTask task = new MovieTask();
            task.execute(POPULAR_URL.concat(API_KEY));


        } else {
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        }


    }

    private void popular() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {

            MovieTask task = new MovieTask();
            task.execute(POPULAR_URL.concat(API_KEY));
        } else {
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        }
    }

    private void top_rated() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            MovieTask task = new MovieTask();
            task.execute(TOP_RATED_URL.concat(API_KEY));
        } else {
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        }
    }

       private  LoaderManager.LoaderCallbacks<Cursor> favoritesLoader=new LoaderManager.LoaderCallbacks<Cursor>(){

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new AsyncTaskLoader<Cursor>(getApplicationContext()) {
                    Cursor fav=null;

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public Cursor loadInBackground() {
                        try{
                            return getContentResolver().query(
                                    MovieContract.FavoriteEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    MovieContract.FavoriteEntry._ID
                            );
                        }catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }


                    }

                    @Override
                    public void deliverResult(Cursor data) {
                        fav=data;
                        super.deliverResult(data);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

                mAdapter.setData(null);

                if(loader.getId()==Fav_LOADER_ID){
                    if(data.getCount()<1){
                        Log.e(TAG,"Well no matches ");
                    }else {
                        while (data.moveToFirst()){
                            int movieId=data.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);
                            int movieTitle=data.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_TITLE);
                            int moviePoster=data.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_POSTER);
                            int movieOverview=data.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_OVERVIEW);
                            int movieRating=data.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_RATING);
                            int movieDate=data.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE);


                            long id=data.getLong(movieId);
                            String title=data.getString(movieTitle);
                            String poster=data.getString(moviePoster);
                            String overview=data.getString(movieOverview);
                            String rating=data.getString(movieRating);
                            String date=data.getString(movieDate);

                            movieList.add(new GridMovieItem(poster,id,title,overview,rating,date));
                        }
                        mAdapter.setData(movieList);
                        Log.v(TAG, "Favorites List have data");

                    }

                }else {
                    mAdapter.clearMoviePosterData();
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.clearMoviePosterData();
                if(loader!=null){
                    mAdapter.clearMoviePosterData();
                }
                else {
                    mAdapter.setData(null);
                }


            }
        };

        private void favoriteLoader(){
            getSupportLoaderManager().initLoader(Fav_LOADER_ID,null,null);
        }




    private void data() {

        mGridView = (GridView) findViewById(R.id.gvMain);
        movieList = new ArrayList<>();
        mAdapter = new ImageAdapter(MainActivity.this, movieList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //// Creating an instance of GridMovieItem class with movie data
                GridMovieItem item = (GridMovieItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, MovieInfo.class);
                //passing data to MovieInfo.class
                intent.putExtra("item", item);
                //start MovieInfo.class
                startActivity(intent);


            }
        });


    }



    private final class MovieTask extends AsyncTask<String, Void, ArrayList<GridMovieItem>> {
        String addInfoUrl;
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.show();

        }

        @Override
        protected ArrayList<GridMovieItem> doInBackground(String... params) {
            if (params.length == 0) {
                return null;

            }

            addInfoUrl = params[0];

            try {
                URL url = new URL(addInfoUrl);
                String Response = NetworkUtils.makeHttpRequest(url);
                ArrayList<GridMovieItem> json = ExtractFeatureFromJson(Response);
                return json;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "Error with creating URL ", e);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Error with creating connection ", e);


                return null;
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<GridMovieItem> gridMovieItems) {
            dialog.dismiss();

            data();

            //adding all items of GridMovieItem Activity
            mAdapter.addAll(gridMovieItems);


            super.onPostExecute(gridMovieItems);
        }

        //Fetching data from Movie Url by JSON parsing.
        public ArrayList<GridMovieItem> ExtractFeatureFromJson(String movie_data_string) {

            ArrayList<GridMovieItem> movielist = new ArrayList<>();

            try {
                JSONObject root = new JSONObject(movie_data_string);
                JSONArray result_array = root.getJSONArray("results");


                for (int i = 0; i < result_array.length(); i++) {
                    JSONObject jsonObject = result_array.getJSONObject(i);
                    String poster_path = jsonObject.getString("poster_path");
                    Long id=jsonObject.getLong("id");


                    String original_title = jsonObject.getString("original_title");


                    String overview = jsonObject.getString("overview");
                    String vote_average = jsonObject.getString("vote_average");

                    String release_date = jsonObject.getString("release_date");

                    GridMovieItem moiveItem = new GridMovieItem(poster_path,id, original_title, overview, vote_average, release_date);
                    movielist.add(moiveItem);
                }
                return movielist;


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Problem parsing the  JSON results", e);
            }
            return null;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_Popular_Movies) {
            popular();
            return true;
        }
        if (id == R.id.action_Top_Rated) {
            top_rated();
            return true;
        }
        if(id==R.id.action_Top_Rated){
            favoriteLoader();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



