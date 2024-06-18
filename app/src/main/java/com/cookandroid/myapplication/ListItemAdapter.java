package com.cookandroid.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {
    ArrayList<Memo> items = new ArrayList<Memo>();

    Context context;

    public ListItemAdapter(ArrayList<Memo> listItems) {
        this.items=listItems;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup parent) {
        context = parent.getContext();
        Memo memo = items.get(i);

        if (converView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            converView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView title = converView.findViewById(R.id.title);
        TextView date = converView.findViewById(R.id.date);

        if(memo.getIsabled()){
            title.setTextColor(Color.BLACK);
            date.setTextColor(Color.BLACK);
        }
        else{
            title.setTextColor(Color.LTGRAY);
            date.setTextColor(Color.LTGRAY);
        }
        if (memo.getIsfind()) {
            title.setBackgroundColor(Color.YELLOW);
            date.setBackgroundColor(Color.YELLOW);
        } else {
            title.setBackgroundColor(Color.TRANSPARENT); // 투명으로 설정
            date.setBackgroundColor(Color.TRANSPARENT);
        }

        title.setText(memo.getTitle());
        date.setText(memo.getDate());

        return converView;

    }

    public void addItem(Memo memo){
        items.add(memo);
        notifyDataSetChanged();
    }

}

