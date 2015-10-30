package com.example.ayush.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String MASTER_PASS="a";
    public static String USER_NAME_TAG = "USER_NAME";
    Button connect;
    private String message2send;
//    static Socket socket;
    TextView code_validation;
    EditText name_field,pass_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        connect = (Button) findViewById(R.id.checkcode);
        code_validation = (TextView) findViewById(R.id.code_status);
        name_field = (EditText) findViewById(R.id.user_name);
        pass_field = (EditText) findViewById(R.id.pass_code);
        setSupportActionBar(toolbar);


        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String entered_user= name_field.getText().toString();
                String entered_code = pass_field.getText().toString();
                if(!entered_user.isEmpty() && entered_code.equals(MASTER_PASS))
                {
                    code_validation.setTextColor(Color.GREEN);
                    code_validation.setText("Welcome");
                    Intent intent = new Intent("android.intent.action.chatactivity");
                    intent.putExtra(USER_NAME_TAG,entered_user);
                    startActivity(intent);

                }
                else if(!entered_code.equals(MASTER_PASS))
                {
                    code_validation.setTextColor(Color.RED);
                    code_validation.setText("password is incorrect");
                }
                else
                {
                    code_validation.setTextColor(Color.RED);
                    code_validation.setText("User name should not be empty!");

                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
