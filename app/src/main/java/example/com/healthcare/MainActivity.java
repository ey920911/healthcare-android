package example.com.healthcare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bean.UserInfo;
import bean.UserItem;
import example.com.R;

public class MainActivity extends Activity {

    private Intent intent = null;
    private Button loginButton;
    private Button registerButton;
    private boolean isAuto;
    private ImageView image_Join;
    private UserInfo userInfo = null;

    resister_connection resister;
    login_connection login;
    String PhoneNumber;
    SharedPreferences prefs;
    Contacts_Connection contacts;
    boolean isSuccess = false;
    private String name;
    private String password;
    private String sex;
    private String age;
    private String height;
    private String weight;
    Dialog dialog_register;
    Dialog dialog_login;
    private ProgressDialog progress;
    Rank_Connection rank;
    private Bitmap profile_bitmap;

    private boolean isProfileImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new ProgressDialog(this);
        progress.setTitle("Wait...");
        progress.setMessage("Please wait loading...");
        progress.setCancelable(false);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        isAuto = false;
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        isAuto = prefs.getBoolean("AutoLogin", isAuto);

        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        PhoneNumber = systemService.getLine1Number();

        PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10, PhoneNumber.length());

        PhoneNumber = "0" + PhoneNumber;
        Info.PHONE_NUM = PhoneNumber;


        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        loginButton.setBackgroundResource(R.drawable.login_icon_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        loginButton.setBackgroundResource(R.drawable.login_icon_on);
                        break;
                }
                return false;
            }
        });
        registerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        registerButton.setBackgroundResource(R.drawable.signin_icon_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        registerButton.setBackgroundResource(R.drawable.signin_icon_on);
                        break;
                }
                return false;
            }
        });


        if (isAuto) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    password = prefs.getString("password", password);

                    login = new login_connection(password, PhoneNumber);
                    login.start();


                }
            });
        } else {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                    final Button  ok, title_cancle;
                    LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    final View layout = inflater.inflate(R.layout.login,null);
                    title_cancle = (Button)layout.findViewById(R.id.title_cancel);
                    ok =(Button)layout.findViewById(R.id.okButton);
                    ok.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            switch(motionEvent.getAction())
                            {
                                case MotionEvent.ACTION_DOWN:
                                    ok.setBackgroundResource(R.drawable.button_bg_off);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    ok.setBackgroundResource(R.drawable.button_bg_on);
                                    break;
                            }
                            return false;
                        }
                    });
                    // alert.setView(layout);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            password = ((EditText) layout.findViewById(R.id.loginpass)).getText().toString();
                            if (((CheckBox) layout.findViewById(R.id.autoLogin)).isChecked())
                                isAuto = true;
                            else
                                isAuto = false;

                            login = new login_connection(password, PhoneNumber);
                            login.start();
                            dialog_login.dismiss();

                        }
                    });
                    title_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog_login.dismiss();
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
                    //AlertDialog dialog = alert.create();
                    dialog_login = new Dialog(MainActivity.this);
                    dialog_login.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog_login.setCanceledOnTouchOutside(false);
                    dialog_login.setContentView(layout);
                    dialog_login.show();
                }
            });
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText edit_name, edit_pass, edit_age, edit_height, edit_weight;
                final RadioGroup rdg_sex;
                final RadioButton rdb_man, rdb_woman;
                final Button cancel, ok, title_cancle;
                ImageView imageview;
               /* AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);*/
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.register,null);
                image_Join = (ImageView) layout.findViewById(R.id.image);
                edit_name = (EditText) layout.findViewById(R.id.name);
                edit_pass = (EditText) layout.findViewById(R.id.password);
                edit_age = (EditText) layout.findViewById(R.id.age);
                edit_height = (EditText) layout.findViewById(R.id.height);
                edit_weight = (EditText) layout.findViewById(R.id.weight);
                rdg_sex = (RadioGroup) layout.findViewById(R.id.sex);
                rdb_man = (RadioButton) layout.findViewById(R.id.man);
                rdb_woman = (RadioButton) layout.findViewById(R.id.woman);
                cancel = (Button)layout.findViewById(R.id.reg_cancel);
                ok = (Button)layout.findViewById(R.id.reg_ok);
                title_cancle = (Button)layout.findViewById(R.id.title_cancel);

                title_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_register.dismiss();
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
                        name = edit_name.getText().toString();
                        password = edit_pass.getText().toString();
                        switch (rdg_sex.getCheckedRadioButtonId()) {
                            case R.id.man:
                                sex = rdb_man.getText().toString();
                                break;
                            case R.id.woman:
                                sex = rdb_woman.getText().toString();
                                break;
                            default:
                                sex = null;
                        }
                        age = edit_age.getText().toString();
                        height = edit_height.getText().toString();
                        weight = edit_weight.getText().toString();


                        if (name.length() == 0 || password.length() == 0 || sex == null || age.length() == 0 || height.length() == 0 || weight.length() == 0) {// null이 아니라 "" return

                            Toast.makeText(getApplicationContext(), "모든정보를 입력해주세요!", Toast.LENGTH_SHORT).show();


                        } else {


                            userInfo = new UserInfo(PhoneNumber, name, password, age, sex, weight, height, profile_bitmap);
                            resister = new resister_connection();
                            resister.start();
                            dialog_register.dismiss();
                        }
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
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_register.dismiss();

                    }
                });
                image_Join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        MainActivity.this.startActivityForResult(intent, 0);
                    }
                });

                /*alert.setView(layout);*/

                dialog_register = new Dialog(MainActivity.this);
                dialog_register.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_register.setCanceledOnTouchOutside(false);
                dialog_register.setContentView(layout);
                dialog_register.show();

                //  EnableHandler handler = new EnableHandler(dialog);

                /*thread = new EnableThread(layout, dialog, handler,PhoneNumber);


                thread.start();*/
            }
        });

        if (!isNetworkStat(this)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(false); // focus
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            alert.setMessage("네트워크가 연결되지 않았습니다. 연결 후 다시 실행하여 주십시오.");
            alert.show();
        }
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
    protected void onStop() {
        super.onStop();
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
               /* Bitmap circleBitmap = Bitmap.createBitmap(resize_bitmap.getWidth(), resize_bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                BitmapShader shader = new BitmapShader (resize_bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(circleBitmap.getWidth()/2, circleBitmap.getHeight()/2, circleBitmap.getWidth()/2, paint);*/
                isProfileImage = true;
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


    public static boolean isNetworkStat(Context context) // 네트워크 연결 여부 확인
    {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // WIFI
        NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX); // 4G LTE
        boolean blte_4g = false;
        if (lte_4g != null)
            blte_4g = lte_4g.isConnected();
        if (mobile != null) {
            if (mobile.isConnected() || wifi.isConnected() || blte_4g)
                return true;
        } else {
            if (wifi.isConnected() || blte_4g)
                return true;
        }

        return false;
    }


    public class resister_connection extends Thread {

        @Override
        public void run() {
            super.run();

            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/Join");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);


                Log.e("start", "start");
                Bitmap resize_bitmap=null;

                if(isProfileImage)
                    resize_bitmap = Bitmap.createScaledBitmap(profile_bitmap, 100, 100, true);
                else {
                    BitmapDrawable d = (BitmapDrawable)(image_Join.getDrawable());
                    Bitmap b = d.getBitmap();
                    resize_bitmap = Bitmap.createScaledBitmap(b, 100, 100, true);
                }

                isProfileImage = false;
                byte[] byteArray = bitmapToByteArray(resize_bitmap);
                String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));

                JSONObject json = new JSONObject();
                json.put("phonenum", userInfo.getPhonenum());
                json.put("nickname", userInfo.getNickname());
                json.put("password", userInfo.getPassword());
                json.put("age", userInfo.getAge());
                json.put("sex", userInfo.getSex());
                json.put("weight", userInfo.getWeight());
                json.put("height", userInfo.getHeight());
                json.put("image", imageString);

                Log.e("start", imageString);
                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                Log.e("start", "보냄");

                String response;

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("start", "OK");
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

                        mHandler.sendEmptyMessage(0);
                        contacts = new Contacts_Connection(PhoneNumber);
                        contacts.start();

                    }
                    else
                    {
                        mHandler.sendEmptyMessage(3);
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
                    Toast.makeText(getApplicationContext(), "회원가입 되었습니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "로그인 하였습니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다!!", Toast.LENGTH_SHORT).show();

                default:
                    break;
            }
        }

    };

    public class login_connection extends Thread {


        private String password = null;
        private String phonenum = null;

        public login_connection(String password, String phonenum) {
            this.password = password;
            this.phonenum = phonenum;
            progress.show();
        }

        @Override
        public void run() {
            super.run();

            try {
                HttpURLConnection conn = null;

                OutputStream os = null;

                ByteArrayOutputStream baos = null;

                URL url = new URL("http://202.30.23.64:8080/androidServlet/Login");
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
                json.put("password", password);


                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                // Log.e("start", "보냄");
                String response;

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //  Log.e("start", "OK");
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


                        isSuccess = true;
                        rank = new Rank_Connection();

                        rank.execute();


                    } else {
                        progress.dismiss();
                        mHandler.sendEmptyMessage(2);
                        isSuccess = false;

                    }


                } else {
                    Log.e("start", "실패");
                }

            } catch (Exception e) {

            }


        }

        public boolean isSuccess() {
            return isSuccess;
        }
    }

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

    public boolean isNumber(String str) {

        if (str == null)

            return false;

        Pattern p = Pattern.compile("([\\p{Digit}]+)(([.]?)([\\p{Digit}]+))?");

        Matcher m = p.matcher(str);

        return m.matches();

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

                if(isNumber(phonenumber))
                    contactlist.add(phonenumber);
            } while (contactCursor.moveToNext());
        }

        if(!contactlist.contains(Info.PHONE_NUM))
            contactlist.add(Info.PHONE_NUM);

        return contactlist;

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


            send_contacts();
            receive_rank(0, "getTotalRank");
            receive_rank(1, "getWalkRank");
            receive_rank(2, "getRunRank");
            receive_rank(3, "getSitupRank");
            receive_rank(4, "getPushupRank");
            receive_myInfo();
            SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("AutoLogin", isAuto);
            editor.putString("password", password);
            editor.commit();
            Log.e("못와",isAuto+"");
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            isSuccess = true;
            progress.dismiss();
            mHandler.sendEmptyMessage(1);
            intent = new Intent(getApplicationContext(), Health_MainActivity.class);

            startActivity(intent);
            finish();

        }


        public void send_contacts()
        {
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
                json.put("phonenum", Info.PHONE_NUM);

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
                        Info.User_info.setPassword(password);

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
