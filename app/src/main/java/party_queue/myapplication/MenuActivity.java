package party_queue.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    @Bind(R.id.search_button)
    Button _partyguest;
    @Bind(R.id.btn_host)
    Button _partyhost;
    @Bind(R.id.searchText)
    EditText _searchText;
    Long USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USER_ID = extras.getLong("USER_ID");
            Log.d("MenuActivity", "got user id");
        }
        _partyhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host();
            }
        });
       _partyguest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guest(_searchText.getText().toString());
            }
        });

    }

    /**
     * If the "host" button is clicked, send the host ot the SpotifyActivity to add songs and manage song play.
     */
    public void host(){
        Intent intent = new Intent(this, SpotifyActivity.class);
        intent.putExtra("USER_ID", USER_ID);
        startActivity(intent);
        //finish();
    }

    /**
     * If the user searched for a queue by username, send them to the party associated with that username
     * @param username the username of the party that was searched for
     */
    public void guest(String username){
        Intent intent = new Intent(this, QueueActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        //finish();
    }
}
