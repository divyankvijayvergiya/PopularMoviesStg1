package application.example.com.popularmoviesstg1.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;



public class MovieContentProvider extends ContentProvider {
    public static final String LOG_TAG = MovieContentProvider.class.getSimpleName();

    private MovieDbHelper mMovieDbHelper;
    public static final int FAVORITES = 200;
    public static final int FAVORITES_ID = 201;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVORITES + "/#", FAVORITES);

        return uriMatcher;


    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES: {
                cursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null
                        , sortOrder);
                break;
            }
            case FAVORITES_ID: {
                selection = FAVORITES_ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME, projection, selection
                        , selectionArgs, null, null, sortOrder);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }


        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int matcher = sUriMatcher.match(uri);
        switch (matcher) {
            case FAVORITES:
                return MovieContract.FavoriteEntry.CONTENT_ALL_TYPE;
            case FAVORITES_ID:
                return MovieContract.FavoriteEntry.CONTENT_ITEMS_TYPE;
            default:
                throw new IllegalArgumentException(
                        "Can't retrieve database selections: "
                                + uri +
                                " according to matcher, " + matcher);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                Log.v(LOG_TAG, "Inserted row Successfully");
                return insertFavorites(uri, values);
            default:
                Log.e(LOG_TAG, "Error in inserting rows");
                throw new IllegalArgumentException("Error inserting a row");
        }


    }

    public Uri insertFavorites(Uri uri, ContentValues contentValues) {
        String movieTitle = contentValues.getAsString(MovieContract.FavoriteEntry.COLUMN_TITLE);
        if (movieTitle == null) {
            throw new IllegalArgumentException("Movie title is empty");
        }
        String movieId = contentValues.getAsString(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);
        if (movieId == null) {
            throw new IllegalArgumentException("Movie title is empty");

        }
        String movieReleaseDate = contentValues.getAsString(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE);
        if (movieReleaseDate == null) {
            throw new IllegalArgumentException("Movie title is empty");

        }
        String moviePoster = contentValues.getAsString(MovieContract.FavoriteEntry.COLUMN_POSTER);
        if (moviePoster == null) {
            throw new IllegalArgumentException("Movie title is empty");

        }
        String movieRating = contentValues.getAsString(MovieContract.FavoriteEntry.COLUMN_RATING);
        if (movieRating == null) {
            throw new IllegalArgumentException("Movie title is empty");

        }
        String movieOverview = contentValues.getAsString(MovieContract.FavoriteEntry.COLUMN_OVERVIEW);
        if (movieOverview == null) {
            throw new IllegalArgumentException("Movie title is empty");

        }
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        long id = db.insert(
                MovieContract.FavoriteEntry.TABLE_NAME,
                null,
                contentValues
        );
        if (id == -1) {
            Log.e(LOG_TAG, "error in inserting");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        Log.v(LOG_TAG, "Id inserted: " + id);
        return ContentUris.withAppendedId(uri, id);


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (matcher) {
            case FAVORITES: {
                rowsDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FAVORITES_ID: {
                selection = MovieContract.FavoriteEntry._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri :" + uri);
            }

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }


        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
