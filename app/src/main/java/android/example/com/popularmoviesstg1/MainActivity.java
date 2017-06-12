package android.example.com.popularmoviesstg1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private   GridView mgridview;
    private ImageAdapter madapter;
    private List<GridMovieItem> movielist;


 private final   String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=2fa75eed778ddc192eed0f69098f7f6e",
            TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=2fa75eed778ddc192eed0f69098f7f6e";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);








            setContentView(R.layout.activity_main);




        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {

            movietask task = new movietask();
                task.execute(POPULAR_URL);



        }else
        {
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        }


    }
    private void popular(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()){

        movietask task=new movietask();
        task.execute(POPULAR_URL);
    }
        else
        {
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        }}
    private void top_rated() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()){
        movietask task = new movietask();
        task.execute(TOP_RATED_URL);}
        else
        {
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        }
    }



    private void data() {

        mgridview = (GridView) findViewById(R.id.gvMain);
        movielist = new ArrayList<>();
        madapter = new ImageAdapter(MainActivity.this, movielist);
        mgridview.setAdapter(madapter);

        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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



    private class movietask extends AsyncTask<String,Void,ArrayList<GridMovieItem>> {
        String add_info_url;
        ProgressDialog dialog=new ProgressDialog(MainActivity.this);

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

            add_info_url = params[0];

            try {
                URL url = new URL(add_info_url);
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
        protected void onPostExecute(final ArrayList<GridMovieItem> gridMovieItem) {
            dialog.dismiss();

            data();


            //adding all items of GridMovieItem Activity
            madapter.addAll(gridMovieItem);






            super.onPostExecute(gridMovieItem);
        }

                //Fetching data from Movie Url by JSON parsing.
        public ArrayList<GridMovieItem> ExtractFeatureFromJson(String movie_data_string) {

                    ArrayList<GridMovieItem>movielist=new ArrayList<>();

            try {
                JSONObject root = new JSONObject(movie_data_string);
                JSONArray result_array = root.getJSONArray("results");






                for (int i = 0; i < result_array.length(); i++) {
                    JSONObject jsonObject = result_array.getJSONObject(i);
                  String  poster_path=jsonObject.getString("poster_path");


                   String original_title = jsonObject.getString("original_title");


                   String overview = jsonObject.getString("overview");
                 String   vote_average = jsonObject.getString("vote_average");

                  String  release_date = jsonObject.getString("release_date");

                    GridMovieItem moiveItem =  new GridMovieItem(poster_path, original_title, overview, vote_average, release_date);
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
    public boolean onCreateOptionsMenu (Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        int id = item.getItemId();

        if(id==R.id.action_Popular_Movies){
                 popular();
                return true;
        }
        if(id==R.id.action_Top_Rated){
         top_rated();
                return true;
        }
        return super.onOptionsItemSelected(item);
                    }

            }



