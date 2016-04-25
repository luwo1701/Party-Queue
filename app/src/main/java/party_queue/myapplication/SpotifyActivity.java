package party_queue.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesAccountRequest;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesAccountResponse;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesAddSongRequest;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesMultiplePlaylistResponse;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesPlaylistRequest;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesPlaylistResponse;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesSongMessage;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.*;
import com.google.api.client.json.jackson2.*;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.mobiwise.playerview.MusicPlayerView;
import com.android.volley.toolbox.*;
import com.android.volley.*;
import com.appspot.party_queue_1243.party_queue.*;
//import com.google.api.client.json.gson;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SpotifyActivity extends AppCompatActivity implements
        PlayerNotificationCallback, ConnectionStateCallback {
    @Bind(R.id.searchText)
    EditText _searchText;

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "838ef695908c472fa60f131a52e0d149";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "my-spotify-test://callback";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;
    MusicPlayerView mpv;
    String firstTrack = null;
    String currentTrackID;
    String nextTrackID;
    Long playlistID;
    int currentSongDuration;
    int playlistSize;
    boolean playstart = false;
    boolean done;
    RequestQueue queue;
    Integer numResultsToShow = new Integer(10);

    int currentIndex;

    TextView artistView;
    TextView songView;
    ImageView nextButton;

    Long USER_ID;
    Lock lock;

    PartyQueue service;
    Thread t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USER_ID = extras.getLong("USER_ID");
        }
        done = false;
        ButterKnife.bind(this);
        Log.d("SpotifyActivity", "Started Activity");
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        currentIndex = 0;
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //trackID = "3ziCNz5vq8pEeRZjPElfYR";
        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        nextButton = (ImageView) findViewById(R.id.next);
        queue = Volley.newRequestQueue(this);
        lock = new ReentrantLock();


        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("SpotifyActivity", "Called Skip");
                if (playstart && currentIndex < playlistSize) {
                    lock.lock();
                    mPlayer.skipToNext();
                    currentTrackID = nextTrackID;
                    currentIndex = (currentIndex + 1) % playlistSize;
                    loadTrackData();
                    Toast.makeText(
                            SpotifyActivity.this,
                            "Skipped",
                            Toast.LENGTH_SHORT
                    ).show();
                    lock.unlock();
                }
                else if (playstart) {
                    Toast.makeText(
                            SpotifyActivity.this,
                            "Cannot Skip Ahead",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });



        mpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpv.isRotating()) {
                    pause();
                } else {
                    if (playstart) resume();
                    else {if(playFirstTrack()) playstart = true;} //mPlayer.play("1zHlj4dQ8ZAtrayhuDDmkY"); playstart = true;}
                }
            }
        });
        artistView = (TextView) findViewById(R.id.textViewSinger);
        songView = (TextView) findViewById(R.id.textViewSong);

        PartyQueue.Builder b = new PartyQueue.Builder(
                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        b.setApplicationName("party_queue_1243");



        service = b.build();

        t = new Thread(new myrunnable(service));
        t.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(SpotifyActivity.this);
                        mPlayer.addPlayerNotificationCallback(SpotifyActivity.this);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("SpotifyActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    public void searchButtonClick(View v)
    {

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
                        String[] trackID = new String[numResultsToShow];

                        String type ="";
                        switch(button.getId()){
                            case R.id.button:
                                type = "track";
                                try {
                                    for (int i=0; i<10; i++) {
                                        //artist = response.getJSONArray("artists").getJSONObject(0).getString("name");
                                        songId[i] = response.getJSONObject(type + "s").getJSONArray("items").getJSONObject(i).getString("id");
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
                        final PopupMenu popup = new PopupMenu(SpotifyActivity.this, button);
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
                                            SpotifyActivity.this,
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
                                                    PopupMenu popup2 = new PopupMenu(SpotifyActivity.this, button);
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

    public void loadTrackData() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://api.spotify.com/v1/tracks/"+currentTrackID, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String songname = "", artistname = "", imgurl = "";
                        int duration = 100;
                        try {
                            songname = response.getString("name");
                            artistname = response.getJSONArray("artists").getJSONObject(0).getString("name");
                            imgurl = response.getJSONObject("album").getJSONArray("images").getJSONObject(1).getString("url");
                            Log.d("SpotifyAvctivity", "Duration = " + response.getInt("duration_ms"));
                            duration = response.getInt("duration_ms");
                            duration = duration / 1000;


                        } catch (JSONException e) {
                            Log.d("SpotifyAvctivity", "Error in load data");}
                        currentSongDuration = duration;
                        mpv.setMax(currentSongDuration);
                        mpv.setProgress(0);
                        if (!imgurl.isEmpty()) mpv.setCoverURL(imgurl);
                        else mpv.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");
                        if (!artistname.isEmpty()) artistView.setText(artistname);
                        if (!songname.isEmpty()) songView.setText(songname);
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

    public boolean playFirstTrack() {
        Log.d("SpotifyActivity", "playfirsttrack");
        if (mpv.isRotating()) mpv.stop();
        if (firstTrack != null) {
            currentTrackID = firstTrack;
            loadTrackData();
            mPlayer.play("spotify:track:" + currentTrackID);
            mpv.start();
            currentIndex = (currentIndex + 1) % playlistSize;
            return true;
        }
        return false;
    }

    void pause() {
        mpv.stop();
        mPlayer.pause();
    }

    void resume() {
        mpv.start();
        mPlayer.resume();
    }

    @Override
    public void onLoggedIn() {
        Log.d("SpotifyActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("SpotifyActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("SpotifyActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("SpotifyActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("SpotifyActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("SpotifyActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            case TRACK_CHANGED:

                if (playerState.playing && (playerState.durationInMs - playerState.positionInMs) > 1) {
                    Log.d("SpotifyActivity", "Track changed, but by skipping, so don't worry");
                    Log.d("SpotifyActivity", "Position - duration (s) = "+ (playerState.positionInMs - playerState.durationInMs) / 1000);
                    //currentTrackID = playerState.trackUri;
                    //currentIndex++;
                    //loadTrackData();
                }
                else {
                    Log.d("SpotifyActivity", "Track changed, by track ending");
                    currentTrackID = nextTrackID;
                    currentIndex = (currentIndex + 1) % playlistSize;
                    loadTrackData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("SpotifyActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        done = true;
        try {
            t.join();
        } catch (InterruptedException e) {e.printStackTrace();}
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    class myrunnable implements Runnable {
        PartyQueue service;

        public myrunnable(PartyQueue s) {
            service = s;
        }

        public void run () {
            PartyQueueApiMessagesPlaylistResponse r;
            int count = 0;
            while (!done) {
                try {
                    if (count == 0) {
                        count++;
                        //PartyQueueApiMessagesPlaylistRequest p = new PartyQueueApiMessagesPlaylistRequest();
                        //p.setName("MyPlaylist");
                        //p.setUserid(USER_ID);
                        playlistID = service.partyqueue().getPlaylistsForUser().setUserid(USER_ID).execute().getPlaylists().get(0).getPid();
                        /*playlistID = service.partyqueue().createPlaylist(p).execute().getPid();
                        PartyQueueApiMessagesAddSongRequest s = new PartyQueueApiMessagesAddSongRequest();
                        s.setName("PPP");
                        s.setSpotifyId("0qQGx2TyjtZNDNyRYO9Z0W");
                        s.setPid(playlistID);
                        service.partyqueue().addSong(s).execute();
                        service.partyqueue().addSong(s).execute();*/
                    }
                    //
                    //HttpResponse h = service.partyqueue().getPlaylist().setUserid(USER_ID).setPid(playlistID).executeUsingHead();
                    r = service.partyqueue().getPlaylist().setPid(playlistID).execute();
                    playlistSize = r.getSongs().size();
                    //if (currentIndex > playlistSize) currentIndex = 0;
                    PartyQueueApiMessagesSongMessage sng = r.getSongs().get(currentIndex);
                    Log.d("SpotifyActivity", "Songs in Q:\n\t"+r.getSongs().get(0)+"\n\t"+
                            r.getSongs().get(1)+"\n\t"+r.getSongs().get(2)+"\n\t"+r.getSongs().get(3)+
                            "\n\t"+r.getSongs().get(4)+"\n\t"+r.getSongs().get(5)+"\n\t"+r.getSongs().get(6)+"\n\t"+r.getSongs().get(7));
                    //playlistID = r.getPlaylists().get(0).getPid();
                    if (!playstart) firstTrack = sng.getSpotifyId();
                    if (playstart && (currentSongDuration - mpv.getProgress() > 3)) {
                        lock.lock();
                        mPlayer.clearQueue();
                        mPlayer.queue("spotify:track:" + sng.getSpotifyId());
                        nextTrackID = sng.getSpotifyId();
                        lock.unlock();
                    }
                    Log.d("SpotifyActivity", "Just queued up " + sng.getSpotifyId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Log.d("SpotifyActivity", "Sleep Exception");
                }
            }
        }
    }

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
