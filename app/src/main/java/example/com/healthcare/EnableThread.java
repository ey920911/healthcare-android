package example.com.healthcare;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import bean.UserInfo;
import example.com.R;

public class EnableThread extends Thread
{
    private View layout;
    private AlertDialog dialog;
    private EnableHandler handler;
    private String name;
    private String password;
    private String sex;
    private String age;
    private String height;
    private String weight;
    private static boolean flag;
    private UserInfo user = null;
    private String phoneNum= null;
    private EditText edit_name, edit_pass, edit_age, edit_height, edit_weight;
    private RadioGroup rdg_sex;
    private RadioButton rdb_man, rdb_woman;
    private  ImageView imageview;

    public EnableThread(View layout, AlertDialog dialog, EnableHandler handler,String phoneNum)
    {
        this.layout = layout;
        this.dialog = dialog;
        this.handler = handler;
        this.phoneNum = phoneNum;
        flag = true;
        edit_name = (EditText)layout.findViewById(R.id.name);
        edit_pass = (EditText)layout.findViewById(R.id.password);
        edit_age = (EditText)layout.findViewById(R.id.age);
        edit_height = (EditText)layout.findViewById(R.id.height);
        edit_weight = (EditText)layout.findViewById(R.id.weight);
        rdg_sex = (RadioGroup) layout.findViewById(R.id.sex);
        rdb_man = (RadioButton) layout.findViewById(R.id.man);
        rdb_woman = (RadioButton) layout.findViewById(R.id.woman);
        imageview = (ImageView)layout.findViewById(R.id.image);

    }

    public void run()
    {
        while(true)
        {
            name = edit_name.getText().toString();
            password = edit_pass.getText().toString();
            switch(rdg_sex.getCheckedRadioButtonId())
            {
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

            if(name.length() == 0 || password.length() == 0 || sex == null || age.length() == 0 || height.length() == 0 || weight.length() == 0) {// null이 아니라 "" return
                handler.sendEmptyMessage(0);

            }
            else {
                handler.sendEmptyMessage(1);
                BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
                final Bitmap imgbitmap = drawable.getBitmap();
                user = new UserInfo(phoneNum,name, password, age, sex,weight,height,imgbitmap);
                break;
            }
        }

    }

    public UserInfo getUser() {
        return user;
    }


}
