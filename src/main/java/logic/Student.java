package logic;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Student {

    private String name;
    private String major;
    private ArrayList<Double> grades;

    public Student(String name, String major,ArrayList<Double> grades){
        this.name = name;
        this.major = major;
        this.grades = grades;
    }
    public String getName() {
        return name;
    }
    public String getMajor() {
        return major;
    }
    public ArrayList<Double> getGrades() {
        return grades;
    }

    public double computeGradeAverage(){
        double sum = 0;
        for(int i = 0; i < grades.size(); i++){
            sum = sum + grades.get(i);
        }
        final DecimalFormat df = new DecimalFormat("0.0");
        return Double.parseDouble(df.format(sum / (grades.size())));
    }
}
