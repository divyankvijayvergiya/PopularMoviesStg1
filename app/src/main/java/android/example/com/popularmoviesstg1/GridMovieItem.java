package android.example.com.popularmoviesstg1;

import android.os.Parcel;
import android.os.Parcelable;



public class GridMovieItem implements Parcelable {
    private String poster_path;
    private String original_title;


    private String overview;
    private String vote_average;
    private String release_date;


    public GridMovieItem(String mposter_path, String title, String moverview , String mvote_average , String mrelease_date) {
        poster_path = mposter_path;
        original_title = title;
        overview = moverview;
        vote_average=mvote_average;
        release_date=mrelease_date;

    }




    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }


    protected GridMovieItem(Parcel in) {
        poster_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GridMovieItem> CREATOR = new Parcelable.Creator<GridMovieItem>() {
        @Override
        public GridMovieItem createFromParcel(Parcel in) {
            return new GridMovieItem(in);
        }

        @Override
        public GridMovieItem[] newArray(int size) {
            return new GridMovieItem[size];
        }
    };
}