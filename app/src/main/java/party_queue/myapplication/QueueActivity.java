package party_queue.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class QueueActivity extends AppCompatActivity {


    List<Data> data;
    RecyclerView recyclerView;

    public void fill_with_data() {
        /*Data holds information for songs in the queue*/
        data = new ArrayList<>();
        data.add(new Data("Yours Truly", R.drawable.mycover, "test uri for Yours Truly"));
        data.add(new Data("Song 2", R.drawable.ic_info_black_24dp, "test uri 2"));
        data.add(new Data("Song 3", R.drawable.ic_info_black_24dp, "test uri 3"));
        data.add(new Data("Song 4",  R.drawable.ic_info_black_24dp, "test uri 4"));
        data.add(new Data("Song 5", R.drawable.ic_info_black_24dp, "test uri 5"));
        data.add(new Data("Song 6", R.drawable.ic_info_black_24dp, "test uri 6"));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        fill_with_data();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    };

    public void updateAll(Recycler_View_Adapter newadap){
        recyclerView.setAdapter(newadap);
    }


}
