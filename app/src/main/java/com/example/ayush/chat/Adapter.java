package com.example.ayush.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ayush on 10/27/2015.
 */
public class Adapter extends BaseAdapter {
    private Context context;
    static ArrayList<String> chatmessages;

    Adapter(Context context){
        this.context = context;
        chatmessages = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return chatmessages.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.childrow,parent,false);
        TextView message = (TextView) convertView.findViewById(R.id.messgeview);
        message.setText(chatmessages.get(position));
        return convertView;
    }
}
