package knn;

import android.os.Environment;

/**
 * 각종 필수적인 상수들이 정의되어 있는 클래스이다.
 */

public class finalStaticVariables {
  public static final boolean DEBUG = false;
  //DEBUG MODE

  public static final int WIN_AREA_MS = 1500; //area for window (ms)
  public static final int DUP_AREA_MS = 500; //area for duplicated (ms)

  //	public final static String DIR_FOLDER = "C:/Users/JoWooyeon/Documents/PushUp/";
  public static final String DIR_FOLDER =
    Environment.getExternalStorageDirectory() + "/Health Care/";
  public static final String ACTIVITY = "PUSH_UP";

  //for reading csv files.
  public static final int NAME_POSITION_FROM_BACK = 13; //string size + .csv
  public static final int NAME_LENTH = 9; //string size
  public static final String NAME_GYRO_9 = "gyroscope";
  public static final String NAME_ACCE_9 = "ccelation";

  public static final int X_DIRECTION = 1;
  public static final int Y_DIRECTION = 2;
  public static final int Z_DIRECTION = 3;
  public static final int T_DIRECTION = 4;

  public static final int ALL_DATA_TYPE = 100;
  public static final int ACCELERATE_DATA_TYPE = 101;
  public static final int GYROSCOPE_DATA_TYPE = 102;

  //for labeling
  public static final int ACTIVITY_RUNNING = 1;
  public static final int ACTIVITY_WALKING = 2;
  public static final int ACTIVITY_PUSHUP = 3;
  public static final int ACTIVITY_SITUP = 4;
  public static final int ACTIVITY_STAY_1 = 5;
  public static final int ACTIVITY_STAY_2 = 6;
  public static final int ACTIVITY_STAY_3 = 7;

  public static void debugPrintln(String printMsg) {
    if (DEBUG) System.out.println(printMsg);
  }
}
