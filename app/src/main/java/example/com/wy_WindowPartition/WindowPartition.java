package example.com.wy_WindowPartition;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import knn.MyFeatureExtractor;
import knn.finalStaticVariables;

/**
 *
 * training data csv 파일을 window size 로 partitioning 하며, feature 를 구하고, label 을 붙여 classifier 를 csv 파일로 저장하는 클래스이다.
 */

public class WindowPartition {
  private File[] folderList;
  private MyFileReader mMyFileReader;
  private LinkedList<CSV_DataUnit> mDividedData;
  CSV_DataUnit[] mUnits;
  int mUnitPos;
  boolean isOldUnitActive;

  MyFeatureExtractor mfe = new MyFeatureExtractor(new Double[0]);

  ///////////////////////////////////////////////////
  int m_dLabel = finalStaticVariables.ACTIVITY_SITUP;
  ///////////////////////////////////////////////////

  String m_filePath =
    finalStaticVariables.DIR_FOLDER + finalStaticVariables.ACTIVITY + "/";

  String m_strAccFeaturePath = m_filePath + "AccFeatureExtraction.csv";
  String m_strGyroFeaturePath = m_filePath + "GyroFeatureExtraction.csv";

  public MyFileReader getMyFileReader() {
    return mMyFileReader;
  }

  public void init() {
    mUnitPos = 0;
    isOldUnitActive = false;
    mUnits = new CSV_DataUnit[2];
    mUnits[0] = new CSV_DataUnit();
    mUnits[1] = new CSV_DataUnit();
    mDividedData = new LinkedList<CSV_DataUnit>();
    mMyFileReader = new MyFileReader();
    File dirFile = new File(m_filePath);
    folderList = dirFile.listFiles();
    for (File tempFile : folderList) {
      String tempPath = tempFile.getParent();
      String tempFileName = tempFile.getName();
      finalStaticVariables.debugPrintln(
        "Path=" + tempPath + "/" + tempFileName
      );
      String filename = tempFile.getName();
      if (!filename.contains(".")) {
        mMyFileReader.setFolder(tempPath + "/" + tempFileName + "/");
        mMyFileReader.setFilePathList();
      }
    }
    if (mMyFileReader.getCSVList().size() < 1) {
      finalStaticVariables.debugPrintln("can not find any csv files");
      System.exit(1);
    }
  }

  public ArrayList<String> getCSVList() {
    return mMyFileReader.getCSVList();
  }

