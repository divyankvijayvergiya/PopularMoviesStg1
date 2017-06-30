package application.example.com.popularmoviesstg1.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dell on 30-06-2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY="application.example.com.popularmoviesstg1";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";


    public static final class FavoriteEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_ALL_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY
                + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEMS_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY
                + "/" + PATH_FAVORITES;


        public static final String TABLE_NAME="favorites";
        public static final String COLUMN_MOVIE_ID= " movie_id ";

        public static final String COLUMN_TITLE= " movie_title ";
        public static final String COLUMN_RELEASE_DATE= " movie_release_date ";
        public static final String COLUMN_POSTER= " movie_poster ";
        public static final String COLUMN_RATING= " movie_rating ";
        public static final String COLUMN_OVERVIEW= " movie_overview ";

        public static Uri builtFavoriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }







    }
}
