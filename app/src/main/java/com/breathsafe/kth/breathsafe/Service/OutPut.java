package com.breathsafe.kth.breathsafe.Service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.R;

public class OutPut extends AppCompatActivity {

    TextView textmessage;
    String stringValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_put);

        textmessage = (TextView) findViewById(R.id.textView1);

        Intent intent = getIntent();
        stringValue = intent.getStringExtra("content");
        textmessage.setText(stringValue);
        System.out.println(stringValue);

    }
}
