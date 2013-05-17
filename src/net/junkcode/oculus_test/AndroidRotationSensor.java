package net.junkcode.oculus_test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class AndroidRotationSensor implements SensorEventListener{

	private SensorManager sensorManager;
	private Sensor sensor;
	float azimuth=0.0f;
	float pitch=0.0f;
	float roll=0.0f;
	
	public AndroidRotationSensor(Context cont){
		sensorManager = (SensorManager) cont.getSystemService(Context.SENSOR_SERVICE);
	    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    if (sensor != null) {
	    	sensorManager.registerListener(this, sensor,
	          SensorManager.SENSOR_DELAY_NORMAL);
	      Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");

	    } else {
	      Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
	      Toast.makeText(cont, "ORIENTATION Sensor not found",
	          Toast.LENGTH_LONG).show();
	    }
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		azimuth = event.values[0];
		pitch = event.values[2];
		roll = event.values[1];
		
	}
	public float getAzimuth(){
		return azimuth;
	}
	public float getPitch(){
		return pitch;
	}
	public float getRoll(){
		return roll;
	}

}
