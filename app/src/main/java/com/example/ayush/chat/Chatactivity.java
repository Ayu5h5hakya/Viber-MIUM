package com.example.ayush.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ayush on 10/27/2015.
 */
public class Chatactivity extends AppCompatActivity {
    ListView chathistory;
    EditText message;
    Button sendbutton;
    ConnectionDaemon connectionDaemon;
    private String message2send="";
    Adapter chat_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        chathistory = (ListView) findViewById(R.id.chathistory);
        message = (EditText) findViewById(R.id.newmessage);
        sendbutton = (Button) findViewById(R.id.send);
        chat_adapter = new Adapter(this);
        chathistory.setAdapter(chat_adapter);
        connectionDaemon = new ConnectionDaemon();
        connectionDaemon.execute();
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message2send = message.getText().toString();
                if (!message2send.isEmpty()) {
                    Adapter.chatmessages.add("YOU : "+message2send);
                    chat_adapter.notifyDataSetChanged();
                    MainActivity.socket.emit("message", message2send);
                    message.setText("");
                }
            }
        });
    }

    class ConnectionDaemon extends AsyncTask<String,Void,Void> {
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

        private Emitter.Listener handler = new Emitter.Listener(){

            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                final String obtainedmessage;
                try
                {
                    obtainedmessage = object.getString("message").toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Adapter.chatmessages.add("SERVER : "+obtainedmessage);
                            chat_adapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (JSONException e) {}
            }
        };
    }
}
