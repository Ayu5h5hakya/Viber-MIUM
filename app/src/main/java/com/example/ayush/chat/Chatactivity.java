package com.example.ayush.chat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ayush on 10/27/2015.
 */
public class Chatactivity extends AppCompatActivity {
    RecyclerView chathistory;
    EditText message;
    Button sendbutton;
    ConnectionDaemon connectionDaemon;
    private String message2send = "";
    Adapter chat_adapter;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        chathistory = (RecyclerView) findViewById(R.id.chathistory);
        message = (EditText) findViewById(R.id.newmessage);
        sendbutton = (Button) findViewById(R.id.send);
        calendar = Calendar.getInstance();
        final MediaPlayer mediaPlayer =MediaPlayer.create(this,R.raw.blop);

        Intent intent = getIntent();
        final String username = intent.getStringExtra(MainActivity.USER_NAME_TAG);
        if(Chat_Sugar.count(Chat_Sugar.class,"des = ?",new String[]{"qwerty"})!=0)
        {
            List<Chat_Sugar> previous = Chat_Sugar.find(Chat_Sugar.class,"des = ?","qwerty");
            ArrayList<String> messages = new ArrayList<>();
            for(Chat_Sugar temp : previous)
            {
                messages.add(temp.src + " : " + temp.message);
            }
            chat_adapter = new Adapter(this,messages);

        }
        else
        {
            chat_adapter = new Adapter(this);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chathistory.setLayoutManager(linearLayoutManager);
        chathistory.setAdapter(chat_adapter);

        connectionDaemon = new ConnectionDaemon();
        connectionDaemon.execute();
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message2send = message.getText().toString();
                if (!message2send.isEmpty()) {
                    Chat_Sugar record = new Chat_Sugar(username,"qwerty",message2send,calendar.getTime());
                    record.save();
                    Adapter.chatmessages.add(username + " : " + message2send);
                    chat_adapter.notifyDataSetChanged();
                    MainActivity.socket.emit("message", message2send);
                    message.setText("");
                    mediaPlayer.start();
                }
            }
        });
    }

    class ConnectionDaemon extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            /*try {
                socket = IO.socket("http://192.168.1.104:8888");
                socket.connect();
            } catch (URISyntaxException e) {}
*/
            MainActivity.socket.on("message", handler);
            return null;
        }

        private Emitter.Listener handler = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                final String obtainedmessage;
                try {
                    obtainedmessage = object.getString("message").toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Adapter.chatmessages.add(" : " + obtainedmessage);
                            chat_adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.socket.disconnect();

    }
}
