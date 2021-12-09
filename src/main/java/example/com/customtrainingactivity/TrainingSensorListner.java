package example.com.customtrainingactivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class TrainingSensorListner implements SensorEventListener {

	private long current;
	private long current2;
	private long startCurrent;
	private long startCurrent2;

	public TrainingSensorListner() {
        super();
    }

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			double[] rawdata = new double[3];
			rawdata[0] = (double)event.values[0];
            rawdata[1] = (double)event.values[1];
            rawdata[2] = (double)event.values[2];

            CollectedSensingData.raw_data.offer(rawdata);

			break;

		case Sensor.TYPE_GYROSCOPE:
			double[] rawdata2 = new double[3];
            rawdata2[0] = (double)event.values[0];
            rawdata2[1] = (double)event.values[1];
            rawdata2[2] = (double)event.values[2];

            CollectedSensingData.raw_data2.offer(rawdata2);

			break;

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
