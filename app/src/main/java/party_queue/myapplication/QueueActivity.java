package party_queue.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class QueueActivity extends AppCompatActivity {


    List<Data> data;

    public void fill_with_data() {
        data = new ArrayList<>();
        data.add(new Data("Yours Truly", R.drawable.mycover));
        data.add(new Data("Song 2", R.drawable.ic_info_black_24dp));
        data.add(new Data("Song 3",  R.drawable.ic_info_black_24dp));
        data.add(new Data("Song 4",  R.drawable.ic_info_black_24dp));
        data.add(new Data("Song 5",  R.drawable.ic_info_black_24dp));
        data.add(new Data("Song 6",  R.drawable.ic_info_black_24dp));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        fill_with_data();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    };


}
