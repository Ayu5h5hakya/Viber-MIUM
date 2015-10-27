package com.example.ayush.chat;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Ayush on 10/27/2015.
 */
public class Chat_Sugar extends SugarRecord<Chat_Sugar> {
    String src,des,message;
    Date time;
    public Chat_Sugar(){
    }

    public Chat_Sugar(String src, String des, String message,Date time){
        this.src=src;
        this.des=des;
        this.message=message;
        this.time = time;

    }

}
