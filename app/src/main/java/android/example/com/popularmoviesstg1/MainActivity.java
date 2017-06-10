package android.example.com.popularmoviesstg1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    GridView mgridview;
    ImageAdapter madapter;
    ArrayList<GridMovieItem>mGriddata;


    String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=2fa75eed778ddc192eed0f69098f7f6e",
            TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=2fa75eed778ddc192eed0f69098f7f6e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






            setContentView(R.layout.activity_main);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            mGriddata=new ArrayList<GridMovieItem>();}
        else {
            mGriddata = savedInstanceState.getParcelableArrayList("movie");
        }


        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {

            movietask task = new movietask();
            task.execute(POPULAR_URL);


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie", mGriddata);
        super.onSaveInstanceState(outState);
    }
    public void data(){

        mgridview = (GridView) findViewById(R.id.gvMain);
        mGriddata = new ArrayList<>();
        madapter = new ImageAdapter(MainActivity.this,  mGriddata);
        mgridview.setAdapter(madapter);
    }


    private class movietask extends AsyncTask<String,Void,GridMovieItem> {
        String add_info_url;
        @Override
        protected GridMovieItem doInBackground(String... params) {
            if (params.length == 0) {
                return null;

            }

            add_info_url = params[0];

            try {
                URL url = new URL(add_info_url);
                String Response = NetworkUtils.makeHttpRequest(url);
                GridMovieItem json = ExtractFeatureFromJson(Response);
                return json;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG, "Error with creating URL ", e);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error with creating connection ", e);


                return null;
            }



        }

        @Override
        protected void onPostExecute(GridMovieItem gridMovieItem) {
                data();




            super.onPostExecute(gridMovieItem);
        }


        public GridMovieItem ExtractFeatureFromJson(String movie_data_string) {



            try {
                JSONObject root = new JSONObject(movie_data_string);
                JSONArray result_array = root.getJSONArray("results");
                for (int i = 0; i < result_array.length(); i++) {
                    JSONObject jsonObject = result_array.getJSONObject(i);
                    String poster_path = jsonObject.getString("poster_path");

                    String original_title = jsonObject.getString("original_title");


                    String overview = jsonObject.getString("overview");
                    double vote_average = jsonObject.getDouble("vote_average");

                    String release_date = jsonObject.getString("release_date");

                    return new GridMovieItem(poster_path, original_title, overview, vote_average, release_date);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Problem parsing the  JSON results", e);
            }
            return null;

        }
    }

    }


