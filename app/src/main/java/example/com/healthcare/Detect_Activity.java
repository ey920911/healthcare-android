package example.com.healthcare;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bean.UserItem;
import example.com.R;


public class Detect_Activity extends Activity {

    private int RATE = SensorManager.SENSOR_DELAY_GAME;
    private AlarmManager mManager;
    private SensorManager sensorManager;
    private Sensor_Listner accelerometerListener;
    private Sensor_Listner gyroscope;
    private Classifier_Thread classifier_thread;
    private boolean isStart = false;
    private String m_strClassifier;//classifier 파일 경로 지정
    private DetectHandler handler;//
    private TextView exercise;//
    private TextView scoreText; ////
    private TextView totalScoreText; ////
    private ImageView detect_image;
    private double[] caloriePerTime;
    private Updata_Score_Connection update;
    private boolean isSuccess= false;
    private ProgressDialog progress;
    private Rank_Connection rank;
    private Intent intent;
    ToggleButton toggle;
    private int count = 4;
    CountDownTimer mCountDown = null;
    private ArrayList<String> m_accClassifierFilePathList;
    private ArrayList<String> m_gyroClassifierFilePathList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_activity);
        progress = new ProgressDialog(this);
        progress.setTitle("Wait...");
        progress.setMessage("Please Wait Saving...");
        progress.setCancelable(false);
        BufferedReader reader = null;
        Info.m_mcfcAcc.clear_array();
        Info.m_mcfcGyro.clear_array();
        Info.m_mcfcAcc.clearListsInKNN();
        try {
            m_strClassifier = "AccClassifier.csv";
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(m_strClassifier), "UTF-8"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Info.m_mcfcAcc.setAccClassifierFromFile(reader);

        try {
            m_strClassifier = "GyroClassifier.csv";
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(m_strClassifier), "UTF-8"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Info.m_mcfcAcc.setGyroClassifierFromFile(reader);


        m_accClassifierFilePathList = (ArrayList<String>) getIntent().getStringArrayListExtra("accClassifierFilePathList");
        m_gyroClassifierFilePathList = (ArrayList<String>) getIntent().getStringArrayListExtra("gyroClassifierFilePathList");
        Info.m_mcfcAcc.setAccTrainingClassifierFromFile(m_accClassifierFilePathList);
        Info.m_mcfcAcc.setGyroTrainingClassifierFromFile(m_gyroClassifierFilePathList);

        Info.m_mcfcAcc.setKOfKNN(9);

        setContentView(R.layout.detect_activity);

        mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        exercise = (TextView)findViewById(R.id.exercise);//
        scoreText = (TextView)findViewById(R.id.scoreText); ////
        totalScoreText = (TextView)findViewById(R.id.totalScoreText);////
        detect_image = (ImageView)findViewById(R.id.detect_image);
        handler = new DetectHandler(exercise, scoreText, totalScoreText,detect_image);////
        totalScoreText.setText(((Integer)(int)(double) Info.User_info.getTotal()).toString());////



        toggle = ((ToggleButton)findViewById(R.id.startstop_btn));
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("toggle", toggle.getText().toString());
                if(toggle.getText().toString().equals("FINISH"))
                {
                    toggle.setBackgroundResource(R.drawable.big_button_bg_off);
                    Log.d("Listener", "등록됨");


                    mCountDown = new CountDownTimer(6000, 1000){
                        public void onTick(long millisUntilFinished) {

                            toggle.setClickable(false);
                            exercise.setText(count+"");
                            count--;
                            if(count == 1)
                            {
                                Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vi.vibrate(1000);
                            }

                        }
                        public void onFinish() {
                            count=4;
                            exercise.setText("ACTIVITY");
                            for(int i =0; i< Info.caloriePerTime.length;i++)
                                Info.caloriePerTime[i] = 0.0;
                            startReadingSensorData();
                            toggle.setClickable(true);
                        }

                    }.start();

                }
                else {
                    toggle.setBackgroundResource(R.drawable.big_button_bg_on);
                    init();
                    stopRedingSensorData();
                    update = new Updata_Score_Connection();
                    update.start();
                }
            }
        });
    }

    public void init(){
        exercise.setText("ACTIVITY");
        detect_image.setImageResource(R.drawable.walking_image);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startReadingSensorData()
    {
        if (!isStart) {
            handler.sendEmptyMessage(100); //// 종료버튼
            accelerometerListener = new Sensor_Listner();
            gyroscope = new Sensor_Listner();

            this.classifier_thread = new Classifier_Thread(handler);

            sensorManager.registerListener(accelerometerListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    RATE);

            sensorManager.registerListener(gyroscope,
                    sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    RATE);

            classifier_thread.start();
            isStart = true;
        }
    }



    public void stopRedingSensorData()
    {
        if (isStart)
        {
            sensorManager.unregisterListener(accelerometerListener);
            sensorManager.unregisterListener(gyroscope);
            classifier_thread.stopThread();
            classifier_thread = null;
            isStart = false;
            Toast.makeText(getApplicationContext(), "종료되었습니다!!", Toast.LENGTH_SHORT).show();

        }
    }

    public class Updata_Score_Connection extends Thread {




        @Override
        public void run() {
            super.run();

            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/updateScore");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);


                //  Log.e("start", "start");

                JSONObject json = new JSONObject();
                json.put("phonenum", Info.User_info.getPhonenum());
                json.put("total",totalScoreText.getText().toString().split("k")[0]);
                json.put("run",String.valueOf(Info.User_info.getRun()+(int)Info.caloriePerTime[0]));
                json.put("walk",String.valueOf(Info.User_info.getWalk()+(int)Info.caloriePerTime[1]));
                json.put("pushup",String.valueOf(Info.User_info.getPushup()+(int)Info.caloriePerTime[2]));
                json.put("situp",String.valueOf(Info.User_info.getSitup()+(int)Info.caloriePerTime[3]));



                //Log.e("ss", jsonarray.toString());
                 Log.e("ss", json.toString());


                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                // Log.e("start", "보냄");
                String response;

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Log.e("start", "OK");
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                    }
                    String sResult = builder.toString();

                    Log.e("response", sResult);
                    if (sResult.equals("success")) {



                    } else {


                    }


                } else {
                    Log.e("start", "실패");
                }

            } catch (Exception e) {

            }


        }





    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        progress.show();
        rank = new Rank_Connection();
        rank.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    public class Rank_Connection extends AsyncTask<Void, String, Void> {


        public Rank_Connection() {


        }


        @Override
        protected void onPreExecute() {


        }


        protected void onProgressUpdate(String result) {


        }


        @Override
        protected Void doInBackground(Void... params) {



            Info.clear_rank();
            receive_rank(0, "getTotalRank");
            receive_rank(1, "getWalkRank");
            receive_rank(2, "getRunRank");
            receive_rank(3, "getSitupRank");
            receive_rank(4, "getPushupRank");
            receive_myInfo();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            intent = new Intent(getApplicationContext(), Health_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }




        public void receive_myInfo() {
            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/getMyInfo");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Log.e("Fragment", "start");

                JSONObject json = new JSONObject();
                json.put("phonenum", Info.PHONE_NUM);
                //json.put("password", password);

                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                // Log.e("Fragment", "보냄");
                String response;

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //  Log.e("Fragment", "OK");
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String sResult = "";
                    String str;


                    while ((str = reader.readLine()) != null) {
                        sResult += str;


                    }

                    JSONObject object = new JSONObject(sResult);
                    //Log.e("response", sResult);

                    // Log.e("response", sResult);
                    if (!sResult.equals("fail")) {


                        byte[] image = null;
                        Bitmap profile = null;


                        if (object.get("image") != null) {
                            image = Base64.decode(object.get("image").toString(), 0);

                            profile = BitmapFactory.decodeByteArray(image, 0, image.length);

                        }


                        Info.User_info.setNickname(object.get("nickname").toString());
                        Info.User_info.setAge(object.get("age").toString());
                        Info.User_info.setSex(object.get("sex").toString());
                        Info.User_info.setWeight(object.get("weight").toString());
                        Info.User_info.setHeight(object.get("height").toString());
                        Info.User_info.setImage(profile);
                        Info.User_info.setPhonenum(object.get("phonenum").toString());
                        Info.User_info.setTotal(Integer.parseInt(object.get("total").toString()));
                        Info.User_info.setWalk(Integer.parseInt(object.get("walking").toString()));
                        Info.User_info.setRun(Integer.parseInt(object.get("running").toString()));
                        Info.User_info.setPushup(Integer.parseInt(object.get("pushup").toString()));
                        Info.User_info.setSitup(Integer.parseInt(object.get("situp").toString()));

                    }


                } else {
                    Log.e("Fragment", "실패");
                }

            } catch (Exception e) {

            }
        }


        public void receive_rank(int type, String url_name) {
            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/" + url_name);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Log.e("Fragment", "start");

                JSONObject json = new JSONObject();
                json.put("phonenum", Info.PHONE_NUM);
                //json.put("password", password);

                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                //Log.e("Fragment", "보냄");
                String response;

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Log.e("Fragment", "OK");
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String sResult = "";
                    String str;

                    JSONArray array = new JSONArray();
                    while ((str = reader.readLine()) != null) {
                        sResult += str;
                        JSONObject result = new JSONObject(str);
                        array.put(result);
                    }
                    // Log.e("response", sResult);

                    Log.e("response", sResult);
                    if (!sResult.equals("fail")) {
                        isSuccess = true;

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            byte[] image = null;
                            Bitmap profile = null;
                            if(type ==0) {
                                if (object.get("image") != null) {
                                    image = Base64.decode(object.get("image").toString(), 0);

                                    profile = BitmapFactory.decodeByteArray(image, 0, image.length);

                                }
                            }

                            switch (type) {
                                case 0:
                                    Info.Rank_Total.add(new UserItem(object.get("nickname").toString(), Integer.parseInt(object.get("score").toString()), profile,object.get("phonenum").toString()));
                                    break;
                                case 1:
                                    Info.Rank_Walk.add(new UserItem(object.get("nickname").toString(), Integer.parseInt(object.get("score").toString()), search_image(object.get("phonenum").toString()),object.get("phonenum").toString()));
                                    break;
                                case 2:
                                    Info.Rank_Run.add(new UserItem(object.get("nickname").toString(), Integer.parseInt(object.get("score").toString()), search_image(object.get("phonenum").toString()),object.get("phonenum").toString()));
                                    break;
                                case 3:
                                    Info.Rank_Situp.add(new UserItem(object.get("nickname").toString(), Integer.parseInt(object.get("score").toString()), search_image(object.get("phonenum").toString()),object.get("phonenum").toString()));
                                    break;
                                case 4:
                                    Info.Rank_Pushup.add(new UserItem(object.get("nickname").toString(), Integer.parseInt(object.get("score").toString()), search_image(object.get("phonenum").toString()),object.get("phonenum").toString()));
                                    break;

                            }

                        }

                    } else {
                        isSuccess = false;
                        Log.e("랭크", "등록실패");
                    }
                } else {
                    Log.e("Fragment", "실패");
                }

            } catch (Exception e) {

            }
        }

        public Bitmap search_image(String phonenum){

            for(UserItem user : Info.Rank_Total)
            {
                if(user.getPhonenum().equals(phonenum))
                {
                    return user.getImage();
                }
            }



            return null;

        }


    }









}
