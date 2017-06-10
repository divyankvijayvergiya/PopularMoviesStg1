package android.example.com.popularmoviesstg1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 10-06-2017.
 */

public class ImageAdapter extends ArrayAdapter<GridMovieItem> {
    public static final String LOG_TAG=ImageAdapter.class.getSimpleName();
    public static final String BASE_URL="http://image.tmdb.org/t/p/w185";
    private int layoutResourceId;




    private ArrayList<GridMovieItem> movielist=new ArrayList<GridMovieItem>();


    public ImageAdapter(Activity context, List<GridMovieItem> movieItems) {
        super(context, 0,movieItems);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String original_title=movielist.get(position).getOriginal_title();
        String poster_path=movielist.get(position).getPoster_path();
        String url=BASE_URL+poster_path;
        ViewHolder holder;


        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_griditem, parent, false);

        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder=new ViewHolder();
        holder.tv_title= (TextView) convertView.findViewById(R.id.grid_item_title);
        holder.imageView_item= (ImageView) convertView.findViewById(R.id.item_image);
        convertView.setTag(holder);
        holder. tv_title.setText(original_title);



        Picasso.with(getContext()).load(url).into(holder.imageView_item);



        return convertView;
    }

    static class ViewHolder {
        TextView tv_title;
        ImageView imageView_item;
    }




}


