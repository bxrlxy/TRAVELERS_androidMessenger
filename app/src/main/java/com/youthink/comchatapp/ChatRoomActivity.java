package com.youthink.comchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.youthink.comchatapp.R;


public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView sendBtn;
    EditText sendView;
    ListView msgList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendBtn = (ImageView)findViewById(R.id.send_btn);
        sendView = (EditText)findViewById(R.id.send_text);
        msgList = (ListView)findViewById(R.id.msg_list);

        sendBtn.setOnClickListener(this);



    }

    public void onClick(View v){
        if(v==sendBtn){
            String msg = sendView.getText().toString();
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            String userId = "me";
            Message message = new Message(msg,ts,userId);
            db.collection("messages").add(message);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 20 && resultCode == RESULT_OK){
            Toast toast = Toast.makeText(this, "나눔방 생성 완료", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

}