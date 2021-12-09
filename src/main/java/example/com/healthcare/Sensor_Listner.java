package example.com.healthcare;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class Sensor_Listner implements SensorEventListener {


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float[] rawdata = event.values;
			String[] floats = new String[rawdata.length + 1];
			for (int i = 0; i < rawdata.length; i++) {
				floats[i] = String.valueOf(rawdata[i]);

			}

			Info.raw_data.offer(floats);

			break;

		case Sensor.TYPE_GYROSCOPE:
			float[] rawdata2 = event.values;
			String[] floats2 = new String[rawdata2.length + 1];
			for (int i = 0; i < rawdata2.length; i++) {
				floats2[i] = String.valueOf(rawdata2[i]);

			}

			Info.raw_data2.offer(floats2);

			break;

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
