package com.example.ayush.chat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

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
    SocketIO socketIO;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        chathistory = (RecyclerView) findViewById(R.id.chathistory);
        message = (EditText) findViewById(R.id.newmessage);
        sendbutton = (Button) findViewById(R.id.send);
        calendar = Calendar.getInstance();
        final MediaPlayer mediaPlayer =MediaPlayer.create(this, R.raw.blop);

        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.USER_NAME_TAG);
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
                    socketIO.emit("message", message2send);
                    message.setText("");
                    mediaPlayer.start();
                }
            }
        });
    }

    class ConnectionDaemon extends AsyncTask<String, Void, Void> {
        String temp="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                socketIO = new SocketIO("http://192.168.1.104:8888");
            } catch (MalformedURLException e) {
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d("SocketIO", "Connection check");
            socketIO.connect(new IOCallback() {

                @Override
                public void onDisconnect() {
                    Log.d("SocketIO", "onDisconnect");
                }

                @Override
                public void onConnect()
                {
                    Log.d("SocketIO", "onConnect");
                    socketIO.emit("user_info",username);
                }

                @Override
                public void onMessage(String s, IOAcknowledge ioAcknowledge) {
                    Log.d("SocketIO", "onMessageString");
                }

                @Override
                public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {
                    Log.d("SocketIO", "onMessageJSON");
                }

                @Override
                public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {

                    if ("message".equals(s) && objects.length > 0) {
                        Log.d("SocketIO", "onMessageONA");
                        JSONObject object = (JSONObject) objects[0];
                        try {
                            temp =object.getString("message");
                            Log.d("SocketIO", temp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Adapter.chatmessages.add(temp);
                                chat_adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                @Override
                public void onError(SocketIOException e) {
                    Log.d("SocketIO", "onError : " + e.getMessage());
                }
            });
            return null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
