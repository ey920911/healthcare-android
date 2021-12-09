package bean;

import android.graphics.Bitmap;

/**
 * Created by Admin on 2014-09-10.
 */
public class UserItem {
    private String phonenum;
    private int score;
    private String nickname;
    private Bitmap image;
    public UserItem(String nickname, int score, Bitmap image,String phonenum)
    {
        setScore(score);
        setNickname(nickname);
        setImage(image);
        setPhonenum(phonenum);
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
}