  public LinkedList getDividedData(int msWindowArea, int msDuplicatedArea) {
    try {
      divideCSVdata(msWindowArea, msDuplicatedArea);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return mDividedData;
  }

  public Double[] getCSVdata(
    int type,
    int direction,
    int msWindowArea,
    int msDuplicatedArea
  ) {
    if (mUnits[mUnitPos] != null) {
      if (finalStaticVariables.DEBUG) {
        mUnits[mUnitPos].printUnits(finalStaticVariables.ALL_DATA_TYPE);
      }
      switch (direction) {
        case finalStaticVariables.X_DIRECTION:
          if (
            type == finalStaticVariables.ACCELERATE_DATA_TYPE
          ) return (Double[]) mUnits[mUnitPos].getAccX().toArray(new Double[0]); // List ����� �����͸� Array ����� �����ͷ� ��ȯ
          else if (
            type == finalStaticVariables.GYROSCOPE_DATA_TYPE
          ) return (Double[]) mUnits[mUnitPos].getGyroX()
            .toArray(new Double[0]); // List ����� �����͸� Array ����� �����ͷ� ��ȯ
        case finalStaticVariables.Y_DIRECTION:
          if (
            type == finalStaticVariables.ACCELERATE_DATA_TYPE
          ) return (Double[]) mUnits[mUnitPos].getAccY().toArray(new Double[0]); // List ����� �����͸� Array ����� �����ͷ� ��ȯ
          else if (
            type == finalStaticVariables.GYROSCOPE_DATA_TYPE
          ) return (Double[]) mUnits[mUnitPos].getGyroY()
            .toArray(new Double[0]); // List ����� �����͸� Array ����� �����ͷ� ��ȯ
        case finalStaticVariables.Z_DIRECTION:
          if (
            type == finalStaticVariables.ACCELERATE_DATA_TYPE
          ) return (Double[]) mUnits[mUnitPos].getAccZ().toArray(new Double[0]); // List ����� �����͸� Array ����� �����ͷ� ��ȯ
          else if (
            type == finalStaticVariables.GYROSCOPE_DATA_TYPE
          ) return (Double[]) mUnits[mUnitPos].getGyroZ()
            .toArray(new Double[0]); // List ����� �����͸� Array ����� �����ͷ� ��ȯ
        default:
          System.out.println("undefined direction is come : " + direction);
      }
      return null;
    } else {
      System.out.println("mUnits is null please check.");
      return null;
    }
  }

  public void divideCSVdata(int msWindowArea, int msDuplicatedArea)
    throws IOException {
    ArrayList<String> mPaths = mMyFileReader.getCSVList();
    String[] nextLine;

    //JH Code START
    File AccFeatureFile = new File(m_strAccFeaturePath);
    File GyroFeatureFile = new File(m_strGyroFeaturePath);

    String strFeaturesColumnNames =
      "Label," +
      "X_max,X_min,X_Aver,X_Median,X_Variance,X_StandardDeviation,X_MedianAbsoluteDeviation,X_RootMeanSquare," +
      "Y_max,Y_min,Y_Aver,Y_Median,Y_Variance,Y_StandardDeviation,Y_MedianAbsoluteDeviation,Y_RootMeanSquare," +
      "Z_max,Z_min,Z_Aver,Z_Median,Z_Variance,Z_StandardDeviation,Z_MedianAbsoluteDeviation,Z_RootMeanSquare," +
      "X-Y_max,X-Y_min,X-Y_Aver,X-Y_Median,X-Y_Variance,X-Y_StandardDeviation,X-Y_MedianAbsoluteDeviation,X-Y_RootMeanSquare," +
      "Y-Z_max,Y-Z_min,Y-Z_Aver,Y-Z_Median,Y-Z_Variance,Y-Z_StandardDeviation,Y-Z_MedianAbsoluteDeviation,Y-Z_RootMeanSquare," +
      "Z-X_max,Z-X_min,Z-X_Aver,Z-X_Median,Z-X_Variance,Z-X_StandardDeviation,Z-X_MedianAbsoluteDeviation,Z-X_RootMeanSquare\n";

    FileOutputStream AccFOS = new FileOutputStream(AccFeatureFile);
    FileOutputStream GyroFOS = new FileOutputStream(GyroFeatureFile);
    BufferedWriter AccBW = new BufferedWriter(new OutputStreamWriter(AccFOS));
    BufferedWriter GyroBW = new BufferedWriter(new OutputStreamWriter(GyroFOS));

    AccBW.write(strFeaturesColumnNames);
    GyroBW.write(strFeaturesColumnNames);

    //JH Code END
    for (String path : mPaths) {
      int flagNew = 0;
      boolean flag = false;
      mUnits[0].clean();
      mUnits[1].clean();

      boolean isAcc = true;
      @SuppressWarnings("resource")
      CSVReader reader = new CSVReader(new FileReader(new File(path)));

      while ((nextLine = reader.readNext()) != null) {
        // X, Y, Z, Time
        if (nextLine[0].length() < 3 || nextLine[0].equals("X")) {
          continue;
        }
        int nUnitPos = (mUnitPos + 1) % 2; //new array pos
        int namePos =
          path.length() - finalStaticVariables.NAME_POSITION_FROM_BACK;
        //					System.out.println("l:"+path.length()+" , u.pfb: "+ Utils.NAME_POSITION_FROM_BACK+ ", r : "+(path.length()-Utils.NAME_POSITION_FROM_BACK));
        //					System.out.println(path.substring(path.length() - Utils.NAME_POSITION_FROM_BACK, path.length() - Utils.NAME_POSITION_FROM_BACK + Utils.NAME_LENTH));

        double x = Double.parseDouble((String) nextLine[0]);
        double y = Double.parseDouble((String) nextLine[1]);
        double z = Double.parseDouble((String) nextLine[2]);
        int t = Integer.parseInt((String) nextLine[3]);

        finalStaticVariables.debugPrintln(
          nextLine[0] +
          "\t|\t" +
          nextLine[1] +
          "\t|\t" +
          nextLine[2] +
          "\t|\t" +
          nextLine[3] +
          "\n"
        );

        if (
          path
            .substring(namePos, namePos + finalStaticVariables.NAME_LENTH)
            .equals(finalStaticVariables.NAME_ACCE_9)
        ) {
          isAcc = true;
          mUnits[mUnitPos].addAccelerateData(x, y, z, t);
          if (finalStaticVariables.DEBUG) {
            System.out.print("new[" + mUnitPos + "] one: \t");
            mUnits[mUnitPos].printUnits(
                finalStaticVariables.ACCELERATE_DATA_TYPE
              );
          }
          if (isOldUnitActive) {
            mUnits[nUnitPos].addAccelerateData(x, y, z, t);
            if (finalStaticVariables.DEBUG) {
              System.out.print("old[" + nUnitPos + "] one: \t");
              mUnits[nUnitPos].printUnits(
                  finalStaticVariables.ACCELERATE_DATA_TYPE
                );
            }
          }
        } else if (
          path
            .substring(namePos, namePos + finalStaticVariables.NAME_LENTH)
            .equals(finalStaticVariables.NAME_GYRO_9)
        ) {
          isAcc = false;
          mUnits[mUnitPos].addGyroscopeData(x, y, z, t);
          if (finalStaticVariables.DEBUG) {
            System.out.print("new[" + mUnitPos + "] one: \t");
            mUnits[mUnitPos].printUnits(
                finalStaticVariables.GYROSCOPE_DATA_TYPE
              );
          }
          if (isOldUnitActive) {
            mUnits[nUnitPos].addGyroscopeData(x, y, z, t);
            if (finalStaticVariables.DEBUG) {
              System.out.print("old[" + nUnitPos + "] one: \t");
              mUnits[nUnitPos].printUnits(
                  finalStaticVariables.GYROSCOPE_DATA_TYPE
                );
            }
          }
        } else {
          System.out.println(
            "Can't know :" +
            path.substring(namePos, namePos + finalStaticVariables.NAME_LENTH)
          );
        }

        if (t / (msWindowArea - msDuplicatedArea) > flagNew) {
          flag = true;
          //switch the input array (start new array and ready for end old array)
          mUnitPos = (mUnitPos + 1) % 2; //mUnitPos == 0 ? 1 : 0;
          isOldUnitActive = true;
          finalStaticVariables.debugPrintln(
            "new one( mUnits[" +
            mUnitPos +
            "] begin.  " +
            t /
            (msWindowArea - msDuplicatedArea) +
            ">" +
            flagNew
          );
          flagNew = t / (msWindowArea - msDuplicatedArea);
        } else if (
          flag && t > flagNew * (msWindowArea - msDuplicatedArea) + 500
        ) {
          finalStaticVariables.debugPrintln(
            "old one( mUnits[" + nUnitPos + "] end."
          );
          flag = false;
          isOldUnitActive = false;
          //End in here.
          //JH Code START
          Double[] doubleArrayX = null;
          Double[] doubleArrayY = null;
          Double[] doubleArrayZ = null;

          if (isAcc) {
            doubleArrayX =
              (Double[]) mUnits[nUnitPos].getAccX().toArray(new Double[0]);
            doubleArrayY =
              (Double[]) mUnits[nUnitPos].getAccY().toArray(new Double[0]);
            doubleArrayZ =
              (Double[]) mUnits[nUnitPos].getAccZ().toArray(new Double[0]);

            mfe.setDoubleArr(doubleArrayX); //MyFeatureExtractor 객체에 데이터 Array 전달
            double[] features = mfe.getFeatures();
            AccBW.write(
              m_dLabel +
              "," +
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayY);
            features = mfe.getFeatures();
            AccBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayZ);
            features = mfe.getFeatures();
            AccBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            int arrLength = doubleArrayX.length;

            Double[] doubleArrayXSubY = new Double[arrLength];

            for (int i = 0; i < arrLength; i++) doubleArrayXSubY[i] =
              doubleArrayX[i] - doubleArrayY[i]; //X-Y array

            Double[] doubleArrayYSubZ = new Double[arrLength];

            for (int i = 0; i < arrLength; i++) doubleArrayYSubZ[i] =
              doubleArrayY[i] - doubleArrayZ[i]; //Y-Z array

            Double[] doubleArrayZSubX = new Double[arrLength];

            for (int i = 0; i < arrLength; i++) doubleArrayZSubX[i] =
              doubleArrayZ[i] - doubleArrayX[i]; //Z-X array

            mfe.setDoubleArr(doubleArrayXSubY);
            features = mfe.getFeatures();
            AccBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayYSubZ);
            features = mfe.getFeatures();
            AccBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayZSubX);
            features = mfe.getFeatures();
            AccBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              "\n"
            );
          } else {
            doubleArrayX =
              (Double[]) mUnits[nUnitPos].getGyroX().toArray(new Double[0]);
            doubleArrayY =
              (Double[]) mUnits[nUnitPos].getGyroY().toArray(new Double[0]);
            doubleArrayZ =
              (Double[]) mUnits[nUnitPos].getGyroZ().toArray(new Double[0]);

            mfe.setDoubleArr(doubleArrayX); //MyFeatureExtractor 객체에 데이터 Array 전달
            double[] features = mfe.getFeatures();
            GyroBW.write(
              m_dLabel +
              "," +
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayY);
            features = mfe.getFeatures();
            GyroBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayZ);
            features = mfe.getFeatures();
            GyroBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            int arrLength = doubleArrayX.length;

            Double[] doubleArrayXSubY = new Double[arrLength];

            for (int i = 0; i < doubleArrayX.length; i++) doubleArrayXSubY[i] =
              doubleArrayX[i] - doubleArrayY[i]; //X-Y array

            Double[] doubleArrayYSubZ = new Double[arrLength];

            for (int i = 0; i < doubleArrayX.length; i++) doubleArrayYSubZ[i] =
              doubleArrayY[i] - doubleArrayZ[i]; //Y-Z array

            Double[] doubleArrayZSubX = new Double[arrLength];

            for (int i = 0; i < doubleArrayX.length; i++) doubleArrayZSubX[i] =
              doubleArrayZ[i] - doubleArrayX[i]; //Z-X array

            mfe.setDoubleArr(doubleArrayXSubY);
            features = mfe.getFeatures();
            GyroBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayYSubZ);
            features = mfe.getFeatures();
            GyroBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              ","
            );

            mfe.setDoubleArr(doubleArrayZSubX);
            features = mfe.getFeatures();
            GyroBW.write(
              features[0] +
              "," +
              features[1] +
              "," +
              features[2] +
              "," +
              features[3] +
              "," +
              features[4] +
              "," +
              features[5] +
              "," +
              features[6] +
              "," +
              features[7] +
              "\n"
            );
          }

          //JH Code END
          mUnits[nUnitPos].clean();
        }
      }

      reader.close();
    }

    AccBW.close();
    GyroBW.close();
    AccFOS.close();
    GyroFOS.close();
  }/** divideCSVdata();
   * 아무래도 training csv 파일로 부터 읽어들인 데이터를 window size 로 partitioning 해서(잘라서) feature extracting 을 하는 것 같다. (추측)
   * 아무래도 feature extracting 해서 feature 에 label 을 붙이고 classifier 를 만드는 메서드 인 것 같다. (추측2)
   * 아무래도 classifier 를 file 로 write 하는 것 같다. (추측3)
   */

  public void printCSVfile(File CSVfile) {
    String[] nextLine;
    try {
      @SuppressWarnings("resource")
      CSVReader reader = new CSVReader(new FileReader(CSVfile));
      while ((nextLine = reader.readNext()) != null) {
        System.out.println(
          nextLine[0] +
          "\t|\t" +
          nextLine[1] +
          "\t|\t" +
          nextLine[2] +
          "\t|\t" +
          nextLine[3] +
          "\n"
        );
      }
    } catch (IOException e) {
      System.out.println("cannot open file");
    }
  }
}
