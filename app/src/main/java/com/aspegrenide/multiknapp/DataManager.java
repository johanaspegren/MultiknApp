package com.aspegrenide.multiknapp;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class DataManager {

    private static final String LOG_TAG = "3M DataManager";
    DatabaseReference mDatabase;
    ArrayList<Message> messages = new ArrayList<Message>();

    public void writeMessageToFirebase(Message message, String dataBaseSet) {
        Log.d(LOG_TAG, "write message:" + message.toString() + ", database:" + dataBaseSet);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(dataBaseSet).child(message.getTimeStamp() + "").setValue(message);
    }

    public void writeMessagesToFirebase(ArrayList<Message> messages, String dataBaseSet) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        for (Message m : messages) {
            mDatabase.child(dataBaseSet).child(m.getTimeStamp() + "").setValue(m);
        }
    }
}