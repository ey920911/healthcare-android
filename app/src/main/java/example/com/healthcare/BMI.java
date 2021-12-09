package example.com.healthcare;

public class BMI
{
    private double height;
    private double weight;

    public BMI(double height, double weight)
    {
        this.height = height;
        this.weight = weight;
    }

    public double calculateBMI(){return weight/(height/100 * height/100);}
    public String checkObesity()
    {
        double calbmi = calculateBMI();
        if(calbmi < 18.5){return "저체중";}
        else if(calbmi< 23){return "정상체중";}
        else if(calbmi < 25){return "과체중";}
        else if(calbmi < 30){return "경도비만";}
        else if(calbmi < 40){return "비만";}
        else{return "고도비만";}
    }
}
