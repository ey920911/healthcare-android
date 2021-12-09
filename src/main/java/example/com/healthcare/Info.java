package example.com.healthcare;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import bean.UserInfo;
import bean.UserItem;
import knn.MyClassifyingClass;

/**
 * Created by Administrator on 2014-09-19.
 */
public class Info {


    public static String PHONE_NUM = "";
    public static UserInfo User_info = new UserInfo();
    public static ArrayList<UserItem> Rank_Total = new ArrayList<UserItem>();

    public static ArrayList<UserItem> Rank_Walk = new ArrayList<UserItem>();

    public static ArrayList<UserItem> Rank_Run = new ArrayList<UserItem>();

    public static ArrayList<UserItem> Rank_Situp = new ArrayList<UserItem>();

    public static ArrayList<UserItem> Rank_Pushup = new ArrayList<UserItem>();

    public static ConcurrentLinkedQueue<String[]> raw_data =  new ConcurrentLinkedQueue<String[]>();
    public static ConcurrentLinkedQueue<String[]> raw_data2 = new ConcurrentLinkedQueue<String[]>();
    public static MyClassifyingClass m_mcfcAcc = new MyClassifyingClass();
    public static MyClassifyingClass m_mcfcGyro = new MyClassifyingClass();


    public static double[] caloriePerTime = new double[4];

    public static void clear_rank(){

        Rank_Total.clear();
        Rank_Situp.clear();
        Rank_Run.clear();
        Rank_Walk.clear();
        Rank_Pushup.clear();
    }


}
