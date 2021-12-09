package knn;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * 테스트 파일로부터 feature 를 extracting 하고, KNN 알고리즘을 이용하여 activity 를 판단한다.
 */
public class MyClassifyingClass {

    private MyFeatureExtractor mfe = new MyFeatureExtractor(new Double[0]);

    private MyKNN m_mK;

    private ArrayList<Double> m_doubleListX;
    private ArrayList<Double> m_doubleListY;
    private ArrayList<Double> m_doubleListZ;



    public MyClassifyingClass(){
        m_mK = new MyKNN();
        m_doubleListX = new ArrayList<Double>();
        m_doubleListY = new ArrayList<Double>();
        m_doubleListZ = new ArrayList<Double>();
       // mMyFileReader = new MyFileReader();
    }/**MyClassifyingClass();
    * MyKNN 객체를 초기화하고, ArrayList 들을 초기화한다. MyFileReader 객체도 초기화한다.
    * */

    public void clearListsInKNN(){
        m_mK.clearLists();
    }

    public void setTestDataPath (String a_strTestDataPath)
    {
//        m_strTestDataPath = a_strTestDataPath;
        //mMyFileReader.setFolder(a_strTestDataPath);
    }/** setTestDataPath();
    * MyFileReader 객체의 setFolder 메서드를 실행하여 테스트 데이터가 있는 디렉터리를 설정한다.
    * */

  /*  public ArrayList<String> ClassifyingTestCSV() {


        mMyFileReader.getFilePathList();
        ArrayList<String> filePathList = mMyFileReader.getCSVList();
        ArrayList<String> labels = new ArrayList<String>();

        for (String filePath : filePathList) {
//            Log.d("filepath",filePath);
            File testFile = new File(filePath);
            String[] nextLine;

            try {
                CSVReader reader = new CSVReader(new FileReader(testFile));

                while ((nextLine = reader.readNext()) != null) //read 시작
                {
                  //  Log.d("is double?",nextLine[0]);
                    m_doubleListX.add(Double.parseDouble(nextLine[0]));
                    m_doubleListY.add(Double.parseDouble(nextLine[1]));
                    m_doubleListZ.add(Double.parseDouble(nextLine[2])); //String으로 가져온 값을 Double로 변환하여 List에 저장
                }

                reader.close();

                double[] features = getFeatures();

                int Label = m_mK.getLabel(features);

                switch (Label) {
                    case finalStaticVariables.ACTIVITY_RUNNING:
                        labels.add("RUNNING\n");
                        break;
                    case finalStaticVariables.ACTIVITY_WALKING:
                        labels.add("WALKING\n");
                        break;
                    case finalStaticVariables.ACTIVITY_PUSHUP:
                        labels.add("PUSHUP\n");
                        break;
                    case finalStaticVariables.ACTIVITY_SITUP:
                        labels.add("SITUP\n");
                        break;
                }


            } catch (IOException e) {
                Log.d("cannot open file", "y");
            }

        }

        return labels;
    }*//** ClassifyingTestCSV();
    * MyFileReader 객체로부터 테스트 파일 목록을 불러와서 그 테스트파일들을 각각 feature extracting 한 다음에
    * KNN 을 사용하여 Classifying 하는 클래스이다.
    * 테스트파일마다 Classifying 하여 그것의 Label 을 ArrayList 에 add 하고, 테스트파일들의 classifying 이 완료되면
    * ArrayList 를 리턴한다.
    * */

    public int classify_Test(double[] a_doubleArrFeatureOfTwoSens)
    {
        int Label = m_mK.getLabel(a_doubleArrFeatureOfTwoSens);
        return Label;
    }



    public boolean setKOfKNN (int a_K){
        if (m_mK!=null) {
            m_mK.setK(a_K);
            return true;
        }
        else
            return false;
    }/** setKOfKNN();
    * MyKNN 객체의 K값을 세팅한다.
    *
    */

