package example.com.customtrainingactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.R;


public class TrainingActivity extends Activity implements OnClickListener {
    private int RATE = SensorManager.SENSOR_DELAY_GAME;
    private SensorManager sensorManager;
    private TrainingSensorListner accelerometerListener;
    private TrainingSensorListner gyroscopeListener;
    private Button m_btnStart;
    private TextView m_tvMessage;
    private ImageView m_ivActivity;
    private Intent intent = null;
    private String state = null;
    private MakeClassifier mkClassifier;
    private Vibrator vi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        intent = getIntent();
        state = intent.getStringExtra("state");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_btnStart = (Button) findViewById(R.id.start_btn);
        m_tvMessage = (TextView) findViewById(R.id.message);

        m_tvMessage.setText(state+" Training");

        m_ivActivity = (ImageView) findViewById(R.id.training_image_view);

        if(state.equals("Running"))
             m_ivActivity.setImageResource(R.drawable.running_image);
        else if(state.equals("Walking"))
             m_ivActivity.setImageResource(R.drawable.walking_image);
        else if(state.equals("PushUp"))
             m_ivActivity.setImageResource(R.drawable.pressup_image);
        else if(state.equals("SitUp"))
             m_ivActivity.setImageResource(R.drawable.situp_image);

        vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometerListener = new TrainingSensorListner();
        gyroscopeListener = new TrainingSensorListner();

        m_btnStart.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start_btn:
                startReadingSensorData();
                break;
            default:
                break;
        }
    }

    public void startReadingSensorData() {

        m_btnStart.setEnabled(false);
        m_tvMessage.setText("Start after: " + 3 + "\"");

        new CountDownTimer(11000, 1000) {

            int countdown = 3;
            public void onTick(long millisUntilFinished) {

                if(countdown>0) {
                    m_tvMessage.setText("Start after: " + countdown + "\"");
                }
                if(countdown==1)
                {
                    vi.vibrate(1000);

                    Log.d("vibrate end","T");

                }
                else if (countdown==0)
                {
                    m_tvMessage.setText("Training");

                    mkClassifier = new MakeClassifier(state);
                    mkClassifier.start();

                    sensorManager.registerListener(accelerometerListener,
                            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            RATE);

                    sensorManager.registerListener(gyroscopeListener,
                            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                            RATE);

                    Log.d("listener register end","T");

                }
                countdown--;
            }

            public void onFinish() {
                m_tvMessage.setText(state+" Training\nfinished");
                m_btnStart.setEnabled(true);

                sensorManager.unregisterListener(accelerometerListener);
                sensorManager.unregisterListener(gyroscopeListener);

                Log.d("listener unregister end","T");

                CollectedSensingData.raw_data.clear();
                CollectedSensingData.raw_data2.clear();
            }
        }.start();


    }
}
