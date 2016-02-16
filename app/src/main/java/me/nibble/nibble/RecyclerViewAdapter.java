package me.nibble.nibble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Whit on 1/27/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>{
    private List<FeedItem> feedItems;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        FeedItem feedItem = feedItems.get(i);

        //Download image using picasso library
//        Picasso.with(mContext).load(feedItem.getThumbnail())
//                .error(R.drawable.snack)
//                .placeholder(R.drawable.snack)
//                .into(customViewHolder.imageView);

        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(feedItem.getTitle()));

        //Handle click event on both title and image click
        customViewHolder.textView.setOnClickListener(clickListener);

        customViewHolder.textView.setTag(customViewHolder);
    }

    @Override
    public int getItemCount() {
        return (null != feedItems ? feedItems.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
//        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.title);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomViewHolder holder = (CustomViewHolder) view.getTag();
            int position = holder.getPosition();

            FeedItem feedItem = feedItems.get(position);
            Toast.makeText(mContext, feedItem.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };
}
