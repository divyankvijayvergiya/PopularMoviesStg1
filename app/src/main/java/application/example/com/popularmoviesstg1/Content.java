package application.example.com.popularmoviesstg1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import application.example.com.popularmoviesstg1.Model.Reviews;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 29-06-2017.
 */

public class Content extends AppCompatActivity {
    @BindView(R.id.tv_reviews)
    TextView content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);



        Reviews item=getIntent().getParcelableExtra("reviews");
        if(item!=null){
            content.setText(item.getContent());
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
