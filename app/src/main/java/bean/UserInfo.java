package bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2014-09-13.
 */
public class UserInfo{

    private String phonenum;
    private String password;
    private String nickname;
    private String age;
    private String sex;
    private String weight;
    private String height;
    private Bitmap image;
    private int total;
    private int walk;
    private int run;
    private int pushup;
    private int situp;
    public UserInfo(){

    }
    public UserInfo(String phone, String nickname, String pass, String age, String sex, String weight, String height, Bitmap image)
    {
        this.phonenum = phone;
        this.nickname = nickname;
        this.password = pass;
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.image = image;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWalk() {
        return walk;
    }

    public void setWalk(int walk) {
        this.walk = walk;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getPushup() {
        return pushup;
    }

    public void setPushup(int pushup) {
        this.pushup = pushup;
    }

    public int getSitup() {
        return situp;
    }

    public void setSitup(int situp) {
        this.situp = situp;
    }
}
