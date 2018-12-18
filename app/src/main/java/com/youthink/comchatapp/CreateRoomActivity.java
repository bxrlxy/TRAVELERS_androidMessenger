package com.youthink.comchatapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class CreateRoomActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText titleView;
    EditText remainsView;
    TextView deadlineView;
    TextView locationTextView;
    Spinner locationSpinner;
    EditText contentView;
    Button createBtn;

    String location;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int timeStamp; // deadline

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        titleView = (EditText) findViewById(R.id.room_title);
        remainsView = (EditText) findViewById(R.id.room_remains);
        deadlineView = (TextView) findViewById(R.id.room_deadline);
        locationTextView = (TextView) findViewById(R.id.locationText);
        locationSpinner = (Spinner) findViewById(R.id.spinner);
        contentView = (EditText) findViewById(R.id.room_content);
        createBtn = (Button) findViewById(R.id.room_btn);

        deadlineView.setOnClickListener(this);
        createBtn.setOnClickListener(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.locations,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);

    }

    /* Spinner OnItemSelectedListener */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        location = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback}
    }

    int year,month,day;
    public void onClick(View v){
        if(v == deadlineView){
            Calendar c= Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) +1;
            day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener(){
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                            deadlineView.setText(year+"년"+monthOfYear+"월"+dayOfMonth+"일");
                        }
                    }, year, month, day);
            datePickerDialog.show();

        }else if(v == createBtn){
            String title = titleView.getText().toString();
            String remains = remainsView.getText().toString();
            String content = contentView.getText().toString();
            String deadline = deadlineView.getText().toString();

            Intent intent = getIntent();
            intent.putExtra("title",title);
            intent.putExtra("remains",remains);
            intent.putExtra("location",location);
            ChatRoom room = new ChatRoom(title, "ME", remains, deadline, content, location);
            db.collection("chatrooms").add(room);
            setResult(RESULT_OK,intent);
            finish();

        }

    }
}
