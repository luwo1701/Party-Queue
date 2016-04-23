package party_queue.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.ButterKnife;
import butterknife.Bind;

public class QueueActivity extends AppCompatActivity {


    List<Data> data;
    RecyclerView recyclerView;
    @Bind(R.id.searchText)
    EditText _searchText;
    Integer numResultsToShow = new Integer(10);
    Recycler_View_Adapter adapter;

    public void fill_with_data() {
        /*Data holds information for songs in the queue*/
        data = new ArrayList<>();
        data.add(new Data("Yours Truly", "http://www.flat-e.com/flate5/wp-content/uploads/cover-960x857.jpg", "test uri for Yours Truly"));
        data.add(new Data("Song 2", "http://cache.boston.com/resize/bonzai-fba/Globe_Photo/2011/04/14/1302796985_4480/539w.jpg", "test uri 2"));
        data.add(new Data("Song 3", "http://s3.amazonaws.com/hiphopdx-production/2016/01/rubble-kings-album-cover.jpg", "test uri 3"));
        data.add(new Data("Song 4",  "https://upload.wikimedia.org/wikipedia/en/8/8a/Super_Blues_(album)_cover_art.jpg", "test uri 4"));
        data.add(new Data("Song 5", "https://upload.wikimedia.org/wikipedia/en/3/33/Album_cover_art_for_jj_no_2.jpg", "test uri 5"));
        data.add(new Data("Song 6", "http://tympanikaudio.com/wp/wp-content/uploads/no_coverart.jpg", "test uri 6"));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        ButterKnife.bind(this);
        fill_with_data();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    };




    public void searchButtonClick(View v) {
        //if (v.getId() == R.id.button) {
            // do something when search button is clicked
            String search = _searchText.getText().toString(); //grabs string from _searchText text box defined in activity_spotify.xml
            //JsonObjectRequest jsObj = new JsonObjectRequest
            //jsObj= Request.Method.GET, "https://api.spotify.com/v1/search?q=tania%20bowra&type=artist"
            final Button button = (Button) v;
            ((Button) v).setText(search); // sets the text inside the button to be the text from the text box

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, "https://api.spotify.com/v1/search?q=" + search + "*&type=track&limit=10", null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // mTxtDisplay.setText("Response: " + response.toString());
                            String[] songId = new String[numResultsToShow];
                            String[] artistnames = new String[numResultsToShow];
                            String[] songName = new String[numResultsToShow];
                            try {
                                for (int i = 0; i < 10; i++) {
                                    //artist = response.getJSONArray("artists").getJSONObject(0).getString("name");
                                    songId[i] = response.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getString("id");
                                    artistnames[i] = response.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(0).getString("name");
                                    songName[i] = response.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getString("name");
                                }

                            } catch (JSONException e) {
                            }
                            //button1 = (Button) findViewById(R.id.button1);

                            //Creating the instance of PopupMenu
                            PopupMenu popup = new PopupMenu(QueueActivity.this, button);
                            //Inflating the Popup using xml file
                            popup.getMenuInflater()
                                    .inflate(R.menu.popup_menu, popup.getMenu());

                            for (int i = 0; i < numResultsToShow; i++) {
                                popup.getMenu().add("Artist: " + artistnames[i] + "\n" + "Song: " + songName[i]);
                                //popup.getMenu().getItem(i).setTitle(artistnames[i]);
                            }
                            //registering popup with OnMenuItemClickListener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    //data.add(new Data(item.getTitle().toString(), "http://www.zyekil.com/wp-content/uploads/2015/12/wolf_cover_art-1.jpg", "new song uri is invisible"));

                                    //adapter.notifyDataSetChanged();

                                    Toast.makeText(
                                            QueueActivity.this,
                                            item.getTitle() + " added to queue",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    return true;
                                }
                            });

                            popup.show(); //showing popup menu
                            //if (!img.isEmpty()) mpv.setCoverURL(img);
                            //else mpv.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");
                            //if (!artist.isEmpty()) artistView.setText(artist);
                            //if (!song.isEmpty()) songView.setText(song);
                            for (int i = 0; i < 10; i++) {
                                //artist = response.getJSONArray("artists").getJSONObject(0).getString("name");
                                Log.d("SpotifyActivity", "songs[" + i + "] = " + songId[i]);
                                Log.d("SpotifyActivity", "artist[" + i + "] = " + artistnames[i]);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d("SpotifyActivity", "ERRorlistenerdeal");

                        }
                    });
            //queue.add(jsObjRequest);


        }
    //}

}
