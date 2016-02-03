package com.corp.random.airmouse;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;



/**
 * Created by sujay on 31/1/16.
 */
public class AirMouse extends AppCompatActivity implements View.OnClickListener
{
    Client c;
    private SensorManager sensorManager;
    private Sensor sensor;

    private SensorEventListener mySensorEventListener = new SensorEventListener()
    {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            StringBuilder str = new StringBuilder();
            for (float x:event.values)
            {
                str.append(x);
                str.append(' ');
            }
            c.send(str.toString());

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_mouse);
        findViewById(R.id.bleft).setOnClickListener(this);
        findViewById(R.id.bright).setOnClickListener(this);
        final String hostname = getIntent().getStringExtra("IP");
        Log.d("AirMouse Hostname",hostname);
        c = new Client(hostname);
        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(sensor==null)
        {
            Log.d("AirMouse","Sensor Not Available");
            finish();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(mySensorEventListener, sensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(mySensorEventListener);
        finish();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        c.disConnect();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bleft:
                c.send("LEFT");
                break;
            case R.id.bright:
                c.send("RIGHT");
                break;
        }
    }
}
