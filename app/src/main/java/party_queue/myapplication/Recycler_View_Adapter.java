package party_queue.myapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class Recycler_View_Adapter extends RecyclerView.Adapter<Recycler_View_Adapter.ViewHolder> {

    List<Data> list = Collections.emptyList();
    Context context;


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
        holder.imageView.setImageResource(list.get(position).imageId);
        holder.song_uri.setText(list.get(position).uri);


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

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
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
            if (v.getId() == upvote.getId()){
                int list_index = v.getId();
                Toast.makeText(v.getContext(), "upvoted  " + title.getText().toString(), Toast.LENGTH_SHORT).show();

                String uri = song_uri.getText().toString(); //TODO:use song uri to talk to the backend and update queue
                list.add(new Data("new song!", R.drawable.icon_like, "new song uri is invisible"));

                Recycler_View_Adapter.this.notifyDataSetChanged();



            } else  if (v.getId() == downvote.getId()){
                Toast.makeText(v.getContext(), "downvoted " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                String uri = song_uri.getText().toString(); //TODO: use song uri to talk to the backend
            }
        }

    }

}