package example.com.customtrainingactivity;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import knn.MyFeatureExtractor;

public class MakeClassifier extends Thread {
	private boolean isAlive = false;

    private int m_label;
    private String m_strActivity;
    private double[][] m_accFeatures;
    private double[][] m_gyroFeatures;
    private MyFeatureExtractor mfe;

	public MakeClassifier(String a_strActivity) {
		super();

        if(a_strActivity.equals("Running"))
            m_label=1;
        else if (a_strActivity.equals("Walking"))
            m_label=2;
        else if(a_strActivity.equals("PushUp"))
            m_label=3;
        else if(a_strActivity.equals("SitUp"))
            m_label=4;

        m_strActivity=a_strActivity;

        m_accFeatures = new double[3][];
        m_accFeatures[0] = new double[48];
        m_accFeatures[1] = new double[48];
        m_accFeatures[2] = new double[48];
        m_gyroFeatures = new double[3][];
        m_gyroFeatures[0] = new double[48];
        m_gyroFeatures[1] = new double[48];
        m_gyroFeatures[2] = new double[48];

	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
        Log.d("write classifier" , "start");
		isAlive = true;

        int accSensCount = 0;
        int gyroSensCount = 0;
        
        int accTrainingCount = 0;
        int gyroTrainingCount = 0;

         double[] rawAccSensingDataX = new double[100];
         double[] rawAccSensingDataY = new double[100];
         double[] rawAccSensingDataZ = new double[100];
         double[] rawGyroSensingDataX = new double[100];
         double[] rawGyroSensingDataY = new double[100];
         double[] rawGyroSensingDataZ = new double[100];
        
		while (isAlive) {
			if(accTrainingCount>2 && gyroTrainingCount>2){
                isAlive=false;
                break;
            }
			if (!CollectedSensingData.raw_data.isEmpty() && accTrainingCount<3) {

				double[] sensData = CollectedSensingData.raw_data.poll();
                rawAccSensingDataX[accSensCount] = sensData[0];
                rawAccSensingDataY[accSensCount] = sensData[1];
                rawAccSensingDataZ[accSensCount] = sensData[2];

                if (accSensCount==99)
                {
                    m_accFeatures[accTrainingCount] = getFeatures(rawAccSensingDataX, rawAccSensingDataY,rawAccSensingDataZ);

                    rawAccSensingDataX = new double[100];
                    rawAccSensingDataY = new double[100];
                    rawAccSensingDataZ = new double[100];


                    Log.d("accTrainingCount",accTrainingCount+"");
                    accTrainingCount++;
                    accSensCount=-1;
                }

                accSensCount++;
			}
			if (!CollectedSensingData.raw_data2.isEmpty() && gyroTrainingCount<3) {

                double[] sensData = CollectedSensingData.raw_data2.poll();
                rawGyroSensingDataX[gyroSensCount] = sensData[0];
                rawGyroSensingDataY[gyroSensCount] = sensData[1];
                rawGyroSensingDataZ[gyroSensCount] = sensData[2];

                if (gyroSensCount==99)
                {
                    m_gyroFeatures[gyroTrainingCount] = getFeatures(rawGyroSensingDataX, rawGyroSensingDataY,rawGyroSensingDataZ);

                    rawGyroSensingDataX = new double[100];
                    rawGyroSensingDataY = new double[100];
                    rawGyroSensingDataZ = new double[100];

                    Log.d("gyroTrainingCount",gyroTrainingCount+"");
                    gyroSensCount=-1;
                    gyroTrainingCount++;
                }

                gyroSensCount++;
			}			
		}

        writeClassifierFile(m_label,m_strActivity,"AccClassifier",m_accFeatures);
        writeClassifierFile(m_label,m_strActivity,"GyroClassifier",m_gyroFeatures);

        Log.d("write classifier" , "end");
	}

    public double[] getFeatures (double[] rawDataX,double[] rawDataY,double[] rawDataZ){

        int index = 0;
        double[] featuresForAllElements = new double[48];

        mfe = new MyFeatureExtractor(rawDataX);
        double[] features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(rawDataY);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(rawDataZ);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        double[] XsubY = new double[100];
        for (int i = 0; i < 100 ; i++)
        {
            XsubY[i] = rawDataX[i] - rawDataY[i];
        }

        double[] YsubZ = new double[100];
        for (int i = 0; i < 100 ; i++)
        {
            YsubZ[i] = rawDataY[i] - rawDataZ[i];
        }

        double[] ZsubX = new double[100];
        for (int i = 0; i < 100 ; i++)
        {
            ZsubX[i] = rawDataZ[i] - rawDataX[i];
        }

        mfe = new MyFeatureExtractor(XsubY);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(YsubZ);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(ZsubX);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        return featuresForAllElements;
    }

    private void writeClassifierFile (int a_label, String a_strActivity, String a_strSensor, double[][] a_FeaturesArr){
        File dataFile;
        String path = Environment.getExternalStorageDirectory() + "/Health Care/"+a_strSensor;
        dataFile = new File(path);
        if (!dataFile.exists())
            dataFile.mkdirs();

        dataFile = new File(path + "/" + a_strActivity + ".csv");

        if(dataFile.exists())
            dataFile.delete();

        try{
            FileOutputStream fos = new FileOutputStream(dataFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (double[] features : a_FeaturesArr)
            {
                bw.write(a_label+",");
                for (int i = 0 ; i<47 ; i++)
                {
                    bw.write(features[i]+",");
                }
                bw.write(features[47]+"\n");
            }

            bw.close();
            fos.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
