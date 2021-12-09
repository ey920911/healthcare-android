package example.com.healthcare;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.R;

public class DetectHandler extends Handler //
{
    private TextView exercise;
    private TextView scoreText;////
    private TextView totalScoreText;////
    private ImageView detect_image;

    private double calorie;////
    private double totalCalorie;////

    public DetectHandler(TextView exercise, TextView scoreText, TextView totalScoreText,ImageView detect_image)////
    {
        this.exercise = exercise;
        this.scoreText = scoreText;////
        this.totalScoreText = totalScoreText;////
        this.detect_image = detect_image;
        calorie = 0;////
        totalCalorie = (double) Info.User_info.getTotal();////db서 가져와야함
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        double temp =0.0;
        switch(msg.what)
        {
            case 1:
                exercise.setText("RUNNING");
                detect_image.setImageResource(R.drawable.running_image);
                if(Info.User_info.getSex().equals("Man"))////
                {
                    temp = 0.232 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30;
                    Info.caloriePerTime[msg.what - 1] += temp; ////
                }
                else////
                {
                    temp = 0.214 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }


                break;
            case 2:
                exercise.setText("WALKING");
                detect_image.setImageResource(R.drawable.walking_image);
                if(Info.User_info.getSex().equals("Man"))////
                {
                    temp = 0.057 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }
                else////
                {
                    temp =  0.053 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }


                break;
            case 3:
                exercise.setText("PUSHUP");
                detect_image.setImageResource(R.drawable.pressup_image);
                if(Info.User_info.getSex().equals("Man"))////
                {
                    temp = 0.570 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }
                else////
                {
                    temp = 0.525 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }


                break;
            case 4:
                exercise.setText("SITUP");
                detect_image.setImageResource(R.drawable.situp_image);
                if(Info.User_info.getSex().equals("Man"))////
                {
                    temp = 0.462 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }
                else////
                {
                    temp = 0.429 * Double.parseDouble(Info.User_info.getWeight()) * 1 / 30; ////
                    Info.caloriePerTime[msg.what - 1] += temp;
                }


                break;
            case 5:
                exercise.setText("STAY");
                detect_image.setImageResource(R.drawable.walking_image);
            case 6:

                detect_image.setImageResource(R.drawable.walking_image);
                exercise.setText("STAY");
            case 7:

                detect_image.setImageResource(R.drawable.walking_image);
                exercise.setText("STAY");
                break;
            case 100: //// 종료버튼
                calorie = 0;////
                break;////
        }
        calorie  +=temp;
        totalCalorie +=temp;
        scoreText.setText(((Integer)(int)calorie).toString());////
        totalScoreText.setText(((Integer)(int)totalCalorie).toString());////
    }
}
