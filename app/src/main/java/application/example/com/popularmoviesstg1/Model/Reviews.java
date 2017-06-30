package application.example.com.popularmoviesstg1.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 29-06-2017.
 */

public class Reviews implements Parcelable {
    String idReviews;
    String author;
    String content;
    String url;
    public Reviews(String idReviews,String author,String content,String url){
        this.idReviews=idReviews;
        this.author=author;
        this.content=content;
        this.url=url;
    }
    public String getIdReviews() {
        return idReviews;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }


    protected Reviews(Parcel in) {
        idReviews = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idReviews);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}