package knn;

import java.util.Arrays;

/**
 * feature extraction 을 위해 수학 계산 메서드들이 들어있다.
 */
public class MyFeatureExtractor {
  private double[] m_doubleArr;
  private double[] m_dAbsElementSubMedian;

  public MyFeatureExtractor(Double[] a_doubleArr) {
    setDoubleArr(a_doubleArr);
  }

  public MyFeatureExtractor(double[] a_doubleArr) {
    if (a_doubleArr == null) {
      m_doubleArr = null;
      return;
    }

    m_doubleArr = a_doubleArr;
    Arrays.sort(m_doubleArr);
  }

  public void setDoubleArr(Double[] a_doubleArr) {
    if (a_doubleArr == null) {
      m_doubleArr = null;
      return;
    }

    m_doubleArr = new double[a_doubleArr.length];

    int i = 0;
    for (Double d : a_doubleArr) m_doubleArr[i++] = (double) d;

    Arrays.sort(m_doubleArr);
  }

  public double getVariance() {
    if (m_doubleArr == null) {
      return -9999;
    }

    double aver = getAverage();
    double sumOfSquare = getSumOfSquare();

    return sumOfSquare / m_doubleArr.length - aver * aver;
  }

  private double getSumOfSquare() {
    if (m_doubleArr == null) {
      return -9999;
    }

    double sumOfSquare = 0;

    for (double d : m_doubleArr) sumOfSquare += d * d;

    return sumOfSquare;
  }

  public double getStandardDeviation() {
    if (m_doubleArr == null) {
      return -9999;
    }

    double standardDeviation = getVariance();

    return Math.sqrt(standardDeviation);
  }

  public double getAverage() {
    if (m_doubleArr == null) {
      return -9999;
    }

    double total = 0;

    for (double d : m_doubleArr) total += d;

    return total / m_doubleArr.length;
  }

  public double getMedianAbsoluteDeviation() {
    if (m_doubleArr == null) {
      return -9999;
    }

    double median = getMedian();

    m_dAbsElementSubMedian = new double[m_doubleArr.length];

    int i = 0;
    for (double d : m_doubleArr) m_dAbsElementSubMedian[i++] =
      Math.abs(d - median);

    Arrays.sort(m_dAbsElementSubMedian);

    return m_dAbsElementSubMedian[(int) (
        (m_dAbsElementSubMedian.length - 1) / 2
      )];
  }

  public double getMaxValue() {
    if (m_doubleArr == null) {
      return -9999;
    }

    return m_doubleArr[m_doubleArr.length - 1];
  }

  public double getMinValue() {
    if (m_doubleArr == null) {
      return -9999;
    }
    return m_doubleArr[0];
  }

  public double getMedian() {
    if (m_doubleArr == null) {
      return -9999;
    }

    return m_doubleArr[(int) ((m_doubleArr.length - 1) / 2)];
  }

  public double getRootMeanSquare() {
    if (m_doubleArr == null) {
      return -9999;
    }

    double sumOfSquare = getSumOfSquare();

    return Math.sqrt(sumOfSquare / m_doubleArr.length);
  }

  public double[] getFeatures() {
    if (m_doubleArr == null) return null;

    double[] features = new double[8];

    features[0] = getMaxValue();
    features[1] = getMinValue();
    features[2] = getAverage();
    features[3] = getMedian();
    features[4] = getVariance();
    features[5] = getStandardDeviation();
    features[6] = getMedianAbsoluteDeviation();
    features[7] = getRootMeanSquare();

    return features;
  } // getFeatures();
  // double 배열(feature)를 리턴한다.
  // MaxValue,
  // MinValue,
  // Average,
  // Median,
  // Variance,
  // StandardDeviation,
  // MedianAbsoluteDeviation,
  // RootMeanSquare
}