    public boolean setGyroClassifierFromFile (BufferedReader br){
        if (m_mK!=null) {
            m_mK.setGyroClassifierFromFile(br);
            return true;
        }
        else
            return false;
    }

    public boolean setAccClassifierFromFile (BufferedReader br){
        if (m_mK!=null) {
            m_mK.setAccClassifierFromFile(br);
            return true;
        }
        else
            return false;
    }/** setClassifierFromFile();
    * MyKNN 객체의 Classifier 파일의 경로를 세팅한다.
    *
    */

    public void clear_array()
    {
        m_doubleListX.clear();
        m_doubleListY.clear();
        m_doubleListZ.clear();
    }

    public boolean setAccTrainingClassifierFromFile (ArrayList<String> a_accTrainingClassifier)
    {
        if(m_mK!=null)
        {
            m_mK.setAccTrainingClassifierFromFile(a_accTrainingClassifier);
            return true;
        }
        else
            return false;
    }

    public boolean setGyroTrainingClassifierFromFile (ArrayList<String> a_gyroTrainingClassifier)
    {
        if(m_mK!=null)
        {
            m_mK.setGyroTrainingClassifierFromFile(a_gyroTrainingClassifier);
            return true;
        }
        else
            return false;
    }

    public double[] getFeatures (){

        double features[] = new double[48];
        int index = 0;

        Double[] doubleArrayAccX = (Double[])m_doubleListX.toArray(new Double[0]); //List 형식의 데이터를 Array 형식의 데이터로 변환

        mfe.setDoubleArr(doubleArrayAccX); //MyFeatureExtractor 객체 생성, 데이터 Array 전달

        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }

        Double[] doubleArrayAccY = (Double[])m_doubleListY.toArray(new Double[0]);

        mfe.setDoubleArr(doubleArrayAccY);

        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }


        Double[] doubleArrayAccZ = (Double[])m_doubleListZ.toArray(new Double[0]);

        mfe.setDoubleArr(doubleArrayAccZ);

        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }

        int arrLength = doubleArrayAccX.length;

        Double[] doubleArrXSubY = new Double[arrLength];

        for (int i = 0 ; i< arrLength ; i++)
            doubleArrXSubY[i]=doubleArrayAccX[i]-doubleArrayAccY[i]; //X-Y array


        Double[] doubleArrYSubZ = new Double[arrLength];

        for (int i = 0 ; i<arrLength ; i++)
            doubleArrYSubZ[i]=doubleArrayAccY[i]-doubleArrayAccZ[i]; //Y-Z array

        Double[] doubleArrZSubX = new Double[arrLength];

        for (int i = 0 ; i<arrLength ; i++)
            doubleArrZSubX[i]=doubleArrayAccZ[i]-doubleArrayAccX[i]; //Z-X array

        mfe.setDoubleArr(doubleArrXSubY);
        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }


        mfe.setDoubleArr(doubleArrYSubZ);
        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }


        mfe.setDoubleArr(doubleArrZSubX);
        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }

        return features;
    }/** getFeatures();
    * MyFeatureExtractor 클래스로 테스트 파일로부터 읽어온 값의 feature extraction 을 한다.
    * feature 는 48개이다.
    */


    public ArrayList<Double> getM_doubleListX() {
        return m_doubleListX;
    }

    public void addM_doubleListX(Double m_doubleListX) {
        this.m_doubleListX.add(m_doubleListX);
    }

    public ArrayList<Double> getM_doubleListY() {
        return m_doubleListY;
    }

    public void addM_doubleListY(Double m_doubleListY) {
        this.m_doubleListY.add(m_doubleListY);
    }

    public ArrayList<Double> getM_doubleListZ() {
        return m_doubleListZ;
    }

    public void addM_doubleListZ(Double m_doubleListZ) {
        this.m_doubleListZ.add(m_doubleListZ);
    }
}
