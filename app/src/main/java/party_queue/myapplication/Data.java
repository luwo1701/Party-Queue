package party_queue.myapplication;

import android.provider.Telephony;

import com.appspot.party_queue_1243.party_queue.PartyQueue;


public class Data {
    public String title;
    public String imageId;
    public Long uri;
    public PartyQueue service;

    /**
     * Constructor for Data object.
     * @param title title of the song
     * @param imageId url to the album cover art
     * @param uri Spotify URI used to access song on Spotify API
     * @param s PartyQueue service used to connect to Google App Engine Backend
     */
    Data(String title, String imageId, Long uri, PartyQueue s) {
        this.title = title;
        this.imageId = imageId;
        this.uri = uri;
        this.service = s;
    }

}