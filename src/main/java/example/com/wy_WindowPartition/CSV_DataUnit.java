package example.com.wy_WindowPartition;

import java.util.ArrayList;
import knn.finalStaticVariables;

public class CSV_DataUnit {
  ArrayList<Double> accX;
  ArrayList<Double> accY;
  ArrayList<Double> accZ;
  ArrayList<Integer> accT;

  ArrayList<Double> gyroX;
  ArrayList<Double> gyroY;
  ArrayList<Double> gyroZ;
  ArrayList<Integer> gyroT;

  public CSV_DataUnit() {
    // TODO Auto-generated constructor stub
    accX = new ArrayList<Double>();
    accY = new ArrayList<Double>();
    accZ = new ArrayList<Double>();
    accT = new ArrayList<Integer>();
    gyroX = new ArrayList<Double>();
    gyroY = new ArrayList<Double>();
    gyroZ = new ArrayList<Double>();
    gyroT = new ArrayList<Integer>();
  }

  public ArrayList<Double> getAccX() {
    return accX;
  }

  public void setAccX(ArrayList<Double> accX) {
    this.accX = accX;
  }

  public ArrayList<Double> getAccY() {
    return accY;
  }

  public void setAccY(ArrayList<Double> accY) {
    this.accY = accY;
  }

  public ArrayList<Double> getAccZ() {
    return accZ;
  }

  public void setAccZ(ArrayList<Double> accZ) {
    this.accZ = accZ;
  }

  public ArrayList<Integer> getTimeAcc() {
    return accT;
  }

  public void setTimeAcc(ArrayList<Integer> accT) {
    this.accT = accT;
  }

  public ArrayList<Double> getGyroX() {
    return gyroX;
  }

  public void setGyroX(ArrayList<Double> gyroX) {
    this.gyroX = gyroX;
  }

  public ArrayList<Double> getGyroY() {
    return gyroY;
  }

  public void setGyroY(ArrayList<Double> gyroY) {
    this.gyroY = gyroY;
  }

  public ArrayList<Double> getGyroZ() {
    return gyroZ;
  }

  public void setGyroZ(ArrayList<Double> gyroZ) {
    this.gyroZ = gyroZ;
  }

  public ArrayList<Integer> getGyroTime() {
    return gyroT;
  }

  public void setGyroTime(ArrayList<Integer> gyroT) {
    this.gyroT = gyroT;
  }

  public void addAccelerateData(double x, Double y, Double z, int t) {
    if (
      accX.size() == accY.size() &&
      accY.size() == accZ.size() &&
      accZ.size() == accT.size()
    ) {
      accX.add(x);
      accY.add(y);
      accZ.add(z);
      accT.add(t);
    } else {
      System.out.println("Accelerate data's size is different please check!");
      System.out.println(
        accX.size() +
        ",  " +
        accY.size() +
        ",  " +
        accZ.size() +
        ",  " +
        accT.size()
      );
    }
  }

  public void addGyroscopeData(Double x, Double y, Double z, int t) {
    if (
      gyroX.size() == gyroY.size() &&
      gyroY.size() == gyroZ.size() &&
      gyroZ.size() == gyroT.size()
    ) {
      gyroX.add(x);
      gyroY.add(y);
      gyroZ.add(z);
      gyroT.add(t);
    } else {
      System.out.println("Gyroscope data's size is different please check!");
      System.out.println(
        gyroX.size() +
        ",  " +
        gyroY.size() +
        ",  " +
        gyroZ.size() +
        ",  " +
        gyroT.size()
      );
    }
  }

  public void printUnits(int type) {
    if (
      accX.size() == accY.size() &&
      accY.size() == accZ.size() &&
      accZ.size() == accT.size() &&
      gyroX.size() == gyroY.size() &&
      gyroY.size() == gyroZ.size() &&
      gyroZ.size() == gyroT.size()
    ) {
      int size = type == finalStaticVariables.ACCELERATE_DATA_TYPE
        ? accX.size()
        : gyroX.size();
      printUnits(type, size);
    } else {
      System.out.println(
        "Accelerate or Gyroscope data's size is different please check!"
      );
      System.out.println(
        accX.size() +
        ",  " +
        accY.size() +
        ",  " +
        accZ.size() +
        ",  " +
        accT.size()
      );
    }
  }

  public void printUnits(int type, int size) {
    switch (type) {
      case finalStaticVariables.ALL_DATA_TYPE:
        printUnits(finalStaticVariables.ACCELERATE_DATA_TYPE, size);
        printUnits(finalStaticVariables.GYROSCOPE_DATA_TYPE, size);
        break;
      case finalStaticVariables.ACCELERATE_DATA_TYPE:
        for (int i = 0; i < size; i++) {
          System.out.print(
            "(" +
            accX.get(i) +
            ", " +
            accY.get(i) +
            ", " +
            accZ.get(i) +
            "," +
            accT.get(i) +
            ")\t"
          );
        }
        break;
      case finalStaticVariables.GYROSCOPE_DATA_TYPE:
        for (int i = 0; i < size; i++) {
          System.out.print(
            "(" +
            gyroX.get(i) +
            ", " +
            gyroY.get(i) +
            ", " +
            gyroZ.get(i) +
            ", " +
            gyroT.get(i) +
            ")\t"
          );
        }
        break;
      default:
        System.out.println("undefined type is come : " + type);
    }

    System.out.println();
  }

  public void clean() {
    accX.clear();
    accY.clear();
    accZ.clear();
    accT.clear();
    gyroX.clear();
    gyroY.clear();
    gyroZ.clear();
    gyroT.clear();
  }
}
