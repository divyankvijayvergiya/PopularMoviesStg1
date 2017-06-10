package android.example.com.popularmoviesstg1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 10-06-2017.
 */

public class GridMovieItem implements Parcelable {
    private String poster_path;



    private String original_title;
    private String overview;
    private double vote_average;
    private String release_date;



    public GridMovieItem(String mposter_path, String title, String moverview , double mvote_average , String mrelease_date) {
        poster_path = mposter_path;
        original_title = title;
        overview = moverview;
        vote_average=mvote_average;
        release_date=mrelease_date;

    }

    private GridMovieItem(Parcel in) {
        poster_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public String toString(){
        return poster_path+"----"+original_title+"----"+overview+"----"+vote_average+"----"+release_date;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeDouble(vote_average);
        dest.writeString(release_date);
    }

    public static final Creator<GridMovieItem> CREATOR = new Creator<GridMovieItem>() {
        @Override
        public GridMovieItem createFromParcel(Parcel in) {
            return new GridMovieItem(in);
        }

        @Override
        public GridMovieItem[] newArray(int size) {
            return new GridMovieItem[size];
        }
    };

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
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

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

}




