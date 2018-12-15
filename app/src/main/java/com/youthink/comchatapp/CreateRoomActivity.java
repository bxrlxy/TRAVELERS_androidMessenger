package com.youthink.comchatapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class CreateRoomActivity extends AppCompatActivity implements View.OnClickListener {

    EditText titleView;
    EditText remainsView;
    TextView deadlineView;
    EditText locationView;
    EditText contentView;
    Button createBtn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int timeStamp; // deadline

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        titleView=(EditText)findViewById(R.id.room_title);
        remainsView = (EditText)findViewById(R.id.room_remains);
        deadlineView = (TextView)findViewById(R.id.room_deadline);
        locationView = (EditText)findViewById(R.id.room_location);
        contentView = (EditText)findViewById(R.id.room_content);
        createBtn = (Button)findViewById(R.id.room_btn);

        deadlineView.setOnClickListener(this);
        locationView.setOnClickListener(this);
        createBtn.setOnClickListener(this);

    }

    public void onClick(View v){
        if(v == deadlineView){
            Calendar c= Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener(){
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                            System.out.println(year);
                            System.out.println(monthOfYear);
                            System.out.println(dayOfMonth);
                        }
                    }, year, month, day);
            datePickerDialog.show();

        }else if(v == locationView){

        }else if(v == createBtn){
            String title = titleView.getText().toString();
            String remains = remainsView.getText().toString();
            String location = locationView.getText().toString();
            String content = contentView.getText().toString();

            Intent intent = getIntent();
            intent.putExtra("title",title);
            intent.putExtra("remains",remains);
            intent.putExtra("location",location);
            ChatRoom room = new ChatRoom(title, "ME", remains, "20181213", content, location);
            db.collection("chatrooms").add(room);
            setResult(RESULT_OK,intent);
            finish();

        }

    }
}
