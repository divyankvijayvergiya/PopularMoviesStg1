package application.example.com.popularmoviesstg1.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class GridMovieItem implements Parcelable {
    private String posterPath;
    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;



    private Long id;


    public GridMovieItem(String posterPath,Long id, String originalTitle, String overview, String voteAverage, String releaseDate) {
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.id=id;

    }
    public Long getId() {
        return id;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    protected GridMovieItem(Parcel in) {
        posterPath = in.readString();
        id=in.readLong();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeLong(id);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
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