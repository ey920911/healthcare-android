package knn;

/**
 * KNN 을 사용하기 위하여 두 double 타입 배열의 distance 를 구하기 위한 클래스이다.
 */
public class MyDistanceCalculator {

    private double[] m_doubleArr1;
    private double[] m_doubleArr2;

    public MyDistanceCalculator(){
        m_doubleArr1=null;
        m_doubleArr2=null;
    }

    public MyDistanceCalculator(Double[] a_doubleArr1, Double[] a_doubleArr2){
        setTwoDoubleArrs(a_doubleArr1, a_doubleArr2);
    }

    public MyDistanceCalculator(double[] a_doubleArr1, double[] a_doubleArr2) {
        if(a_doubleArr1 == null || a_doubleArr2 == null || a_doubleArr1.length != a_doubleArr2.length) {
            m_doubleArr1 = null;
            m_doubleArr2 = null;
            return;
        }

        m_doubleArr1=a_doubleArr1;
        m_doubleArr2=a_doubleArr2;
    }/** MyDistanceCalculator()
    * 생성자이다. double 타입, Double 타입의 배열을 인자로 받아서 멤버변수를 세팅한다.
    * 아무 인자가 없다면 멤버변수를 null 로 세팅한다.
    */

    public void setTwoDoubleArrs(double[] a_doubleArr1, double[] a_doubleArr2){
        if(a_doubleArr1 == null || a_doubleArr2 == null || a_doubleArr1.length != a_doubleArr2.length) {
            m_doubleArr1 = null;
            m_doubleArr2 = null;
            return;
        }

        m_doubleArr1=a_doubleArr1;
        m_doubleArr2=a_doubleArr2;
    }
    public void setTwoDoubleArrs(Double[] a_doubleArr1, Double[] a_doubleArr2){
        if(a_doubleArr1 == null || a_doubleArr2 == null || a_doubleArr1.length != a_doubleArr2.length) {
            m_doubleArr1 = null;
            m_doubleArr2 = null;
            return;
        }

        m_doubleArr1=new double[a_doubleArr1.length];
        m_doubleArr2=new double[a_doubleArr2.length];

        for (int i = 0 ; i<a_doubleArr1.length ; i++) {
            m_doubleArr1[i] = (double) a_doubleArr1[i];
            m_doubleArr2[i] = (double) a_doubleArr2[i];
        }

    }/**setTowDoubleArrs();
     * 멤버변수인 두개의 double 배열을 세팅하는 메서드이다.
     * 인자는 double 배열, Double 배열 둘 다 가능하다.
     *
     */


    public double getDistance(){

        if (m_doubleArr1 == null || m_doubleArr2== null || m_doubleArr1.length != m_doubleArr2.length)
        {
            return -9999;
        }

        double distance = 0;

        for(int i = 0 ; i<m_doubleArr1.length ; i++){
            distance += Math.pow(m_doubleArr1[i] - m_doubleArr2[i], 2);
        }

        return distance;
    }/** getDistance();
    * 두 double 배열이 세팅되어있다면, 이 메서드로 그 두 배열 사이의 distance 를 구하고, 리턴한다.
    *
    */

}
