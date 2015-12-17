package com.example.lukaszwieczorek.chelseanews;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {
    Context context;
    int layoutResourceId;
    News data[] = null;


    public NewsAdapter(Context context, int layoutResourceId, News[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    // not focusable when clicking
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewsHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new NewsHolder();

            holder.title = (TextView) row.findViewById(R.id.textViewNewsTitle);
            holder.description = (TextView) row.findViewById(R.id.textViewNewsDescription);
            holder.data = (TextView) row.findViewById(R.id.textViewNewsDate);

            row.setTag(holder);
        }
        else {
            holder = (NewsHolder)row.getTag();
        }

        News news = data[position];

        holder.title.setText(news.getTitle());
//        holder.description.setText(news.getDescription());
        holder.description.setText(Html.fromHtml(news.getDescription()));
        holder.data.setText(news.getDate());

        return row;
    }

    static class NewsHolder {
        TextView title;
        TextView description;
        TextView data;
    }
}
