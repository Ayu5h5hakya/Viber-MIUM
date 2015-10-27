package com.example.ayush.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ayush on 10/27/2015.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context context;
    static ArrayList<String> chatmessages;

    Adapter(Context context){
        this.context = context;
        chatmessages = new ArrayList<>();
    }

    Adapter(Context context,ArrayList<String> previousmessages){
        this.context = context;
        chatmessages = previousmessages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.childrow,parent,false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.message.setText(chatmessages.get(position));
    }


    @Override
    public int getItemCount() {
        return chatmessages.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView message;

        public MyViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.messgeview);

        }
    }
}
