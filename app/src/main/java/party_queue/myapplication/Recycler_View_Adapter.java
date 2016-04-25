package party_queue.myapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.party_queue_1243.party_queue.PartyQueue;
import com.appspot.party_queue_1243.party_queue.model.PartyQueueApiMessagesVoteSongRequest;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class Recycler_View_Adapter extends RecyclerView.Adapter<Recycler_View_Adapter.ViewHolder> {

    List<Data> list = Collections.emptyList();
    Context context;

    /**
     * Constructor for the recycler view adapter.
     * @param list the list of songs
     * @param context the current context
     */
    public Recycler_View_Adapter(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder holder = new ViewHolder(v, list, context);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).title);

        Picasso.with(context).load(list.get(position).imageId).into(holder.imageView);

        holder.song_uri.setText(list.get(position).uri.toString());


    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     *  Insert a new item to the RecyclerView on a predefined position
     * @param position integer position in list
     * @param data the new song data
     */
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * Remove a RecyclerView item containing a specified Data object.
     * @param data data object to be removed
     */
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView title;
        ImageView imageView;
        View upvote;
        View downvote;
        TextView song_uri;
        Context context;
        List<Data> list;

        public ViewHolder(View itemView, List<Data> list, Context context) {

            super(itemView);

            this.context = context;
            this.list = list;



            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            upvote = itemView.findViewById(R.id.upvote);
            downvote = itemView.findViewById(R.id.downvote);
            song_uri = (TextView) itemView.findViewById(R.id.song_uri);

            upvote.setOnClickListener(this);
            downvote.setOnClickListener(this);

        }

        @Override
        public void onClick(View v){
            Log.d("Recycler", "OnClick");
            if (v.getId() == upvote.getId()){
                int list_index = v.getId();

                Toast.makeText(v.getContext(), "upvoted  " + title.getText().toString(), Toast.LENGTH_SHORT).show();

               /* String uri = song_uri.getText().toString(); //TODO:use song uri to talk to the backend and update queue
                Log.d("Recycler", "B");
                Thread t1 = new Thread(new vote(list.get(list_index).service, list.get(list_index).uri, 1));
                t1.start();*/
                //list.add(new Data("new song!", "http://www.zyekil.com/wp-content/uploads/2015/12/wolf_cover_art-1.jpg", "new song uri is invisible"));

                //Recycler_View_Adapter.this.notifyDataSetChanged();



            } else  if (v.getId() == downvote.getId()){
                int list_index = v.getId();
                /*Thread t1 = new Thread(new vote(list.get(list_index).service, list.get(list_index).uri, -1));
                t1.start();*/
                Toast.makeText(v.getContext(), "downvoted " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                String uri = song_uri.getText().toString(); //TODO: use song uri to talk to the backend
            }
        }

    }

    public class vote implements Runnable {
        int vote;
        Long songID;
        PartyQueue service;

        /**
         * vote on a song
         * @param s the backend service
         * @param i the song id
         * @param v the vote count
         */
        public vote(PartyQueue s, Long i, int v) {
            service = s;
            songID = i;
            vote = v;
        }

        public void run(){
            try {
                PartyQueueApiMessagesVoteSongRequest s = new PartyQueueApiMessagesVoteSongRequest().setId(songID);
                if (vote > 0) service.partyqueue().upvote(s).execute();
                else service.partyqueue().downvote(s).execute();
            } catch (IOException e) {e.printStackTrace();}
        }
    }

}