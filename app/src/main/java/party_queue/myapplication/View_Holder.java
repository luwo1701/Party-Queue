package party_queue.myapplication;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Queue;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by busun on 4/20/16.
 */
public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CardView cv;
    TextView title;
    ImageView imageView;
    View upvote;
    View downvote;
    TextView song_uri;
    Context context;
    List<Data> list;

    View_Holder(View itemView, List<Data> list, Context context) {

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
            Recycler_View_Adapter newadap = new Recycler_View_Adapter(list, context);



        } else  if (v.getId() == downvote.getId()){
            Toast.makeText(v.getContext(), "downvoted " + title.getText().toString(), Toast.LENGTH_SHORT).show();
            String uri = song_uri.getText().toString(); //TODO: use song uri to talk to the backend
        }
    }

}