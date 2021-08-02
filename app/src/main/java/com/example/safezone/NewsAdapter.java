package com.example.safezone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.safezone.NewsWebsite.NEWS_CARD_STRING;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private ArrayList<NewsItem> newsItem = new ArrayList();
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(newsItem.get(position).getTitle());
        holder.description.setText(newsItem.get(position).getDescription());
        holder.date.setText(newsItem.get(position).getDate());

        holder.newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 26/07/2021 navigate the user to web activity
                Intent intent = new Intent(context,NewsWebsite.class);
                intent.putExtra(NEWS_CARD_STRING,newsItem.get(position).getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItem.size();
    }

    public void setNewsItem(ArrayList<NewsItem> newsItem) {
        this.newsItem = newsItem;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title, description, date;
        private CardView newsCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.newsCard_title);
            description = itemView.findViewById(R.id.newsCard_Content);
            date = itemView.findViewById(R.id.newsCard_pubdate);
            newsCard = itemView.findViewById(R.id.newsCardId);
        }

    }
}
