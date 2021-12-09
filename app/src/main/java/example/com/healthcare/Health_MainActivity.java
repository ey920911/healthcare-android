package example.com.healthcare;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bean.UserInfo;
import bean.UserItem;
import example.com.customtrainingactivity.TrainingMainActivity;
import example.com.R;
import example.com.wy_WindowPartition.MyFileReader;
import knn.finalStaticVariables;


public class Health_MainActivity extends FragmentActivity implements View.OnClickListener{
    private Button btnStart;
    private PagerSlidingTabStrip tab;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private Intent intent = null;
    private BackPressCloseHandler backPressCloseHandler;

    private ImageView my_profile_image;
    private TextView my_name_text;
    private TextView bmi_text;
    private BMI bmi;

    private Button m_btnSettingTraining;
    private ImageView image_Join;
    private Dialog dialog_setting;
    private String name;
    private String password;
    private String sex;
    private String age;
    private String height;
    private String weight;
    private String PhoneNumber;
    private Update_Myprofile_connection update;
    private Contacts_Connection contacts;
    private SharedPreferences prefs;
    private boolean isAuto;
    private Bitmap profile_bitmap;
    private boolean isChangImage = false;

    private MyFileReader m_mFileRead = new MyFileReader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_main);


        my_profile_image = (ImageView)findViewById(R.id.myImage);
        my_name_text = (TextView)findViewById(R.id.nameText);
        bmi_text = (TextView)findViewById(R.id.bmiText);

        init_my_profile();

        m_btnSettingTraining = (Button) findViewById(R.id.setting);
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        isAuto = prefs.getBoolean("AutoLogin", isAuto);
        Log.e("ss",isAuto+"");

        backPressCloseHandler = new BackPressCloseHandler(this);

        if (savedInstanceState == null) {


            tab = (PagerSlidingTabStrip)findViewById(R.id.slidingTab);
            pager = (ViewPager)findViewById(R.id.pager);

            btnStart = (Button)findViewById(R.id.btnStart);
            btnStart.setOnClickListener(this);
            btnStart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch(motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            btnStart.setBackgroundResource(R.drawable.big_button_bg_on);
                            break;
                        case MotionEvent.ACTION_UP:
                            btnStart.setBackgroundResource(R.drawable.big_button_bg_off);
                            break;
                    }
                    return false;
                }
            });

            adapter = new MyPagerAdapter(getSupportFragmentManager(),this);

            pager.setAdapter(adapter);
           /* final int pageMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            pager.setPageMargin(pageMargin);
*/
            tab.setViewPager(pager);
        }
        my_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText edit_name, edit_pass, edit_age, edit_height, edit_weight;
                final Button cancel, ok, title_cancle;
                ImageView imageview;

               /* AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);*/
                LayoutInflater inflater = (LayoutInflater) Health_MainActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.profile_edit, null);
                image_Join = (ImageView) layout.findViewById(R.id.image_edit);
                edit_name = (EditText) layout.findViewById(R.id.name_edit);
                edit_age = (EditText) layout.findViewById(R.id.age_edit);
                edit_height = (EditText) layout.findViewById(R.id.height_edit);
                edit_weight = (EditText) layout.findViewById(R.id.weight_edit);
                cancel = (Button) layout.findViewById(R.id.reg_cancel_edit);
                ok = (Button) layout.findViewById(R.id.reg_ok_edit);
                title_cancle = (Button) layout.findViewById(R.id.title_cancel_edit);
                title_cancle.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                title_cancle.setBackgroundResource(R.drawable.cancle_icon_on);
                                break;
                            case MotionEvent.ACTION_UP:
                                title_cancle.setBackgroundResource(R.drawable.cancle_icon_off);
                                break;
                        }
                        return false;
                    }
                });
                cancel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                cancel.setBackgroundResource(R.drawable.button_bg_on);
                                break;
                            case MotionEvent.ACTION_UP:
                                cancel.setBackgroundResource(R.drawable.button_bg_off);
                                break;
                        }
                        return false;
                    }
                });
                ok.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                ok.setBackgroundResource(R.drawable.button_bg_on);
                                break;
                            case MotionEvent.ACTION_UP:
                                ok.setBackgroundResource(R.drawable.button_bg_off);
                                break;
                        }
                        return false;
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_setting.dismiss();

                    }
                });
                title_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_setting.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        name = edit_name.getText().toString();
                        age = edit_age.getText().toString();
                        height = edit_height.getText().toString();
                        weight = edit_weight.getText().toString();


                        if (name.length() == 0 || age.length() == 0 || height.length() == 0 || weight.length() == 0) {// null이 아니라 "" return

                            Toast.makeText(getApplicationContext(), "모든정보를 입력해주세요!", Toast.LENGTH_SHORT).show();


                        } else {

                            BitmapDrawable drawable = (BitmapDrawable) image_Join.getDrawable();
                            Bitmap imgbitmap = drawable.getBitmap();
                            Info.User_info.setNickname(name);
                            Info.User_info.setAge(age);
                            Info.User_info.setHeight(height);
                            Info.User_info.setWeight(weight);
                            if(isChangImage)
                            {
                                Bitmap resizebitmap = Bitmap.createScaledBitmap(profile_bitmap, 100, 100, true);
                                Info.User_info.setImage(resizebitmap);
                            }
                            update = new Update_Myprofile_connection();
                            update.start();
                            dialog_setting.dismiss();
                        }
                    }
                });
                image_Join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Health_MainActivity.this.startActivityForResult(intent, 0);
                    }
                });

                /*alert.setView(layout);*/

                dialog_setting = new Dialog(Health_MainActivity.this);
                dialog_setting.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_setting.setCanceledOnTouchOutside(false);
                dialog_setting.setContentView(layout);
                dialog_setting.show();
                Bitmap resize_bitmap = Bitmap.createScaledBitmap(Info.User_info.getImage(), 500, 500, true);
                image_Join.setImageBitmap(resize_bitmap);
                edit_name.setText(Info.User_info.getNickname());
                edit_age.setText(Info.User_info.getAge());
                edit_height.setText(Info.User_info.getHeight());
                edit_weight.setText(Info.User_info.getWeight());

            }
            //  EnableHandler handler = new EnableHandler(dialog);

                /*thread = new EnableThread(layout, dialog, handler,PhoneNumber);


                thread.start();*/
        });
        m_btnSettingTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Button cancel, ok, title_cancle;
                final CheckBox autoLoginSetting;
                final  Button training;

               /* AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);*/
                LayoutInflater inflater = (LayoutInflater) Health_MainActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.setting, null);
                image_Join = (ImageView) layout.findViewById(R.id.image);

                cancel = (Button) layout.findViewById(R.id.reg_cancel);
                ok = (Button) layout.findViewById(R.id.reg_ok);
                title_cancle = (Button) layout.findViewById(R.id.title_cancel);
                autoLoginSetting = (CheckBox)layout.findViewById(R.id.autoLoginSetting);
                training = (Button)layout.findViewById(R.id.training);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_setting.dismiss();

                    }
                });
                title_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_setting.dismiss();
                    }
                });
                title_cancle.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                title_cancle.setBackgroundResource(R.drawable.cancle_icon_on);
                                break;
                            case MotionEvent.ACTION_UP:
                                title_cancle.setBackgroundResource(R.drawable.cancle_icon_off);
                                break;
                        }
                        return false;
                    }
                });
                cancel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                cancel.setBackgroundResource(R.drawable.button_bg_on);
                                break;
                            case MotionEvent.ACTION_UP:
                                cancel.setBackgroundResource(R.drawable.button_bg_off);
                                break;
                        }
                        return false;
                    }
                });
                ok.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                ok.setBackgroundResource(R.drawable.button_bg_on);
                                break;
                            case MotionEvent.ACTION_UP:
                                ok.setBackgroundResource(R.drawable.button_bg_off);
                                break;
                        }
                        return false;
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((CheckBox) layout.findViewById(R.id.autoLoginSetting)).isChecked())
                            isAuto = true;
                        else
                            isAuto = false;

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("AutoLogin", isAuto);
                        editor.commit();
                        dialog_setting.dismiss();
                    }
                });
                training.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("training","click");
                        intent = new Intent(getApplicationContext(), TrainingMainActivity.class);
                        startActivity(intent);
                    }
                });

                training.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                training.setBackgroundResource(R.drawable.button_bg_off);
                                break;
                            case MotionEvent.ACTION_UP:
                                training.setBackgroundResource(R.drawable.button_bg_on);
                                break;
                        }
                        return false;
                    }
                });
                autoLoginSetting.setChecked(isAuto);
                Log.d("자동로그인", String.valueOf(isAuto));

                /*alert.setView(layout);*/

                dialog_setting = new Dialog(Health_MainActivity.this);
                dialog_setting.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_setting.setCanceledOnTouchOutside(false);
                dialog_setting.setContentView(layout);
                dialog_setting.show();

            }
            //  EnableHandler handler = new EnableHandler(dialog);

                /*thread = new EnableThread(layout, dialog, handler,PhoneNumber);


                thread.start();*/
        });

    }


    public void init_my_profile(){
        my_profile_image.setImageBitmap(Info.User_info.getImage());
        my_name_text.setText(Info.User_info.getNickname());
        bmi = new BMI(Double.parseDouble(Info.User_info.getHeight()),Double.parseDouble(Info.User_info.getWeight()));
        bmi_text.setText(bmi.checkObesity());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        backPressCloseHandler.onBackPressed();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                int imageHeight = image_Join.getHeight();
                profile_bitmap = getCorrectlyOrientedImage(getApplicationContext(), selectedImage, imageHeight);
                Bitmap resize_bitmap = Bitmap.createScaledBitmap(profile_bitmap, imageHeight, imageHeight, true);
                isChangImage = true;
                image_Join.setImageBitmap(resize_bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri, int imageHeight) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > imageHeight || rotatedHeight > imageHeight) {
            float widthRatio = ((float) rotatedWidth) / ((float) imageHeight);
            float heightRatio = ((float) rotatedHeight) / ((float) imageHeight);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnStart:

                m_mFileRead.setFolder(finalStaticVariables.DIR_FOLDER+"/AccClassifier");
                m_mFileRead.setFilePathList();
                ArrayList<String> accClassifierFilePathList = m_mFileRead.getCSVList();
//                Log.d("csv acc file count",accClassifierFilePathList.size()+"");

                m_mFileRead.setFolder(finalStaticVariables.DIR_FOLDER+"/GyroClassifier");
                m_mFileRead.setFilePathList();
                ArrayList<String> gyroClassifierFilePathList = m_mFileRead.getCSVList();
//                Log.d("csv gyro file count",gyroClassifierFilePathList.size()+"");


//                boolean isAccGyroClassifier = false;
//                if(accClassifierFilePathList.size() == 4 && gyroClassifierFilePathList.size() == 4)
//                    isAccGyroClassifier=true;
                boolean isAccGyroClassifier = true;
                if(isAccGyroClassifier) {
                    intent = new Intent(getApplicationContext(), Detect_Activity.class);
                    intent.putExtra("accClassifierFilePathList", accClassifierFilePathList);
                    intent.putExtra("gyroClassifierFilePathList", gyroClassifierFilePathList);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"어플리케이션을 훈련시키세요!",Toast.LENGTH_LONG).show();
                    m_btnSettingTraining.callOnClick();
                }
                break;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private final String[] TITLES = {"Total","Walk","Run","PushUp","SitUp"};
        private final int[] ICONS = { R.drawable.total_icon_off, R.drawable.walking_icon_off,
                R.drawable.running_icon_off,R.drawable.pressup_icon_off,R.drawable.situp_icon_off };
        private Context context;
        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getPageIconResId(int position) {
            // TODO Auto-generated method stub
            return ICONS[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return FragScore.newInstance(0);
                case 1:
                    return FragScore.newInstance(1);
                case 2:
                    return FragScore.newInstance(2);
                case 3:
                    return FragScore.newInstance(3);
                case 4:
                    return FragScore.newInstance(4);
                default:
                    return null;
            }

        }

    }



    public class Update_Myprofile_connection extends Thread {

        @Override
        public void run() {
            super.run();

            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/updateMyprofile");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                byte[] byteArray=null;
                Log.e("start", "start");

                byteArray = bitmapToByteArray(Info.User_info.getImage());

                String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));

                JSONObject json = new JSONObject();
                json.put("phonenum", Info.User_info.getPhonenum());
                json.put("nickname", Info.User_info.getNickname());
                json.put("password", Info.User_info.getPassword());
                json.put("age", Info.User_info.getAge());
                json.put("sex", Info.User_info.getSex());
                json.put("weight", Info.User_info.getWeight());
                json.put("height", Info.User_info.getHeight());
                json.put("image", imageString);

                Log.e("start", imageString);
                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                Log.e("start", "보냄");
                String response;

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //     Log.e("start", "OK");
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                    }
                    String sResult = builder.toString();

                    //  Log.e("response", sResult);

                    if (sResult.equals("success")) {

                        mHandler.sendEmptyMessage(0);
                        contacts = new Contacts_Connection(PhoneNumber);
                        contacts.start();

                    }

                } else {
                    mHandler.sendEmptyMessage(3);
                    Log.e("start", "실패");
                }

            } catch (Exception e) {

            }


        }

        public byte[] bitmapToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "회원정보가 변경 되었습니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "로그인 하였습니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "회원정보가 변경 되지 않았습니다!!", Toast.LENGTH_SHORT).show();

                default:
                    break;
            }
        }

    };

    public class Contacts_Connection extends Thread {

        private String phonenum = null;

        public Contacts_Connection(String phonenum) {
            this.phonenum = phonenum;
        }

        @Override
        public void run() {
            super.run();

            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/Contacts");
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
                json.put("phonenum", phonenum);

                ArrayList<String> my_contacts = getContactList();
                JSONArray jsonarray = new JSONArray(my_contacts);
                json.put("contacts", jsonarray);
                //Log.e("ss", jsonarray.toString());
                // Log.e("ss", json.toString());


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

                    //Log.e("response", sResult);
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

    public ArrayList<String> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<String> contactlist = new ArrayList<String>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(0).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3)
                            + phonenumber.substring(3, 6)
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3)
                            + phonenumber.substring(3, 7)
                            + phonenumber.substring(7);
                }


                contactlist.add(phonenumber);
            } while (contactCursor.moveToNext());
        }

        if(!contactlist.contains(Info.PHONE_NUM))
            contactlist.add(Info.PHONE_NUM);

        return contactlist;

    }






}
