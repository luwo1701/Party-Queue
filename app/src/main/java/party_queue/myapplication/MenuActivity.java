package party_queue.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    @Bind(R.id.search_button)
    Button _partyguest;
    @Bind(R.id.btn_host)
    Button _partyhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        _partyhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host();
            }
        });
       _partyguest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guest();
            }
        });

    }

    public void host(){
        Intent intent = new Intent(this, SpotifyActivity.class);
        startActivity(intent);
        //finish();
    }

    public void guest(){
        Intent intent = new Intent(this, QueueActivity.class);
        startActivity(intent);
        //finish();
    }
}
