package example.com.healthcare;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Classifier_Thread extends Thread {
    private float[] gravity;
    private String STRSAVEPATH = Environment
            .getExternalStorageDirectory() + "/Monitoring/";
    private File file, file2;
    private File dir;
    private String data;
    private boolean isAlive = false;
    private String phone_state = null;
    private String[] label = { "X", "Y", "Z", "Time" };
    private int accCount = 0;
    private int gyroCount = 0;
    private int label_activity;
    private DetectHandler handler; //

    public Classifier_Thread(DetectHandler handler){this.handler = handler;}//

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        isAlive = true;
        boolean accDataFull = false;
        boolean gyroDataFull = false;
        //make_init();
        while (isAlive) {

            if (!Info.raw_data.isEmpty() && !accDataFull ) {

                String[] sendata = Info.raw_data.poll();
                Info.m_mcfcAcc.addM_doubleListX(Double.parseDouble(sendata[0]));
                Info.m_mcfcAcc.addM_doubleListY(Double.parseDouble(sendata[1]));
                Info.m_mcfcAcc.addM_doubleListZ(Double.parseDouble(sendata[2]));

                if (accCount%100==99)
                {
                    accDataFull=true;
                    accCount=-1;
                }

                accCount++;
            }
            if (!Info.raw_data2.isEmpty() && !gyroDataFull) {

                String[] sendata = Info.raw_data2.poll();
                Info.m_mcfcGyro.addM_doubleListX(Double.parseDouble(sendata[0]));
                Info.m_mcfcGyro.addM_doubleListY(Double.parseDouble(sendata[1]));
                Info.m_mcfcGyro.addM_doubleListZ(Double.parseDouble(sendata[2]));

                if (gyroCount%100==99)
                {
                    gyroDataFull=true;
                    gyroCount=-1;
                }

                gyroCount++;
            }

            if (accDataFull && gyroDataFull)
            {
                accDataFull=false;
                gyroDataFull=false;

                double[] featuresOfAccSens = Info.m_mcfcAcc.getFeatures();
                if(featuresOfAccSens[4] < 5 && featuresOfAccSens[12] < 5 && featuresOfAccSens[20] < 5)
                {
                    handler.sendEmptyMessage(5);
                    Log.e("label","5");
                }

                else{
                    double[] featuresOfTwoSensors = new double[96];
                    double[] featuresOfGyroSens = Info.m_mcfcGyro.getFeatures();
                    for (int i = 0; i<48 ; i++)
                        featuresOfTwoSensors[i] = featuresOfAccSens[i];
                    for (int i = 0; i<48 ; i++)
                        featuresOfTwoSensors[i+48] = featuresOfGyroSens[i];

                    Log.d("accX variance" , featuresOfTwoSensors[4]+"");

                    label_activity = Info.m_mcfcAcc.classify_Test(featuresOfTwoSensors);
                    Log.e("label",label_activity+"");
                    handler.sendEmptyMessage(label_activity);//
                }
                Info.m_mcfcAcc.clear_array();
                Info.m_mcfcGyro.clear_array();
            }

        }

    }

    public void make_init(){
        dir = makeDirectory(STRSAVEPATH);

		/*file = makeFile(dir, STRSAVEPATH + state + "/" + phone_state + "/"
				+ data + "-" + strCutTime + "accelation_0.csv");

		writeFile(file, label);
		file2 = makeFile(dir, STRSAVEPATH + state + "/" + phone_state + "/"
				+ data + "-" + strCutTime + "gyroscope_0.csv");
		writeFile(file2, label);*/
    }


    private File makeDirectory(String dir_path) {
        File dir = new File(dir_path);
        if (!dir.exists()) {
            dir.mkdirs();

        } else {

        }

        return dir;
    }

    private File makeFile(File dir, String file_path) {
        File file = null;
        boolean isSuccess = false;
        if (dir.isDirectory()) {
            file = new File(file_path);
            if (file != null && !file.exists()) {

                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
            } else {

            }
        }
        return file;
    }

    private boolean writeFile(File file, String[] sendData) {
        String[] sendDatas = sendData;
        boolean result;
        FileOutputStream fos;
        String tab = ",";

        Log.e("send", sendDatas[0] + sendDatas[1] + sendDatas[2] + sendDatas[3]);
        if (file != null && file.exists()) {
            try {
                fos = new FileOutputStream(file, true);
                try {
                    for (int i = 0; i < sendDatas.length; i++) {

                        fos.write(sendDatas[i].getBytes());
                        fos.write(tab.getBytes());
                    }

                    fos.write(13);
                    fos.write(10);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public void stopThread() {
        isAlive = false;
    }
}
