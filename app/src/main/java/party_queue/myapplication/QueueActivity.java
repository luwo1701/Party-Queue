package party_queue.myapplication;

import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.toolbox.*;
import com.android.volley.*;
import com.appspot.party_queue_1243.party_queue.PartyQueue;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesAddSongRequest;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesPlaylistResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Implements the queue.
 */

public class QueueActivity extends AppCompatActivity {


    List<Data> data;
    RecyclerView recyclerView;

    @Bind(R.id.searchText)
    EditText _searchText;

    RequestQueue queue;

    Integer numResultsToShow = new Integer(10);
    Recycler_View_Adapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    boolean init;
    Long playlistID;
    String usertarget;
    PartyQueue service;

    String tID;
    Long URI;

    /*public void fill_with_data() {
        *//*Data holds information for songs in the queue*//*
        data = new ArrayList<>();
        data.add(new Data("Yours Truly", "http://www.flat-e.com/flate5/wp-content/uploads/cover-960x857.jpg", "test uri for Yours Truly"));
        data.add(new Data("Song 2", "http://cache.boston.com/resize/bonzai-fba/Globe_Photo/2011/04/14/1302796985_4480/539w.jpg", "test uri 2"));
        data.add(new Data("Song 3", "http://s3.amazonaws.com/hiphopdx-production/2016/01/rubble-kings-album-cover.jpg", "test uri 3"));
        data.add(new Data("Song 4",  "https://upload.wikimedia.org/wikipedia/en/8/8a/Super_Blues_(album)_cover_art.jpg", "test uri 4"));
        data.add(new Data("Song 5", "https://upload.wikimedia.org/wikipedia/en/3/33/Album_cover_art_for_jj_no_2.jpg", "test uri 5"));
        data.add(new Data("Song 6", "http://tympanikaudio.com/wp/wp-content/uploads/no_coverart.jpg", "test uri 6"));


    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        Log.d("MenuActivity", "Menu Activity");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usertarget = extras.getString("username");
            Log.d("MenuActivity", "got username");
        }
        PartyQueue.Builder b = new PartyQueue.Builder(
                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        b.setApplicationName("party_queue_1243");

        service = b.build();
        queue = Volley.newRequestQueue(this);

        init = false;
        Thread t1 = new Thread(new fillData(service));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e){e.printStackTrace();}

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ButterKnife.bind(this);



        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }

    PartyQueue getService() {
        return service;
    }

    /**
     *Refresh the view by reloading items from the backend.
     */
    void refreshItems() {
        // Load items
        // ...
        Log.d("MenuActivity", "RefreshItems");
        Thread t1 = new Thread(new fillData(service));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {e.printStackTrace();}

        // Load complete
        onItemsLoadComplete();
    }

    /**
     * Notify frontend that changes have been completed and reload layout.
     */
    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        Log.d("MenuActivity", "LoadComplete");
        adapter.notifyDataSetChanged();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * Listens for clicks on the search by user button.
     * @param v the current view in the activity
     */
    public void searchButtonClick(View v)
    {
        Log.d("MenuActivity", "SearchButtonClick");
        // do something when search button is clicked
        String search = _searchText.getText().toString().replaceAll(" ","+"); //grabs string from _searchText text box defined in activity_spotify.xml

        //JsonObjectRequest jsObj = new JsonObjectRequest
        //jsObj= Request.Method.GET, "https://api.spotify.com/v1/search?q=tania%20bowra&type=artist"
        final Button button = (Button) v;
        //( (Button) v).setText(search); // sets the text inside the button to be the text from the text box
        String type ="";
        switch(button.getId()){
            case R.id.button:
                type = "track";
                break;
            case R.id.button2:
                type = "artist";
                break;

        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://api.spotify.com/v1/search?q="+search+"*&type="+ type +"&limit="+numResultsToShow , null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // mTxtDisplay.setText("Response: " + response.toString();
                        final String[] songId = new String[numResultsToShow];
                        final String[] artistnames = new String[numResultsToShow];
                        final String[] songName = new String[numResultsToShow];
                        final String[] artistId = new String[numResultsToShow];

                        String type ="";
                        switch(button.getId()){
                            case R.id.button:
                                type = "track";
                                try {
                                    for (int i=0; i<10; i++) {
                                        //artist = response.getJSONArray("artists").getJSONObject(0).getString("name");
                                        songId[i] = response.getJSONObject(type+"s").getJSONArray("items").getJSONObject(i).getString("id");
                                        artistnames[i] = response.getJSONObject(type+"s").getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(0).getString("name");
                                        songName[i] = response.getJSONObject(type+"s").getJSONArray("items").getJSONObject(i).getString("name");
                                    }

                                } catch (JSONException e) {}
                                break;
                            case R.id.button2:
                                type = "artist";
                                try {
                                    for (int i=0; i<10; i++) {
                                        //artist = response.getJSONArray("artists").getJSONObject(0).getString("name");
                                        artistId[i] = response.getJSONObject(type+"s").getJSONArray("items").getJSONObject(i).getString("id");
                                        artistnames[i] = response.getJSONObject(type + "s").getJSONArray("items").getJSONObject(i).getString("name");
                                        //songName[i] = response.getJSONObject(type+"s").getJSONArray("items").getJSONObject(i).getString("name");
                                    }

                                } catch (JSONException e) {}
                                break;


                        }

                        //button1 = (Button) findViewById(R.id.button1);

                        //Creating the instance of PopupMenu
                        final PopupMenu popup = new PopupMenu(QueueActivity.this, button);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate(R.menu.popup_menu, popup.getMenu());

                        for (int i = 0; i<numResultsToShow; i++) {
                            if (button.getId() == R.id.button) {
                                //popup.getMenu().add("Artist: " + artistnames[i] + "\n" + "Song: " + songName[i]);
                                popup.getMenu().add(i,i,i,artistnames[i]);
                            }
                            else {
                                popup.getMenu().add(i,i,i,"Artist: " + artistnames[i]);
                            }
                        }
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                final String title;
                                if (button.getId() == R.id.button) {
                                    title = songName[0];
                                    Thread t2 = new Thread(new addSongRunnable(service, songId[item.getItemId()], songName[item.getItemId()]));
                                    t2.start();
                                    Toast.makeText(
                                            QueueActivity.this,
                                            //gets track name and prints it to page and alerts user
                                            item.getTitle().toString() + " added to queue" +"item number is "+songId[item.getItemId()],
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                                //if song, add to q and set title = songname
                                else {

                                    String id =  artistId[item.getItemId()];
                                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                            (Request.Method.GET, "https://api.spotify.com/v1/artists/"+id+"/top-tracks?country=US" , null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    final String[] songIds = new String[numResultsToShow];
                                                    final String[] artistname = new String[numResultsToShow];
                                                    final String[] songNames = new String[numResultsToShow];
                                                    String type = "";
                                                    //switch(button.getId()){
                                                    //  case R.id.button:
                                                    //type = "track";
                                                    try {
                                                        for (int i = 0; i < 10; i++) {

                                                            songNames[i] = response.getJSONArray("tracks").getJSONObject(i).getString("name");
                                                            Log.d("SpotifyActivity", "songs[" + i +"] = " + songNames[i]);
                                                            // artistname[i] = response.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(0).getString("name");
                                                            //songIds[i] = response.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getString("name");
                                                        }
                                                        //;

                                                    } catch (JSONException e) {}
                                                    //Creating the instance of PopupMenu
                                                    PopupMenu popup2 = new PopupMenu(QueueActivity.this, button);
                                                    //Inflating the Popup using xml file
                                                    popup2.getMenuInflater()
                                                            .inflate(R.menu.popup_menu, popup2.getMenu());

                                                    for (int i = 0; i<numResultsToShow; i++) {

                                                        popup2.getMenu().add(i,i,i,"Song: " + songNames[i]);
                                                    }
                                                    popup2.show(); //showing new popup menu




                                                }
                                            }, new Response.ErrorListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // TODO Auto-generated method stub
                                                    Log.d("SpotifyActivity", "ERRorlistenerdeal2");

                                                }
                                            });
                                    queue.add(jsObjRequest);
                                }

