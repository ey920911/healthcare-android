package knn;

import android.util.Log;
import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * classifier 와 KNN 을 사용하여 테스트 데이터의 activity 가 무엇인지 판단하는 클래스이다.
 */
public class MyKNN {
  private int m_K;
  private String m_strClassifier;

  private ArrayList<String[]> m_strAccClassifierList;
  private ArrayList<String[]> m_strGyroClassifierList;

  public MyKNN() {
    m_strAccClassifierList = new ArrayList<String[]>();
    m_strGyroClassifierList = new ArrayList<String[]>();
  }

  public void clearLists() {
    Log.d("KNN list size", m_strAccClassifierList.size() + "");
    m_strAccClassifierList.clear();
    m_strGyroClassifierList.clear();
    Log.d("KNN list size", m_strAccClassifierList.size() + "");
  }

  public void setK(int a_K) {
    if (a_K < 1 || a_K % 2 == 0) {
      //error
    } else m_K = a_K;
  }/**setK();
   * KNN 알고리즘을 사용하기 위하여 K를 세팅한다.
   *
   */

  public void setGyroClassifierFromFile(BufferedReader br) {
    try {
      CSVReader reader = new CSVReader(br);

      reader.readNext(); //1번째 row 무시

      String[] nextLine = null;

      while ((nextLine = reader.readNext()) != null) {
        m_strGyroClassifierList.add(nextLine);
      }

      reader.close();
    } catch (IOException e) {
      Log.d("cannot open file", "y");
    }
  }

  public void setAccClassifierFromFile(BufferedReader br) {
    try {
      CSVReader reader = new CSVReader(br);

      reader.readNext(); //1번째 row 무시

      String[] nextLine = null;

      while ((nextLine = reader.readNext()) != null) {
        m_strAccClassifierList.add(nextLine);
      }

      reader.close();
    } catch (IOException e) {
      Log.d("cannot open file", "y");
    }
  }/**setClassifierFromFile();
   * 인자로 넘겨받은 classifier 파일의 경로를 이용하여 classifier 를 파일로 읽는다.
   * ArrayList 객체에 classifier 들을 add 한다.
   *
   */

  public void setAccTrainingClassifierFromFile(
    ArrayList<String> a_accTrainingClassifier
  ) {
    try {
      CSVReader reader;

      for (String path : a_accTrainingClassifier) {
        reader = new CSVReader(new FileReader(new File(path)));

        String[] nextLine = null;

        while ((nextLine = reader.readNext()) != null) {
          m_strAccClassifierList.add(nextLine);
        }
        reader.close();
      }
    } catch (IOException e) {
      Log.d("cannot open file", "y");
    }
  }

  public void setGyroTrainingClassifierFromFile(
    ArrayList<String> a_gyroTrainingClassifier
  ) {
    try {
      CSVReader reader;

      for (String path : a_gyroTrainingClassifier) {
        reader = new CSVReader(new FileReader(new File(path)));

        String[] nextLine = null;

        while ((nextLine = reader.readNext()) != null) {
          m_strGyroClassifierList.add(nextLine);
        }
        reader.close();
      }
    } catch (IOException e) {
      Log.d("cannot open file", "y");
    }
  }

  public int getLabel(double[] a_features) {
    int Label = 0;

    int labelOfClassifier = 0;
    double[] featuresOfClassifier = new double[96];
    double distance = 0;

    MyDistanceCalculator mdc = new MyDistanceCalculator();

    LinkedList<Integer> labelList = new LinkedList<Integer>();
    LinkedList<Double> distanceList = new LinkedList<Double>();
    //KNN start//

    String[] nextLine;

    for (int index = 0; index < m_strAccClassifierList.size(); index++) {
      nextLine = m_strAccClassifierList.get(index);

      labelOfClassifier = Integer.parseInt(nextLine[0]);
      for (int i = 0; i < 48; i++) {
        featuresOfClassifier[i] = Double.parseDouble(nextLine[i + 1]);
      }

      nextLine = m_strGyroClassifierList.get(index);

      for (int i = 48; i < 96; i++) {
        featuresOfClassifier[i] = Double.parseDouble(nextLine[i - 47]);
      }

      mdc.setTwoDoubleArrs(a_features, featuresOfClassifier);
      distance = mdc.getDistance();

      if (distanceList.size() > 0) {
        int i;
        for (i = 0; i < distanceList.size(); i++) {
          if (distanceList.get(i) < distance) {
            break;
          }
        }
        if (i != 0 || distanceList.size() <= m_K) {
          labelList.add(i, labelOfClassifier);
          distanceList.add(i, distance);
          if (distanceList.size() > m_K) {
            labelList.remove(0);
            distanceList.remove(0);
          }
        }
      } else if (distanceList.size() == 0) {
        labelList.add(labelOfClassifier);
        distanceList.add(distance);
      }
    }

    int LabelCandidates[] = new int[7];
    //LabelCandidates[0] = ACTIVITY_RUNNING
    //LabelCandidates[1] = ACTIVITY_WALKING
    //LabelCandidates[2] = ACTIVITY_PUSHUP
    //LabelCandidates[3] = ACTIVITY_SITUP

    LabelCandidates[0] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_RUNNING);
    LabelCandidates[1] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_WALKING);
    LabelCandidates[2] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_PUSHUP);
    LabelCandidates[3] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_SITUP);
    LabelCandidates[4] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_STAY_1);
    LabelCandidates[5] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_STAY_2);
    LabelCandidates[6] =
      Collections.frequency(labelList, finalStaticVariables.ACTIVITY_STAY_3);

    int maxLabelFreq = 0;
    int finalLabel = 1;

    //        Log.d("label","1 : RUNNING");
    //        Log.d("label","2 : WALKING");
    //        Log.d("label","3 : PUSHUP");
    //        Log.d("label","4 : SITUP");

    for (int i = 0; i < LabelCandidates.length; i++) {
      //            Log.d("label, freq", (i + 1) + ", " + LabelCandidates[i]);

      if (LabelCandidates[i] > maxLabelFreq) {
        finalLabel = i + 1;
        maxLabelFreq = LabelCandidates[i];
      }
    }
    Label = finalLabel;
    //KNN end//
    return Label;
  }/**getLabel();
   * ClassifierList 의 classifier 와 인자로 넘겨받은 feature 와의 distance 를 구하고, distance 가 낮은 K 개의 label 을 저장한다.
   * K 개의 label 중 많은 것을 선정하여, 최종 label 로 판단한다. 그 label 을 리턴한다.
   */
  /**
    * Plan :
    * file open 하고 윈도우로 자르는건 우연이가 한다.
    * 그 다음 윈도우로 자른 것을 MyFeatureExtractor 클래스로 feature extracting 하고, label 붙여서 csv 파일로 따로 저장한다.

    * 이 클래스에서는 labeling 한 feature 들이 담긴 csv 파일을 연다.
    * test data 가 들어오면 윈도우로 자르고, 그것을 feature extracting 한 다음에, MyDistanceCalculator 클래스로
    * labeled data 와 distance 를 구한다.
    * 가장 distance 가 짧은 K개의 label 들을 K개의 배열에 모아놓고, distance 계산이 모두 완료되면 label 숫자가 많은 것을 test data 의 label 로 한다.
    */
}
