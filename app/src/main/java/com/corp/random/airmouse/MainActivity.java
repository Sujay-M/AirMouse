package com.corp.random.airmouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bdone).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,AirMouse.class);
        i.putExtra("IP",((EditText)findViewById(R.id.etIP)).getText().toString());
        startActivity(i);
    }
}