                                return true;
                            }
                        });

                        popup.show(); //showing popup menu
                        //if (!img.isEmpty()) mpv.setCoverURL(img);
                        //else mpv.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");
                        //if (!artist.isEmpty()) artistView.setText(artist);
                        //if (!song.isEmpty()) songView.setText(song);
                        for (int i=0; i<10; i++) {
                            //artist = response.getJSONArray("artists").getJSONObject(0).getString("name");
                            Log.d("SpotifyActivity", "songs[" + i +"] = " + songId[i]);
                            Log.d("SpotifyActivity", "artist[" + i +"] = " + artistnames[i]);

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("SpotifyActivity", "ERRorlistenerdeal");

                    }
                });
        queue.add(jsObjRequest);

    }
    /*public interface VolleyCallback{
        void onSuccess(JSONObject string);
    }*/

    /**
     * Fill the activity with data by connecting to the backend.
     */
    public class fillData implements Runnable {
        PartyQueue service;
        PartyQueueApiMessagesPlaylistResponse r;
        int playlistSize;
        public fillData(PartyQueue s) {
            service = s;
        }
        public void run() {
            Log.d("MenuActivity", "RUN");
            if (!init){
                try {
                    playlistID = service.partyqueue().getPlaylistsForUserByName().setUsername(usertarget).execute().getPlaylists().get(0).getPid();
                    init = true;
                } catch (IOException e) {e.printStackTrace();}
            }
            if (playlistID != null) {
                try {
                    data = new ArrayList<>();
                    r = service.partyqueue().getPlaylist().setPid(playlistID).execute();
                    playlistSize = r.getSongs().size();
                    Log.d("MenuActivity", "playlist size = "+playlistSize);
                    for (int i=0; i<playlistSize; i++){
                        tID = r.getSongs().get(i).getSpotifyId();
                        URI = r.getSongs().get(i).getId();
                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, "https://api.spotify.com/v1/tracks/"+tID, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String songname = "", artistname = "", imgurl = "";
                                    try {
                                        songname = response.getString("name");
                                        artistname = response.getJSONArray("artists").getJSONObject(0).getString("name");
                                        imgurl = response.getJSONObject("album").getJSONArray("images").getJSONObject(1).getString("url");
                                        data.add(new Data(songname, imgurl, URI, service));
                                        Log.d("QActivity", "Success");
                                    } catch (JSONException e) {
                                        Log.d("QActivity", "Error in load data");}
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    Log.d("QActivity", "ERRorlistenerdeal");
                                }
                            });
                        queue.add(jsObjRequest);
                    }
                } catch (IOException e) {e.printStackTrace();}
            }
        }
    }
    /**
     * Add a song from the backend.
     */
    class addSongRunnable implements Runnable {
        PartyQueue service;
        String tID;
        String name;

        public addSongRunnable(PartyQueue s, String id, String n) {
            service = s;
            tID = id;
            name = n;
        }

        public void run () {
            PartyQueueApiMessagesAddSongRequest r = new PartyQueueApiMessagesAddSongRequest();
            r.setPid(playlistID);
            r.setName(name);
            r.setSpotifyId(tID);

            try {
                service.partyqueue().addSong(r).execute();
            } catch (IOException e) {e.printStackTrace(); Log.d("SpotifyActivity", "Error Adding a Song");}


        }
    }

}
