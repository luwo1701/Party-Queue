package party_queue.myapplication;

import android.provider.Telephony;

import com.appspot.party_queue_1243.party_queue.PartyQueue;

/**
 * Created by busun on 4/20/16.
 */
public class Data {
    public String title;
    public String imageId;
    public Long uri;
    public PartyQueue service;

    Data(String title, String imageId, Long uri, PartyQueue s) {
        this.title = title;
        this.imageId = imageId;
        this.uri = uri;
        this.service = s;
    }

}