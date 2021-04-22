package com.example.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView position, requirement;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        intent=getIntent();

        position=findViewById(R.id.position);
        requirement=findViewById(R.id.requirement);

        String posStr = intent.getStringExtra("position");
        String reqStr = intent.getStringExtra("requirement");

        position.setText("Position: " +posStr);
        requirement.setText("Requirement: " +reqStr);

    }
}